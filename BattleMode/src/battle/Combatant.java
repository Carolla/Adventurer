package battle;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import mylib.pdc.MetaDie;
import battle.ActionSelector.CombatAction;


public class Combatant implements CombatantInterface {
	private TargetStrategy _targetStrategy;
	private static Map<CombatantType, Integer> _typeCounts = new TreeMap<CombatantType, Integer>();
	
	protected String _name = null;
	protected int _hp = 10;
	protected int _ac = 10;
	protected int _initiative = 10;
	protected int _strength = 10;
	protected int _dexterity = 10;
	protected static final MetaDie _metadie = new MetaDie(System.currentTimeMillis());
	protected boolean _shouldTryEscaping = false;
	protected int _attackRoll = 0;

	protected CombatantType _type = CombatantType.HERO;
	protected CombatantAttack _attack = CombatantAttack.NORMAL;
	protected CombatantWeapon _weapon = CombatantWeapon.ONE_DAMAGE_WEAPON;
	protected Set<CombatantArmor> _armors = new TreeSet<CombatantArmor>();

	private final ActionSelector _actionSelector;
	
	public Combatant(TargetStrategy strategy)
	{
		_targetStrategy = strategy;
		_actionSelector = new ActionSelector();
	}

    /* (non-Javadoc)
	 * @see battle.CombatantInterface#isDefeated()
	 */
	@Override
	public boolean isDefeated() {
		return isUnconscious(); 
	}

	/* (non-Javadoc)
	 * @see battle.CombatantInterface#isUnconscious()
	 */
    @Override
	public boolean isUnconscious()
    {
        return _hp <= 0;
    }

    private CombatantInterface lastTarget = null;

    /* (non-Javadoc)
	 * @see battle.CombatantInterface#takeTurn(java.util.List, battle.Battle)
	 */
    @Override
	public int takeTurn(List<CombatantInterface> combatants, Battle battle)
    {
    	int damage = 0;
    	if (!isDefeated()) {
	    	CombatAction action = _actionSelector.getNextAction();
	    	switch (action)
	    	{
		    	case ATTACK:
		    		lastTarget = selectTarget(combatants);
		    		damage = attack(lastTarget);
		    		if (lastTarget.isDefeated()) {
		    			lastTarget = null;
		    		}
		    		break;
		    	case FLEE:
		    		tryToEscape(battle);
		    		break;
		    	case EQUIP_WEAPON:
		    		CombatantWeapon weapon = _actionSelector.selectWeapon();
		    		equip(weapon);
		    		break;
		    	case EQUIP_ARMOR:
		    		CombatantArmor armor= _actionSelector.selectArmor();
		    		equip(armor);
		    		break;
				case ATTACK_SAME:
					if (lastTarget == null) {
			    		lastTarget = selectTarget(combatants);
					}
					damage = attack(lastTarget);
					break;
	    	}
    	}
    	return damage;
    }

