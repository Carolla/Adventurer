package battle;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import mylib.pdc.MetaDie;


public class Combatant {

    public enum CombatantType {HERO, ENEMY};
    public enum CombatantAttack {CUSTOM, NORMAL};
    public enum CombatantWeapon {ONE_DAMAGE_WEAPON, DAGGER, MORNING_STAR};
    public enum CombatantArmor {HELMET, SHIELD, CHAIN_MAIL};
    
	protected int _hp = 10;
	protected int _ac = 10;
	protected int _turnCount = 0;
	protected int _initiative = 10;
	protected int _strength = 10;
	protected final MetaDie _metadie = new MetaDie(System.currentTimeMillis());
	protected boolean _shouldTryEscaping = false;
	protected int _attackRoll = 0;

	protected CombatantType _type = CombatantType.HERO;
	protected CombatantAttack _attack = CombatantAttack.NORMAL;
	protected CombatantWeapon _weapon = CombatantWeapon.ONE_DAMAGE_WEAPON;
	protected Set<CombatantArmor> _armors = new TreeSet<CombatantArmor>();

    /**
     * Status of player in battle - defeated player is out of battle
     * 
     * @return whether player is participating in battle anymore
     */
	public boolean isDefeated() {
		return isUnconscious(); 
	}

	/**
	 * Whether player is able to take actions in the battle
	 * 
	 * @return players consciousness
	 */
    public boolean isUnconscious()
    {
        return _hp <= 0;
    }

    /**
     * Each combtant executes actions during a battle, turn by turn.  Takes
     * the next turn for a combatant.
     * @param selectTarget(_combatants the enemy to strike this round
     * @param battle the current battle
     * @return the damage done
     */
    public int takeTurn(List<Combatant> combatants, Battle battle)
    {
        _turnCount++;
        if (shouldAttack())
        {
        	Combatant target = selectTarget(combatants);
            int damage = attack(target);
            target.displayHP();
            return damage;
        } else {
            tryToEscape(battle);
            return 0;
        }
    }

    /**
     * Target selection method.  Combatant chooses from the list of available
     * Combatants.
     * 
     * @param combatants the targets to select from
     * @return the chosen combatant
     */
    private Combatant selectTarget(List<Combatant> combatants) {
    	for (Combatant c : combatants) {
    		if (c._type != _type) {
    			return c;
    		}
    	}
		return this; //Attack self seems reasonable default...
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
    	
    	damage += getStrengthDamageBonus();
    	
    	switch(_attack)
    	{
    	case CUSTOM:
    		break;
    	case NORMAL:
    		_attackRoll = _metadie.getRandom(1,20);
    		break;
    	}
    	
    	return new Attack(_attackRoll, damage);
    }

    private final int MIN_STRENGTH_DAMAGE_BONUS = 16;
    private final int MAX_STRENGTH_PENALTY = 5;
    private int getStrengthDamageBonus() {
    	int bonus = 0;
    	if (_strength >= MIN_STRENGTH_DAMAGE_BONUS) {
    		bonus = (_strength - (MIN_STRENGTH_DAMAGE_BONUS - 2)) / 2;
    	} else if (_strength <= MAX_STRENGTH_PENALTY) {
    		bonus = (_strength - (MAX_STRENGTH_PENALTY + 2)) / 2;
    	}
		return bonus;
	}

	/**
     * Causes damage to an opponent.  
     * 
     * @param attack what the attacker rolled
     * @return the number of HP damage done by the attack
     */
    private int attacked(Attack attack)
    {
        if ((attack.hitRoll() >= _ac && attack.hitRoll() > 1) || attack.hitRoll() == 20) {
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

    /**
     * Check the status of HP.
     * 
     * @return whether combatant has max HP
     */
    public boolean hasFullHP()
    {
        return (_hp == 10);
    }


    /**
     * Write to the console the HP left for the combatant.
     */
    public void displayHP()
    {
        if (_type == CombatantType.HERO) {
            System.out.println("You have " + _hp + " HP left.");
        } else {
            System.out.println("The enemy has " + _hp + " HP left.");
        }        
    }



    /**
     * Write to the console when victory is complete
     */
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

	/**
	 * At the start of battle, or when surprised, determine order in battle
	 * @return the number of initiative roll
	 */
	public int rollInitiative() {
		return _initiative;
	}


	/**
	 * The combatant will attack the victim
	 * @param victim the Combatant to be attacked
	 * @return the damage done by the attack
	 */
	public int attack(Combatant victim) {
		Attack attack = makeAttackAndDamageRoll();
		if (attack.hitRoll() > 1) {
			return victim.attacked(attack);
		} else {
			unequip(_weapon);
			return 0;
		}
	}

	/**
	 * The combatant adds a new piece of armor
	 * @param armor the armor to be worn
	 */
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

	/**
	 * The combatant removes a piece of armor
	 * @param armor the armor to be removed
	 */
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
	

	/**
	 * The combatant unequips a new weapon
	 * @param weapon the weapon to be unequipped
	 */
	public boolean unequip(CombatantWeapon weapon) {
		if (_weapon == weapon) {
			_weapon = CombatantWeapon.ONE_DAMAGE_WEAPON;
			return true;
		}			
		return false;
	}
	
	/**
	 * The combatant equips a new weapon
	 * @param weapon the weapon to be used
	 */
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
	    		break;
	    	}
	    	return true;
		}
		return false;
	}

	private static List<Combatant> findAllCombatantsByType(List<Combatant> combatants, CombatantType type) {
		List<Combatant> result = new ArrayList<Combatant>();
		for (Combatant c : combatants) {
			if (c._type == type) {
				result.add(c);
			}
		}
		return result;
	}
	
	public static List<Combatant> findAllEnemies(List<Combatant> combatants) {
		return findAllCombatantsByType(combatants, CombatantType.ENEMY);
	}
	
	public static List<Combatant> findAllHeros(List<Combatant> combatants) {
		return findAllCombatantsByType(combatants, CombatantType.HERO);
	}
}
