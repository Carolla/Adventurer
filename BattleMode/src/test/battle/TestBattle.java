package test.battle;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import battle.Battle;
import battle.Combatant;

public class TestBattle {
	/**
	 * Player that automatically wins combat
	 */
	public class DummyPlayer implements Combatant {
		@Override
		public boolean isDefeated() {
			return false;
		}
	}

	/**
	 * Enemy that automatically loses combat
	 */
	public class DummyEnemy implements Combatant {
		@Override
		public boolean isDefeated() {
			return true;
		}
	}

	@Test
	public void BattleEndsWhenOneCombatantIsDefeated()
	{
		Combatant player = new DummyPlayer();
		Combatant enemy = new DummyEnemy();
		Battle battle = new Battle(player, enemy);
		while (battle.isOngoing())
		{
			battle.advance();
		}
		assertTrue(battle.isWinner(player));
		assertFalse(battle.isWinner(enemy));
	}
}
