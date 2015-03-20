package battle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import mylib.MsgCtrl;

public class Battle {

	private int _round = 0;
	private List<CombatantInterface> _combatants;
	private List<CombatantInterface> _heros;
	private List<CombatantInterface> _enemies;
	private List<CombatantInterface> _firstGroup;
	private List<CombatantInterface> _secondGroup;
    private List<CombatantInterface> _escapedCombatants = new ArrayList<CombatantInterface>();

	public Battle(CombatantInterface... combatantList) {
		if (combatantList.length < 1) {
			MsgCtrl.msgln("Unable to start Battle with less than 2 Combatants");
		}
		
		_combatants = new ArrayList<CombatantInterface>(Arrays.asList(combatantList));
		_heros = Combatant.findAllHeros(_combatants);
		_enemies = Combatant.findAllEnemies(_combatants);
		
		CombatantInterface highestRoller = rollForInitiative(_combatants);
		if (_heros.contains(highestRoller)) {
			_firstGroup = _heros;
			_secondGroup = _enemies;
		} else {
			_secondGroup = _heros;
			_firstGroup = _enemies;
		}
	}


	private CombatantInterface rollForInitiative(List<CombatantInterface> _combatants2) {
		int highestRoll = 1;
		CombatantInterface highestRoller = _combatants2.get(0);
		for (CombatantInterface c : _combatants2) {
			int currentRoll = c.rollInitiative();
			if (currentRoll > highestRoll) {
				highestRoll = currentRoll;
				highestRoller = c;
			}
		}
		return highestRoller;
	}

	/**
	 * Check if a battle is currently taking place.
	 * @param enemy 
	 * 
	 * @return whether the combatant is in the battle
	 */
	public boolean isInBattle(CombatantInterface combatant) {
		return _combatants.contains(combatant);
	}

	public boolean isOngoing() {
		if (_escapedCombatants.isEmpty() && (allEnemiesDefeated() || allHerosDefeated())) {
				return false;
		} else {
			int herosOutOfBattle = 0;
			int enemiesOutOfBattle = 0;
			for (CombatantInterface c : _heros) {
				if (c.isDefeated() || _escapedCombatants.contains(c)) {
					herosOutOfBattle++;
				}
			}
			for (CombatantInterface c : _enemies) {
				if (c.isDefeated() || _escapedCombatants.contains(c)) {
					enemiesOutOfBattle++;
				}
			}

			return (herosOutOfBattle != _heros.size() && enemiesOutOfBattle != _enemies.size());
		}
	}

	private boolean allOfGroupDefeated(List<CombatantInterface> group) {
		for (CombatantInterface c : group) {
			if (!c.isDefeated()) {
				return false;
			}
		}
		return true;
	}

	private boolean allHerosDefeated() {
		return allOfGroupDefeated(_heros);
	}

	private boolean allEnemiesDefeated() {
		return allOfGroupDefeated(_enemies);
	}


	/**
	 * Complete a single "turn" of combat
	 */
	public void advance() {
		System.out.println("Round " + _round++);
		CombatantInterface lastCombatant = _firstGroup.get(0);
		for (CombatantInterface c : _firstGroup) {
			c.takeTurn(_combatants, this);
			lastCombatant = c;
		}

		if (!isOngoing() && isWinner(lastCombatant)) {
			lastCombatant.displayVictory();
			return;
		}
		
		for (CombatantInterface c : _secondGroup) {
			c.takeTurn(_combatants, this);
			lastCombatant = c;
		}
		
		if (!isOngoing() && isWinner(lastCombatant)) {
			lastCombatant.displayVictory();
			return;
		}
	}

	public boolean isWinner(CombatantInterface combatant) {
		if (isOngoing()) {
			return false;
		} else {
			return !combatant.isDefeated();
		}
	}
	
	public boolean escape(Combatant combatant) {
	    return _escapedCombatants.add(combatant);
	}

    public boolean combatantEscaped(CombatantInterface combatant)
    {
        return _escapedCombatants.contains(combatant);
    }

}
