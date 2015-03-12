package battle;


public interface Combatant {

    public enum CombatantType {HERO, ENEMY};
    public enum CombatantAttack {CUSTOM, NORMAL};
    public enum CombatantWeapon {FIST, DAGGER, MORNING_STAR};
    public enum CombatantArmor {HELMET, SHIELD, CHAIN_MAIL};
    
    /**
     * Status of player in battle - defeated player is out of battle
     * 
     * @return whether player is participating in battle anymore
     */
	boolean isDefeated();

	/**
	 * Whether player is able to take actions in the battle
	 * 
	 * @return players consciousness
	 */
    boolean isUnconscious();

    /**
     * Each combtant executes actions during a battle, turn by turn.  Takes
     * the next turn for a combatant.
     * @param opponent the enemy to strike this round
     * @param battle the current battle
     * @return the damage done
     */
    int takeTurn(Combatant opponent, Battle battle);

    /**
     * Causes damage to an opponent.  
     * 
     * @param attack what the attacker rolled
     * @return the number of HP damage done by the attack
     */
    int attacked(Attack attack);

    /**
     * Check the status of HP.
     * 
     * @return whether combatant has max HP
     */
    boolean hasFullHP();

    /**
     * Write to the console the HP left for the combatant.
     */
    void displayHP();

    /**
     * Write to the console when victory is complete
     */
	void displayVictory();

	/**
	 * At the start of battle, or when surprised, determine order in battle
	 * @return the number of initiative roll
	 */
	int rollInitiative();

	/**
	 * The combatant will attack the victim
	 * @param victim the Combatant to be attacked
	 * @return the damage done by the attack
	 */
	int attack(Combatant victim);

	/**
	 * The combatant adds a new piece of armor
	 * @param armor the armor to be worn
	 */
	void equip(CombatantArmor armor);

	/**
	 * The combatant equips a new weapon
	 * @param weapon the weapon to be used
	 */
	void equip(CombatantWeapon weapon);

	/**
	 * The combatant removes a piece of armor
	 * @param armor the armor to be removed
	 */
	void unequip(CombatantArmor shield);

	/**
	 * The combatant unequips a new weapon
	 * @param weapon the weapon to be unequipped
	 */
	void unequip(CombatantWeapon weapon);


}
