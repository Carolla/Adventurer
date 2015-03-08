package battle;

public class Battle {
	private Combatant _firstCombatant;
	private Combatant _secondCombatant;
    private Combatant _escapedCombatant = null;

	private int _round = 0;

	public Battle(Combatant combatant1, Combatant combatant2) {
		rollForInitiative(combatant1, combatant2);
	}

	private void rollForInitiative(Combatant combatant1, Combatant combatant2) {
		if (combatant1.rollInitiative() > combatant2.rollInitiative())
		{
			_firstCombatant = combatant1;
			_secondCombatant = combatant2;
		} else {
			_firstCombatant = combatant2;
			_secondCombatant = combatant1;
		}
	}

	/**
	 * Check if a battle is currently taking place.
	 * @param enemy 
	 * 
	 * @return whether the combatant is in the battle
	 */
	public boolean isInBattle(Combatant combatant) {
		return (_firstCombatant == combatant || _secondCombatant == combatant);
	}

	public boolean isOngoing() {
		return (!_firstCombatant.isDefeated() && !_secondCombatant.isDefeated() && _escapedCombatant == null);
	}

	/**
	 * Complete a single "turn" of combat
	 */
	public void advance() {
		System.out.println("Round " + _round++);
		_firstCombatant.takeTurn(_secondCombatant, this);

		if (isOngoing()) {
			_secondCombatant.displayHP();
			_secondCombatant.takeTurn(_firstCombatant, this);
			_firstCombatant.displayHP();
		} else {
			if (isWinner(_firstCombatant)) {
				_firstCombatant.displayVictory();
			}
			return;
		}
		
		if (isWinner(_secondCombatant))
		{
			_secondCombatant.displayVictory();
		}
	}

	public boolean isWinner(Combatant combatant) {
		if (!isOngoing()) {
			return !combatant.isDefeated();
		} else {
			return false;
		}
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
