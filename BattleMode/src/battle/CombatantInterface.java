package battle;

import java.util.List;

public interface CombatantInterface {

    public enum CombatantType {HERO, ENEMY};
    public enum CombatantAttack {CUSTOM, NORMAL};
    public enum CombatantWeapon {ONE_DAMAGE_WEAPON, DAGGER, MORNING_STAR};
    public enum CombatantArmor {HELMET, SHIELD, CHAIN_MAIL};
    
	/**
	 * Status of player in battle - defeated player is out of battle
	 * 
	 * @return whether player is participating in battle anymore
	 */
	public abstract boolean isDefeated();

	/**
	 * Whether player is able to take actions in the battle
	 * 
	 * @return players consciousness
	 */
	public abstract boolean isUnconscious();

	/**
	 * Each combtant executes actions during a battle, turn by turn.  Takes
	 * the next turn for a combatant.
	 * @param selectTarget(_combatants the enemy to strike this round
	 * @param battle the current battle
	 * @return the damage done
	 */
	public abstract int takeTurn(List<CombatantInterface> _combatants, Battle battle);

	/**
	 * Check the status of HP.
	 * 
	 * @return whether combatant has max HP
	 */
	public abstract boolean hasFullHP();

	/**
	 * Write to the console the HP left for the combatant.
	 */
	public abstract void displayHP();

	/**
	 * Write to the console when victory is complete
	 */
	public abstract void displayVictory();

	/**
	 * At the start of battle, or when surprised, determine order in battle
	 * @return the number of initiative roll
	 */
	public abstract int rollInitiative();

	/**
	 * The combatant will attack the victim
	 * @param victim the Combatant to be attacked
	 * @return the damage done by the attack
	 */
	public abstract int attack(CombatantInterface victim);

	/**
	 * The combatant adds a new piece of armor
	 * @param armor the armor to be worn
	 */
	public abstract boolean equip(CombatantArmor armor);

	/**
	 * The combatant removes a piece of armor
	 * @param armor the armor to be removed
	 */
	public abstract boolean unequip(CombatantArmor armor);

	/**
	 * The combatant unequips a new weapon
	 * @param weapon the weapon to be unequipped
	 */
	public abstract boolean unequip(CombatantWeapon weapon);

	/**
	 * The combatant equips a new weapon
	 * @param weapon the weapon to be used
	 */
	public abstract boolean equip(CombatantWeapon weapon);
	
    /**
     * Target selection method.  Combatant chooses from the list of available
     * Combatants.
     * 
     * @param combatants the targets to select from
     * @return the chosen combatant
     */
	public abstract CombatantInterface selectTarget(List<CombatantInterface> combatants);
	
	/**
     * Causes damage to an opponent.  
     * 
     * @param attack what the attacker rolled
     * @return the number of HP damage done by the attack
     */
	public abstract int attacked(Attack attack);

	/**
	 * Check if the type of the Combatant matches
	 * 
	 * @param type the type to compare
	 * @return true if types match
	 */
	public abstract boolean isType(CombatantType type);

}