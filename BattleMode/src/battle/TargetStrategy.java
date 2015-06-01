package battle;

import java.util.List;

public interface TargetStrategy {

	/**
	 * Select a target from a list of valid targets
	 * 
	 * @param combatants The list of valid targets
	 * @return one of the targets
	 */
	CombatantInterface selectTarget(List<CombatantInterface> combatants);

}
