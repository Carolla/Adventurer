package battle;

public class Battle {

	private Combatant _player;
	private Combatant _enemy;
	private int _round = 0;
    private Combatant _escapedCombatant = null;

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
		return (!_player.isDefeated() && !_enemy.isDefeated() && _escapedCombatant == null);
	}

	/**
	 * Complete a single "turn" of combat
	 */
	public void advance() {
		System.out.println("Round " + _round++);
		_player.takeTurn(_enemy, this);

		if (isOngoing()) {
		    _enemy.displayHP();
		    _enemy.takeTurn(_player, this);
		    _player.displayHP();
		} else {
			if (isWinner(_player)) {
				displayHeroVictory();
			}
			return;
		}
		
		if (!isOngoing())
		{
			displayEnemyVictory();
		}
	}

	private void displayHeroVictory() {
		System.out.println("You have won!");
	}

	private void displayEnemyVictory() {
		System.out.println("The enemy has won...");
	}

	public boolean isWinner(Combatant combatant) {
		return !combatant.isDefeated();
	}
	
	public boolean escape(Combatant combatant) {
	    _escapedCombatant = combatant;
	    return true;
	}

    public boolean combatantEscaped(Combatant combatant)
    {
        return _escapedCombatant == combatant;
    }

}
