package battle;

public interface Combatant {
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
     * @param _enemy the enemy to strike this round
     * @return the damage done
     */
    int takeTurn(Combatant _enemy);

    /**
     * Causes damage to an opponent.  
     * 
     * @param attack what the attacker rolled
     * @return the number of HP damage done by the attack
     */
    int attack(Attack attack);

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

}
