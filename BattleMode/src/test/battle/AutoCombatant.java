package test.battle;

import battle.BattleAction;
import battle.BattleAction.BattleActionType;
import battle.Combatant;

public class AutoCombatant implements Combatant {

    private BattleAction _attackAction;

	private int _hp = 1;
    private int _turnCount = 0;
    private int _ac = 10;

	/**
	 */
	public AutoCombatant()
	{
		_attackAction = new BattleAction(BattleActionType.HIT);
	}

	public AutoCombatant(BattleAction action)
    {
	    _attackAction = action;
    }

    int getTurnCount()
    {
       return _turnCount;
    }

    @Override
	public boolean isDefeated() {
		return isUnconscious();
	}

    @Override
    public boolean isUnconscious()
    {
        return _hp > 0;
    }

    @Override
    public BattleAction takeTurn()
    {
        _turnCount++;
        return _attackAction;
    }

    @Override
    public int attack(int attackRoll)
    {
        if (attackRoll > _ac) {
            return 1;
        }
        return 0;
    }
}
