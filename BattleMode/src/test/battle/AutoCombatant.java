package test.battle;

import java.util.Set;
import java.util.TreeSet;

import mylib.pdc.MetaDie;
import battle.Attack;
import battle.Battle;
import battle.Combatant;

public class AutoCombatant implements Combatant {

	private int _hp = 10;
    private int _ac = 10;
    private int _turnCount = 0;
	private int _initiative = 10;
    private CombatantType _type = CombatantType.HERO;
	private CombatantAttack _attack = CombatantAttack.NORMAL;
	private CombatantDamage _dmg = CombatantDamage.FIST;
    private MetaDie _metadie;
	private boolean _shouldTryEscaping = false;
	public int _attackRoll;
	private Set<CombatantArmor> _armor = new TreeSet<CombatantArmor>();

    
	/**
	 */
	private AutoCombatant(CombatantType type)
	{
	    _type = type;
	    _metadie = new MetaDie(System.currentTimeMillis());
	}
	
	public static class CombatantBuilder
	{
		private int withHp = 10;
		private int withInitiative = 10;
		private boolean withEscape = false;
		private CombatantAttack withAttack = CombatantAttack.NORMAL;
		private CombatantDamage withDmg = CombatantDamage.FIST;
		private CombatantType withType = CombatantType.HERO;
		private int withAttackRoll = 10;
		private int withAc = 10;
		private Set<CombatantArmor> withArmors = new TreeSet<CombatantArmor>();
		
		public CombatantBuilder() { }
		
		public CombatantBuilder withType(CombatantType type)
		{
			withType = type;
			return this;
		}
		
		public CombatantBuilder withHP(int hp)
		{
			withHp = hp;
			return this;
		}
		
		public CombatantBuilder shouldTryEscaping()
		{
			withEscape = true;
			return this;
		}
		
		public CombatantBuilder withHit(CombatantAttack attack)
		{
			withAttack = attack;
			return this;
		}
		
		public CombatantBuilder withSpecificHit(int attackRoll)
		{
			withAttack = CombatantAttack.CUSTOM;
			withAttackRoll = attackRoll;
			return this;
		}
		
		public CombatantBuilder withDamage(CombatantDamage dmg)
		{
			withDmg = dmg;
			return this;
		}

		public CombatantBuilder withAC(int ac) {
			withAc = ac;
			return this;
		}
		
		public AutoCombatant build()
		{
			AutoCombatant auto = new AutoCombatant(withType);
			auto._hp = withHp;
			auto._dmg = withDmg;
			auto._attack = withAttack;
			auto._attackRoll = withAttackRoll;
			auto._ac = withAc;
			auto._shouldTryEscaping = withEscape;
			auto._initiative = withInitiative;
			for (CombatantArmor piece : withArmors) {
				auto.equip(piece);
			}
			return auto;
		}

		public CombatantBuilder withInitiative(int initiative) {
			withInitiative = initiative;
			return this;
		}

		public CombatantBuilder withArmor(CombatantArmor armor) {
			withArmors.add(armor);
			return this;
		}

	}
	
    public int getTurnCount()
    {
       return _turnCount;
    }

    @Override
	public boolean isDefeated() {
		return isUnconscious(); 
	}

    @Override
    public boolean isUnconscious()
    {
        return _hp <= 0;
    }

    @Override
    public int takeTurn(Combatant opponent, Battle battle)
    {
        _turnCount++;
        if (shouldAttack())
        {
            return attack(opponent);
        } else {
            tryToEscape(battle);
            return 0;
        }
    }

    private void tryToEscape(Battle battle)
    {
        if (_type == CombatantType.HERO)
        {
            System.out.print("You try to escape.  ");
            if (battle.escape(this)) {
                System.out.println("You escaped!");
            } else{
                System.out.println("You did not escape!");
            }
        } else {
            System.out.println("Enemies do not flee in battle.");
        }
            
    }

    private boolean shouldAttack()
    {
    	if (_shouldTryEscaping)
    	{
    		return _hp > 1;
    	} else {
    		return true;
    	}
    }

    private Attack makeAttackRoll()
    {
    	int damage = 0;
    	int attack = 0;
    	
    	switch(_dmg)
    	{
    	case DAGGER:
    		damage = _metadie.getRandom(1,4);
    		break;
    	case MORNING_STAR:
    		damage = _metadie.getRandom(2,6);
    		break;
    	case FIST:
    	default:
    		damage = 1;
    		break;
    	}
    	
    	switch(_attack)
    	{
    	case AUTO_MISS:
    		attack = 0;
    		break;
    	case CUSTOM:
    		attack = _attackRoll;
    		break;
    	case NORMAL:
    		attack = _metadie.getRandom(1,20);
    		break;
    	case ACCURATE:
    		attack = _metadie.getRandom(9,20);
    		break;
    	case AUTO_HIT:
    		attack = 20;
    		break;
    	}
    	
    	return new Attack(attack, damage);
    }

    @Override
    public int attacked(Attack attack)
    {
        if (attack.hitRoll() >= _ac && attack.hitRoll() > 0) {
            int damage = attack.damageRoll();
            _hp = _hp - damage;
            displayHit(damage);
            return damage;
        } else {
            displayMiss();
        }
        return 0;
    }

    private void displayMiss()
    {
        if (_type == CombatantType.HERO) {
            System.out.print("The enemy missed.  ");
        } else {
            System.out.print("You missed.  ");
        }
    }

    private void displayHit(int damage)
    {
        if (_type == CombatantType.HERO) {
            System.out.print("The enemy hit you for " + damage + " damage.  ");
        } else {
            System.out.print("You hit the enemy for " + damage + " damage.  ");
        }
    }

    @Override
    public boolean hasFullHP()
    {
        return (_hp == 10);
    }

    @Override
    public void displayHP()
    {
        if (_type == CombatantType.HERO) {
            System.out.println("You have " + _hp + " HP left.");
        } else {
            System.out.println("The enemy has " + _hp + " HP left.");
        }        
    }

	@Override
	public void displayVictory() {
		switch (_type) 
		{
		case HERO:
			System.out.println("You have won!");
			break;
		case ENEMY:
		default:
			System.out.println("The enemy has won...");
			break;
		}
		
	}

	@Override
	public int rollInitiative() {
		return _initiative;
	}

	@Override
	public int attack(Combatant victim) {
		return victim.attacked(makeAttackRoll());
	}

	@Override
	public void equip(CombatantArmor armor) {
		if (_armor.add(armor)) {
			switch(armor)
			{
			case HELMET:
				_ac += 1;
				break;
			case SHIELD:
				_ac += 2;
				break;
			case CHAIN_MAIL:
				_ac += 4;
				break;
			}
		}
	}
}
