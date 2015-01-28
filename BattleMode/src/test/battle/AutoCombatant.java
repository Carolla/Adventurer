package test.battle;

import mylib.pdc.MetaDie;
import battle.Attack;
import battle.Battle;
import battle.Combatant;

public class AutoCombatant implements Combatant {

	private int _hp = 10;
    private int _turnCount = 0;
    private int _ac = 10;
    private CombatantType _type;
    private MetaDie _metadie;
    private boolean _escapedFromBattle;

    public enum CombatantType {HERO, ENEMY, STRONG_ENEMY, WEAK_ENEMY, IMMOBILE_HERO};

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
		return isUnconscious() || escapedFromBattle();
	}

    private boolean escapedFromBattle()
    {
        return _escapedFromBattle;
    }

    @Override
    public boolean isUnconscious()
    {
        return _hp <= 0;
    }

    @Override
    public int takeTurn(Combatant opponent, Battle battle)
    {
        _turnCount++;
        if (shouldAttack())
        {
            return opponent.attack(makeAttackRoll());
        } else {
            tryToEscape(battle);
            return 0;
        }
    }

    private void tryToEscape(Battle battle)
    {
        if (_type == CombatantType.HERO || _type == CombatantType.IMMOBILE_HERO)
        {
            System.out.print("You try to escape.  ");
            if (battle.escape(this)) {
                System.out.println("You escaped!");
                _escapedFromBattle = true;
            }
        } else {
            System.out.println("Enemies do not flee in battle.");
        }
            
    }

    private boolean shouldAttack()
    {
        if (_type == CombatantType.HERO || _type == CombatantType.IMMOBILE_HERO)
        {
            return _hp > 1;
        } else {
            return true;
        }
    }

    private Attack makeAttackRoll()
    {
        if (_type == CombatantType.HERO) {
            return new Attack(_metadie.getRandom(8, 14), _metadie.getRandom(1,2));
        } else if (_type == CombatantType.IMMOBILE_HERO) {
            return new Attack(0,0);
        } else if (_type == CombatantType.STRONG_ENEMY) {
            return new Attack(_metadie.getRandom(10, 12), 2);
        } else if (_type == CombatantType.WEAK_ENEMY) {
            return new Attack(_metadie.getRandom(4, 11), 1);
        } else {
            return new Attack(_metadie.getRandom(6, 12), _metadie.getRandom(1,3));
        }
    }

    @Override
    public int attack(Attack attack) //AttackRoll attackRoll, DamageRoll damageRoll)
    {
        if (attack.hitRoll() > _ac) {
            int damage = attack.damageRoll();
            _hp = _hp - damage;
            if (damage > 0) {
                displayHit(damage);
            }
            return damage;
        } else {
            displayMiss();
        }
        return 0;
    }

    private void displayMiss()
    {
        if (_type == CombatantType.HERO || _type == CombatantType.IMMOBILE_HERO) {
            System.out.print("The enemy missed.  ");
        }
        else {
            System.out.print("You missed.  ");
        }
    }

    private void displayHit(int damage)
    {
        if (_type == CombatantType.HERO || _type == CombatantType.IMMOBILE_HERO) {
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
        if (_type == CombatantType.HERO || _type == CombatantType.IMMOBILE_HERO) {
            System.out.println("You have " + _hp + " HP left.");
        } else {
            System.out.println("The enemy has " + _hp + " HP left.");
        }        
    }
}
