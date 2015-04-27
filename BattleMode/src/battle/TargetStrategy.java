package battle;

import java.util.List;

public interface TargetStrategy {

	CombatantInterface selectTarget(List<CombatantInterface> combatants);

}