	protected void tryToEscape(Battle battle)
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
            System.out.print("The enemy tries to escape.  ");
            if (battle.escape(this)) {
                System.out.println("The enemy escaped!");
            } else{
                System.out.println("The enemy did not escape!");
            }
        }
    }

    protected boolean shouldAttack()
    {
    	if (_shouldTryEscaping)
    	{
    		return _hp > 1;
    	} else {
    		return true;
    	}
    }

    private Attack makeAttackAndDamageRoll()
    {
    	int damage = 0;
    	
    	switch(_weapon)
    	{
    	case DAGGER:
    		damage = _metadie.getRandom(2,4);
    		break;
    	case MORNING_STAR:
    		damage = _metadie.getRandom(3,6);
    		break;
    	case ONE_DAMAGE_WEAPON:
    	default:
    		damage = 1;
    		break;
    	}
    	
    	damage += getStrengthBonus();
    	
    	int attackRoll = 0;
    	switch(_attack)
    	{
    	case CUSTOM:
    		attackRoll = _attackRoll;
    		break;
    	case NORMAL:
    		attackRoll = _metadie.getRandom(1,20);
    		break;
    	}
    	
    	attackRoll += getDexterityBonus();
    	
    	return new Attack(attackRoll, damage);
    }

    private int getDexterityBonus() {
    	return getAttributeBonus(_dexterity);
	}

    private int getStrengthBonus() {
    	return getAttributeBonus(_strength);
	}
    
	private final int MIN_ATTRIBUTE_BONUS = 16;
    private final int MAX_ATTRIBUTE_PENALTY = 5;
    private int getAttributeBonus(int attribute) {
    	int bonus = 0;
    	if (attribute >= MIN_ATTRIBUTE_BONUS) {
    		bonus = (attribute - (MIN_ATTRIBUTE_BONUS - 2)) / 2;
    	} else if (attribute <= MAX_ATTRIBUTE_PENALTY) {
    		bonus = (attribute - (MAX_ATTRIBUTE_PENALTY + 2)) / 2;
    	}
		return bonus;
	}

    /* (non-Javadoc)
	 * @see battle.CombatantInterface#attacked(battle.Attack)
	 */
    @Override
    public int attacked(Attack attack)
    {
    	int effectiveAc = _ac + getDexterityBonus();
        if ((attack.hitRoll() >= effectiveAc && attack.hitRoll() > 1) || attack.hitRoll() == 20) {
            int damage = attack.damageRoll();
            _hp = _hp - damage;
            displayHit(damage);
            displayHP();
            return damage;
        } else {
            displayMiss();
        }
        return 0;
    }

    private void displayMiss()
    {
        if (_type == CombatantType.HERO) {
            System.out.println("The enemy missed.  ");
        } else {
            System.out.println("You missed.  ");
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

    /* (non-Javadoc)
	 * @see battle.CombatantInterface#hasFullHP()
	 */
    @Override
	public boolean hasFullHP()
    {
        return (_hp == 10);
    }


    /* (non-Javadoc)
	 * @see battle.CombatantInterface#displayHP()
	 */
    @Override
	public void displayHP()
    {
        if (_type == CombatantType.HERO) {
            System.out.println("You have " + _hp + " HP left.");
        } else {
            System.out.println("The enemy has " + _hp + " HP left.");
        }        
    }



    /* (non-Javadoc)
	 * @see battle.CombatantInterface#displayVictory()
	 */
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

	/* (non-Javadoc)
	 * @see battle.CombatantInterface#rollInitiative()
	 */
	@Override
	public int rollInitiative() {
		return _initiative;
	}


	/* (non-Javadoc)
	 * @see battle.CombatantInterface#attack(battle.Combatant)
	 */
	@Override
	public int attack(CombatantInterface target) {
		Attack attack = makeAttackAndDamageRoll();
		if (attack.hitRoll() > 1) {
			return target.attacked(attack);
		} else {
			unequip(_weapon);
			return 0;
		}
	}

	/* (non-Javadoc)
	 * @see battle.CombatantInterface#equip(battle.Combatant.CombatantArmor)
	 */
	@Override
	public boolean equip(CombatantArmor armor) {
		if (_armors.add(armor)) {
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
			return true;
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see battle.CombatantInterface#unequip(battle.Combatant.CombatantArmor)
	 */
	@Override
	public boolean unequip(CombatantArmor armor) {
		if (_armors.remove(armor)) {
			switch(armor)
			{
			case HELMET:
				_ac -= 1;
				break;
			case SHIELD:
				_ac -= 2;
				break;
			case CHAIN_MAIL:
				_ac -= 4;
				break;
			}
			return true;
		}
		return false;
	}
	

	/* (non-Javadoc)
	 * @see battle.CombatantInterface#unequip(battle.Combatant.CombatantWeapon)
	 */
	@Override
	public boolean unequip(CombatantWeapon weapon) {
		if (_weapon == weapon) {
			_weapon = CombatantWeapon.ONE_DAMAGE_WEAPON;
			return true;
		}			
		return false;
	}
	
	/* (non-Javadoc)
	 * @see battle.CombatantInterface#equip(battle.Combatant.CombatantWeapon)
	 */
	@Override
	public boolean equip(CombatantWeapon weapon) {
		if (_weapon != weapon) {
			_weapon = weapon;    	
			switch(_weapon)
	    	{
	    	case DAGGER:
	    		System.out.println("Equipped a dagger");
	    		break;
	    	case MORNING_STAR:
	    		System.out.println("Equipped a morning star!");
	    		break;
	    	case ONE_DAMAGE_WEAPON:
	    		System.out.println("You are fighting with nothing but your fists");
	    		break;
	    	}
	    	return true;
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see battle.CombatantInterface#equip(battle.Combatant.CombatantType)
	 */
	@Override
	public boolean isType(CombatantType type) {
		return _type == type;
	}

	private static List<CombatantInterface> findAllCombatantsByType(List<CombatantInterface> _combatants, CombatantType type) {
		List<CombatantInterface> result = new ArrayList<CombatantInterface>();
		for (CombatantInterface c : _combatants) {
			if (c.isType(type)) {
				result.add(c);
			}
		}
		return result;
	}
	
	public static List<CombatantInterface> findAllEnemies(List<CombatantInterface> _combatants) {
		return findAllCombatantsByType(_combatants, CombatantType.ENEMY);
	}
	
	public static List<CombatantInterface> findAllHeros(List<CombatantInterface> combatants) {
		return findAllCombatantsByType(combatants, CombatantType.HERO);
	}

	@Override
	public CombatantInterface selectTarget(List<CombatantInterface> combatants) {
		return  _targetStrategy.selectTarget(combatants);
	}

	@Override
	public String name() {
		if (_name == null) {
			int num = _typeCounts.containsKey(_type) ? _typeCounts.get(_type) : 0;
			_typeCounts.put(_type, ++num);
			if (_type == CombatantType.HERO) {
				_name = "HERO" + num;
			} else {
				_name = "ENEMY" + num;
			}
		}
		return _name;
	}
}
