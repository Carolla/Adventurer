package test.battle;

import battle.Attack;
import battle.Battle;
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
    public int takeTurn(Combatant opponent, Battle battle)
    {
        return 0;
    }

    @Override
    public int attacked(Attack attack)
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

	@Override
	public void displayVictory() {}

	@Override
	public int rollInitiative() {
		return 0;
	}

	@Override
	public int attack(Combatant victim) {
		return 0;
	}

	@Override
	public void equip(CombatantArmor armor) { }

	@Override
	public void unequip(CombatantArmor armor) { }

	@Override
	public void equip(CombatantWeapon weapon) {	}
	
	@Override
	public void unequip(CombatantWeapon weapon) {	}
}	
