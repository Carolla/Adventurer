package battle;

import test.battle.AutoCombatant;

public class BattleMain {

	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		System.out.println("You enter combat...");
		Combatant hero = new AutoCombatant.CombatantBuilder().build();
		Combatant enemy = new AutoCombatant.CombatantBuilder().build();
		Battle battle = new Battle(hero, enemy);
		while (battle.isOngoing()) {
			battle.advance();
		}
		
		if (battle.isWinner(hero)) {
			System.out.println("You won!");
		} else {
			System.out.println("You lost...");
		}
	}

}
