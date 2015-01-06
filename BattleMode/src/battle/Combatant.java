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
     * @return 
     */
    BattleAction takeTurn();

    /**
     * Causes damage to an opponent.  
     * 
     * @param attackRoll what the attacker rolled
     * @return the number of HP damage done by the attack
     */
    int attack(int attackRoll);

}
