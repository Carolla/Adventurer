package battle;

public class Battle {

	private Combatant _player;
	private Combatant _enemy;
	private int _round;

	public Battle(Combatant player1, Combatant enemy) {
		_player = player1;
		_enemy = enemy;
	}

	/**
	 * Check if a battle is currently taking place.
	 * @param enemy 
	 * 
	 * @return whether the combatant is in the battle
	 */
	public boolean isInBattle(Combatant combatant) {
		return (_player == combatant || _enemy == combatant);

	}

	public boolean isOngoing() {
		return !(_player.isDefeated() || _enemy.isDefeated());
	}

	/**
	 * Complete a single "turn" of combat
	 */
	public void advance() {
		System.out.println("Round " + _round++);
	}

	public boolean isWinner(Combatant combatant) {
		return true;
	}

}
