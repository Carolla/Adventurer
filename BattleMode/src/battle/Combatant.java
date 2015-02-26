package battle;

public interface Combatant {
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
	public void displayVictory();

	/**
	 * At the start of battle, or when surprised, determine order in battle
	 * @return the number of initiative roll
	 */
	public int rollInitiative();


}
