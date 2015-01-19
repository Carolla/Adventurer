package test.battle;

import battle.BattleAction;
import battle.BattleAction.BattleActionType;
import battle.Combatant;

public class AutoCombatant implements Combatant {

    private BattleAction _attackAction;

	private int _hp = 10;
    private int _turnCount = 0;
    private int _ac = 10;
    private CombatantType _type;

    public enum CombatantType {HERO, ENEMY};

	/**
	 */
	public AutoCombatant(CombatantType type)
	{
	    _type = type;
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
        return _hp == 0;
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
            _hp--;
            displayHit(1);
            return 1;
        }
        return 0;
    }

    private void displayHit(int damage)
    {
        if (_type == CombatantType.HERO) {
            System.out.print("The enemy hit you for " + damage + " damage.  ");
        }
        else {
            System.out.print("You hit the enemy for " + damage + " damage.  ");
        }
    }

    @Override
    public boolean hasFullHP()
    {
        return (_hp == 10);
    }

    @Override
    public void displayHP()
    {
        if (_type == CombatantType.HERO) {
            System.out.println("You have " + _hp + " HP left.");
        } else {
            System.out.println("The enemy has " + _hp + " HP left.");
        }        
    }
}
