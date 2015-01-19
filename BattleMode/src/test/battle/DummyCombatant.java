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

    @Override
    public boolean isUnconscious()
    {
        return false;
    }

    @Override
    public int takeTurn(Combatant opponent)
    {
        return 0;
    }

    @Override
    public int attack(int attackRoll)
    {
        return 0;
    }

    @Override
    public boolean hasFullHP()
    {
        return true;
    }

    @Override
    public void displayHP() { }
}	
