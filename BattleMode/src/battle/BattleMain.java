package battle;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import test.battle.AutoCombatant;
import battle.CombatantInterface.CombatantType;

public class BattleMain {

	private static final Scanner in = new Scanner(System.in);
	
	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		int numEnemies = getNumberOfEnemies();
		System.out.println("You enter combat against " + numEnemies + " foes...");
		Combatant hero = new Combatant(new ManualTargetStrategy());
		List<CombatantInterface> combatants = new ArrayList<CombatantInterface>(numEnemies + 1);
		while (numEnemies-- > 0) {
			Combatant enemy = new AutoCombatant.CombatantBuilder().withType(CombatantType.ENEMY).build();
			combatants.add(enemy);
		}
		combatants.add(hero);
		
		Battle battle = new Battle(combatants.toArray(new CombatantInterface[combatants.size()]));
		while (battle.isOngoing()) {
			battle.advance();
		}
		
		if (battle.isWinner(hero)) {
			System.out.println("You won!");
		} else {
			System.out.println("You lost...");
		}
		System.exit(0);
	}
	
	private static int getNumberOfEnemies() {
		System.out.print("How many enemies would you like to fight? ");
		if (in.hasNextInt()) {
			return in.nextInt();
		}
		return getNumberOfEnemies();
	}
	
	private static class ManualTargetStrategy implements TargetStrategy
	{
		@Override
		public CombatantInterface selectTarget(List<CombatantInterface> combatants) {
			CombatantInterface target = null;
			List<CombatantInterface> enemies = Combatant.findAllEnemies(combatants);
			if (enemies.size() == 0) {
				return null;
			}
			
			while (target == null) {
				int i = 1;
				for (CombatantInterface c : enemies) {
					System.out.println("Monster " + i + ": " + c.name());
					i++;
				}
				System.out.print("Select a target: ");
	
				if (in.hasNextInt()) {
					int position = in.nextInt() - 1;
					if (position < enemies.size()) {
						target = enemies.get(position);
					} else {
						System.out.println("Invalid target.\n");
					}
				}
			}
			return target;
		}
	}
}
