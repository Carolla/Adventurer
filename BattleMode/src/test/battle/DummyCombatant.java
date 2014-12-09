package test.battle;

import battle.Combatant;

/**
 * Combatant that wins or loses based on boolean determination
 */
public class DummyCombatant implements Combatant {

	private boolean _automaticWin;

	public DummyCombatant(boolean automaticWin)
	{
		_automaticWin = automaticWin;
	}

	@Override
	public boolean isDefeated() {
		return !_automaticWin;
	}
}	
