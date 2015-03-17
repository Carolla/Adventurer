package battle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import mylib.MsgCtrl;

public class Battle {

	private int _round = 0;
	private List<Combatant> _combatants;
	private List<Combatant> _heros;
	private List<Combatant> _enemies;
	private List<Combatant> _firstGroup;
	private List<Combatant> _secondGroup;
    private List<Combatant> _escapedCombatants = new ArrayList<Combatant>();

	public Battle(Combatant... combatantList) {
		if (combatantList.length < 1) {
			MsgCtrl.msgln("Unable to start Battle with less than 2 Combatants");
		}
		
		_combatants = new ArrayList<Combatant>(Arrays.asList(combatantList));
		_heros = Combatant.findAllHeros(_combatants);
		_enemies = Combatant.findAllEnemies(_combatants);
		
		Combatant highestRoller = rollForInitiative(_combatants);
		if (_heros.contains(highestRoller)) {
			_firstGroup = _heros;
			_secondGroup = _enemies;
		} else {
			_secondGroup = _heros;
			_firstGroup = _enemies;
		}
	}


	private Combatant rollForInitiative(List<Combatant> combatants) {
		int highestRoll = 1;
		Combatant highestRoller = combatants.get(0);
		for (Combatant c : combatants) {
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
	public boolean isInBattle(Combatant combatant) {
		return _combatants.contains(combatant);
	}

	public boolean isOngoing() {
		if (_escapedCombatants.isEmpty()) {
			if (allEnemiesDefeated() || allHerosDefeated()) {
				return false;
			}
		} else {
			if (_escapedCombatants.containsAll(_heros) || _escapedCombatants.containsAll(_enemies)) {
				return false;
			}
		}
		return true;
	}

	private boolean allOfGroupDefeated(List<Combatant> group) {
		for (Combatant c : group) {
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
		Combatant lastCombatant = _firstGroup.get(0);
		for (Combatant c : _firstGroup) {
			c.takeTurn(_combatants, this);
			lastCombatant = c;
		}

		if (!isOngoing() && isWinner(lastCombatant)) {
			lastCombatant.displayVictory();
			return;
		}
		
		for (Combatant c : _secondGroup) {
			c.takeTurn(_combatants, this);
			lastCombatant = c;
		}
		
		if (!isOngoing() && isWinner(lastCombatant)) {
			lastCombatant.displayVictory();
			return;
		}
	}

	public boolean isWinner(Combatant combatant) {
		if (isOngoing()) {
			return false;
		} else {
			return !combatant.isDefeated();
		}
	}
	
	public boolean escape(Combatant combatant) {
	    return _escapedCombatants.add(combatant);
	}

    public boolean combatantEscaped(Combatant combatant)
    {
        return _escapedCombatants.contains(combatant);
    }

}
