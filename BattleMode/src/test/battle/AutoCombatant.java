package test.battle;

import mylib.pdc.MetaDie;
import battle.Attack;
import battle.Battle;
import battle.Combatant;

public class AutoCombatant implements Combatant {

	private int _hp = 10;
    private int _turnCount = 0;
    private int _ac = 10;
    private final CombatantType _type;
    private MetaDie _metadie;

    public enum CombatantType {HERO, ENEMY, STRONG_ENEMY, WEAK_ENEMY, FEEBLE_HERO, AUTO_HIT};

	/**
	 */
	public AutoCombatant(CombatantType type)
	{
	    _type = type;
	    _metadie = new MetaDie();
	}
	
	public static class CombatantBuilder
	{
		private int withHp;
		private CombatantType withType;
		public CombatantBuilder(CombatantType type)
		{
			withType = type;
		}
		
		public CombatantBuilder withHP(int hp)
		{
			withHp = hp;
			return this;
		}
		
		public Combatant build()
		{
			AutoCombatant auto = new AutoCombatant(withType);
			auto._hp = withHp;
			return auto;
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
            return opponent.attacked(makeAttackRoll());
        } else {
            tryToEscape(battle);
            return 0;
        }
    }

    private void tryToEscape(Battle battle)
    {
        if (_type == CombatantType.HERO || _type == CombatantType.FEEBLE_HERO)
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
        if (_type == CombatantType.HERO || _type == CombatantType.FEEBLE_HERO)
        {
            return _hp > 1;
        } else {
            return true;
        }
    }

    private Attack makeAttackRoll()
    {
        if (_type == CombatantType.HERO) {
            return new Attack(_metadie.getRandom(8, 14), _metadie.getRandom(1,2));
        } else if (_type == CombatantType.FEEBLE_HERO) {
            return new Attack(0,0);
        } else if (_type == CombatantType.STRONG_ENEMY) {
            return new Attack(_metadie.getRandom(10, 12), 2);
        } else if (_type == CombatantType.WEAK_ENEMY) {
            return new Attack(_metadie.getRandom(4, 11), 1);
        } else if (_type == CombatantType.AUTO_HIT) {
        	return new Attack(20, 1);
        } else {
            return new Attack(_metadie.getRandom(6, 12), _metadie.getRandom(1,3));
        }
    }

    @Override
    public int attacked(Attack attack) //AttackRoll attackRoll, DamageRoll damageRoll)
    {
        if (attack.hitRoll() > _ac) {
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
        if (_type == CombatantType.HERO || _type == CombatantType.FEEBLE_HERO) {
            System.out.print("The enemy missed.  ");
        }
        else {
            System.out.print("You missed.  ");
        }
    }

    private void displayHit(int damage)
    {
        if (_type == CombatantType.HERO || _type == CombatantType.FEEBLE_HERO) {
            System.out.print("The enemy hit you for " + damage + " damage.  ");
        }
        else {
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
        if (_type == CombatantType.HERO || _type == CombatantType.FEEBLE_HERO) {
            System.out.println("You have " + _hp + " HP left.");
        } else {
            System.out.println("The enemy has " + _hp + " HP left.");
        }        
    }
}
