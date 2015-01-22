package test.battle;

import mylib.pdc.MetaDie;
import battle.Combatant;

public class AutoCombatant implements Combatant {

	private int _hp = 10;
    private int _turnCount = 0;
    private int _ac = 10;
    private CombatantType _type;
    private MetaDie _metadie;

    public enum CombatantType {HERO, ENEMY};

	/**
	 */
	public AutoCombatant(CombatantType type)
	{
	    _type = type;
	    _metadie = new MetaDie();
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
    public int takeTurn(Combatant opponent)
    {
        _turnCount++;
        return opponent.attack(makeAttackRoll());
    }

    private int makeAttackRoll()
    {
        if (_type == CombatantType.HERO) {
            return _metadie.getRandom(8, 14);
        } else {
            return _metadie.getRandom(6, 12);
        }
    }

    @Override
    public int attack(int attackRoll)
    {
        if (attackRoll > _ac) {
            _hp--;
            displayHit(1);
            return 1;
        } else {
            displayMiss();
        }
        return 0;
    }

    private void displayMiss()
    {
        if (_type == CombatantType.HERO) {
            System.out.print("The enemy missed.  ");
        }
        else {
            System.out.print("You missed.  ");
        }
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
