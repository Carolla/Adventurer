package test.battle;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import mylib.MsgCtrl;
import mylib.pdc.MetaDie;

import org.junit.Before;
import org.junit.Test;

import battle.Battle;
import battle.Combatant;
import battle.Combatant.CombatantType;
import battle.Combatant.CombatantWeapon;

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
		Combatant player = new AutoCombatant.CombatantBuilder().build();
		Combatant enemy = new AutoCombatant.CombatantBuilder().withType(CombatantType.ENEMY).build();
		Battle battle = new Battle(player, enemy);
		assertEquals(0, ((AutoCombatant) player).getTurnCount());
		assertEquals(0, ((AutoCombatant) enemy).getTurnCount());
		battle.advance();
		assertEquals(1, ((AutoCombatant) player).getTurnCount());
		assertEquals(1, ((AutoCombatant) enemy).getTurnCount());
	}
	
	@Test
	public void BattleEndWhenOneCombatantEscapes()
	{
        Combatant player = new AutoCombatant.CombatantBuilder().shouldTryEscaping().withHP(2).build();
        Combatant enemy = new AutoCombatant.CombatantBuilder().withType(CombatantType.ENEMY).withSpecificHit(20).withWeapon(CombatantWeapon.FIST).withHP(9).build();
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
        Combatant player = new AutoCombatant.CombatantBuilder().withSpecificHit(20).withWeapon(CombatantWeapon.FIST).withHP(10).build();
        Combatant enemy = new AutoCombatant.CombatantBuilder().withType(CombatantType.ENEMY).withSpecificHit(20).withWeapon(CombatantWeapon.FIST).withHP(9).build();
        Battle battle = new Battle(player, enemy);
        while (battle.isOngoing()) {
            battle.advance();
        }
        assertFalse(player.isDefeated());
        assertTrue(enemy.isDefeated());
	}
	
    @Test
    public void CombatantOrderIsDeterminedByInitiativeRoll()
    {
    	boolean heroGoesFirst = false;
    	//Flip a coin
    	if (new MetaDie(System.currentTimeMillis()).getRandom(1, 2) == 1)
    	{
    		heroGoesFirst = true;
    	}
    	
    	Combatant player = new AutoCombatant.CombatantBuilder().withSpecificHit(20).withWeapon(CombatantWeapon.FIST).withHP(1).build();
    	Combatant enemy = new AutoCombatant.CombatantBuilder().withInitiative(11).withType(CombatantType.ENEMY).withSpecificHit(20).withWeapon(CombatantWeapon.FIST).withHP(1).build();
    	
    	if (heroGoesFirst)
    	{
    		player = new AutoCombatant.CombatantBuilder().withInitiative(11).withSpecificHit(20).withWeapon(CombatantWeapon.FIST).withHP(1).build();
    		enemy = new AutoCombatant.CombatantBuilder().withType(CombatantType.ENEMY).withSpecificHit(20).withWeapon(CombatantWeapon.FIST).withHP(1).build();
    	} 
    	
        Battle battle = new Battle(player, enemy);
        while (battle.isOngoing()) {
            battle.advance();
        }
        
        if (heroGoesFirst)
        {
	        assertFalse(player.isDefeated());
	        assertTrue(enemy.isDefeated());
        } else {
        	assertTrue(player.isDefeated());
        	assertFalse(enemy.isDefeated());
        }
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
