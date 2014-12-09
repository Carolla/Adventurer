package test.battle;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import battle.Battle;
import battle.Combatant;

public class TestBattle {

	private Combatant _enemy;
	private Combatant _player;
	private Battle _battle;

	@Test
	public void BattleEndsWhenOneCombatantIsDefeated()
	{
		Battle battle = SetupBasicBattle();

		assertFalse(battle.isOngoing());
		assertTrue(battle.isWinner(_player) || battle.isWinner(_enemy));
	}
	
	@Test
	public void WhenBattleEndsThereIsAWinnerAndALoser()
	{
		Battle battle = SetupBasicBattle();

		assertTrue(battle.isWinner(_player) || battle.isWinner(_enemy));
		assertFalse(battle.isWinner(_player) && battle.isWinner(_enemy));
	}
	
	@Test
	public void WhenBattleEndsPlayerWinningMeansEnemyLoses()
	{
		Battle battle = SetupBasicBattle();

		assertTrue(battle.isWinner(_player));
		assertFalse(battle.isWinner(_enemy));
	}

	@Test
	public void WhenBattleEndsEnemyWinningMeansPlayerLoses()
	{
		Combatant player = new DummyCombatant(false);
		Combatant enemy = new DummyCombatant(true);
		Battle battle = new Battle(player, enemy);
		while (battle.isOngoing())
		{
			battle.advance();
		}
		assertTrue(battle.isWinner(enemy));
		assertFalse(battle.isWinner(player));	
	}

	@Test
	public void BattleDoesNotEndWhenNeitherPlayerIsDefeated()
	{
		Combatant player = new DummyCombatant(false);
		Combatant enemy = new DummyCombatant(false);
		Battle battle = new Battle(player, enemy);
		for (int i = 0; i < 10000 && battle.isOngoing(); i++)
		{
			battle.advance();
		}
		assertFalse(battle.isWinner(enemy) || battle.isWinner(player));
	}
	
	private Battle SetupBasicBattle() 
	{
		_player = new DummyCombatant(true);
		_enemy = new DummyCombatant(false);
		_battle = new Battle(_player, _enemy);
		while (_battle.isOngoing())
		{
			_battle.advance();
		}
		return _battle;
	}
}
