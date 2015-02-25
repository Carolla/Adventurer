package test.battle;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import mylib.MsgCtrl;

import org.junit.Before;
import org.junit.Test;

import test.battle.AutoCombatant.CombatantType;
import battle.Battle;
import battle.Combatant;

public class TestBattle {

	private Combatant _enemy;
	private Combatant _player;
	private Battle _battle;

    @Before
    public void setup()
    {
        MsgCtrl.auditMsgsOn(true);
    }

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
        MsgCtrl.msgln("BattleDoesNotEndWhenNeitherPlayerIsDefeated()");
		Combatant player = new DummyCombatant(false);
		Combatant enemy = new DummyCombatant(false);
		Battle battle = new Battle(player, enemy);
		for (int i = 0; i < 10000 && battle.isOngoing(); i++)
		{
			battle.advance();
		}
		assertFalse(battle.isWinner(enemy) || battle.isWinner(player));
	}
	
	@Test
	public void BattleAllowsPlayersToTakeSuccessiveTurns()
	{
		AutoCombatant player = new AutoCombatant(CombatantType.HERO);
		AutoCombatant enemy = new AutoCombatant(CombatantType.ENEMY);
		Battle battle = new Battle(player, enemy);
		assertEquals(0, player.getTurnCount());
		assertEquals(0, enemy.getTurnCount());
		battle.advance();
		assertEquals(1, player.getTurnCount());
		assertEquals(1, enemy.getTurnCount());
	}
	
	@Test
	public void BattleEndWhenOneCombatantEscapes()
	{
        Combatant player = new AutoCombatant(CombatantType.FEEBLE_HERO);
        Combatant enemy = new AutoCombatant(CombatantType.WEAK_ENEMY);
        Battle battle = new Battle(player, enemy);
        assertFalse(battle.combatantEscaped(player));
        while (battle.isOngoing()) {
            battle.advance();
        }
        assertTrue(battle.combatantEscaped(player));
	}
	
	@Test
	public void CombatantsCanStartWithDifferentHP()
	{
        Combatant player = new AutoCombatant.CombatantBuilder(CombatantType.AUTO_HIT).withHP(10).build();
        Combatant enemy = new AutoCombatant.CombatantBuilder(CombatantType.AUTO_HIT).withHP(9).build();
        Battle battle = new Battle(player, enemy);
        while (battle.isOngoing()) {
            battle.advance();
        }
        assertFalse(player.isDefeated());
        assertTrue(enemy.isDefeated());
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
