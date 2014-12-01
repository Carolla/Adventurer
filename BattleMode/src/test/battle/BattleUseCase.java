package test.battle;

import static org.junit.Assert.*;

import org.junit.Test;

import battle.Battle;
import battle.Combatant;
import battle.Enemy;
import battle.Player;

public class BattleUseCase {

	@Test
	public void ThePlayerWantsToDoCombatWithTheEnemy()
	{
		Combatant player1 = new Player();
		Combatant enemy = new Enemy();
		Battle battle = new Battle(player1, enemy);
		assertTrue(battle.isInBattle(player1));
		assertTrue(battle.isInBattle(enemy));
	}
	
	@Test
	public void ThePlayerWantsToDefeatTheEnemyInBattle()
	{
		Combatant player1 = new AutoPlayer();
		Combatant enemy = new AutoEnemy();
		Battle battle = new Battle(player1, enemy);
		while (battle.isOngoing())
		{
			battle.advance();
		}
	}
}
