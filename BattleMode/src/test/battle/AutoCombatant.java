package test.battle;

import battle.Combatant;

public class AutoCombatant implements Combatant {

	private boolean _automaticHit;
	private int _hp = 1;

	/**
	 * 
	 * 
	 * @param automaticHit whether to hit or miss automatically
	 */
	public AutoCombatant(boolean automaticHit)
	{
		_automaticHit = automaticHit;
	}

	@Override
	public boolean isDefeated() {
		return _hp  > 0;
	}
}
