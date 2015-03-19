package test.battle;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import mylib.MsgCtrl;
import mylib.pdc.MetaDie;

import org.junit.Before;
import org.junit.Test;

import battle.Battle;
import battle.Combatant;
import battle.CombatantInterface;
import battle.CombatantInterface.CombatantType;
import battle.CombatantInterface.CombatantWeapon;

public class TestBattle {

	private CombatantInterface _enemy;
	private Combatant _player;
	private Battle _battle;
	private static final MetaDie _metadie = new MetaDie(System.currentTimeMillis());
	
    @Before
    public void setup()
    {
        MsgCtrl.auditMsgsOn(true);
    }

	@Test
	public void BattleEndsWhenOneCombatantIsDefeated()
	{
        MsgCtrl.msgln("BattleEndsWhenOneCombatantIsDefeated()");
		_player = new AutoCombatant.CombatantBuilder().build();
		_enemy = new AutoCombatant.CombatantBuilder().withType(CombatantType.ENEMY).build();
		_battle = new Battle(_player, _enemy);
		while (_battle.isOngoing())
		{
			_battle.advance();
		}
		
		assertFalse(_battle.isOngoing());
		assertTrue(_battle.isWinner(_player) || _battle.isWinner(_enemy));
	}
	
	@Test
	public void WhenBattleEndsThereIsAWinnerAndALoser()
	{
        MsgCtrl.msgln("WhenBattleEndsThereIsAWinnerAndALoser()");
		_player = new AutoCombatant.CombatantBuilder().build();
		_enemy = new AutoCombatant.CombatantBuilder().withType(CombatantType.ENEMY).build();
		_battle = new Battle(_player, _enemy);
		while (_battle.isOngoing())
		{
			_battle.advance();
		}
		
		assertTrue(_battle.isWinner(_player) || _battle.isWinner(_enemy));
		assertFalse(_battle.isWinner(_player) && _battle.isWinner(_enemy));
	}
	
	@Test
	public void WhenBattleEndsPlayerWinningMeansEnemyLoses()
	{
        MsgCtrl.msgln("WhenBattleEndsPlayerWinningMeansEnemyLoses()");
		_player = new AutoCombatant.CombatantBuilder().withAC(20).build();
		_enemy = new AutoCombatant.CombatantBuilder().withType(CombatantType.ENEMY).build();
		_battle = new Battle(_player, _enemy);
		while (_battle.isOngoing())
		{
			_battle.advance();
		}
		
		assertTrue(_battle.isWinner(_player));
		assertFalse(_battle.isWinner(_enemy));
	}

	@Test
	public void WhenBattleEndsEnemyWinningMeansPlayerLoses()
	{
        MsgCtrl.msgln("WhenBattleEndsEnemyWinningMeansPlayerLoses()");
		_player = new AutoCombatant.CombatantBuilder().build();
		_enemy = new AutoCombatant.CombatantBuilder().withType(CombatantType.ENEMY).withAC(20).build();
		Battle battle = new Battle(_player, _enemy);
		while (battle.isOngoing())
		{
			battle.advance();
		}
		assertTrue(battle.isWinner(_enemy));
		assertFalse(battle.isWinner(_player));	
	}

	@Test
	public void BattleDoesNotEndWhenNeitherPlayerIsDefeated()
	{
        MsgCtrl.msgln("BattleDoesNotEndWhenNeitherPlayerIsDefeated()");
        Combatant player = new AutoCombatant.CombatantBuilder().withSpecificHit(0).build();
        CombatantInterface enemy = new AutoCombatant.CombatantBuilder().withType(CombatantType.ENEMY).withSpecificHit(0).build();
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
        MsgCtrl.msgln("BattleAllowsPlayersToTakeSuccessiveTurns()");
		Combatant player = new AutoCombatant.CombatantBuilder().build();
		CombatantInterface enemy = new AutoCombatant.CombatantBuilder().withType(CombatantType.ENEMY).build();
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
        MsgCtrl.msgln("BattleEndWhenOneCombatantEscapes()");
        Combatant player = new AutoCombatant.CombatantBuilder().shouldTryEscaping().withHP(2).build();
        CombatantInterface enemy = new AutoCombatant.CombatantBuilder().withType(CombatantType.ENEMY).withSpecificHit(20).withWeapon(CombatantWeapon.ONE_DAMAGE_WEAPON).withHP(9).build();
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
		for (int round = 0; round < 15; round++) {
            MsgCtrl.msgln("CombatantOrderIsDeterminedByInitiativeRoll() round " + round);
			int startingHp = _metadie.getRandom(5, 25);
	        Combatant player = new AutoCombatant.CombatantBuilder().withSpecificHit(20).withWeapon(CombatantWeapon.ONE_DAMAGE_WEAPON).withHP(startingHp).build();
	        CombatantInterface enemy = new AutoCombatant.CombatantBuilder().withType(CombatantType.ENEMY).withSpecificHit(20).withWeapon(CombatantWeapon.ONE_DAMAGE_WEAPON).withHP(startingHp - 1).build();
	        Battle battle = new Battle(player, enemy);
	        while (battle.isOngoing()) {
	            battle.advance();
	        }
	        assertFalse(player.isDefeated());
	        assertTrue(enemy.isDefeated());
		}
	}
	
    @Test
    public void CombatantOrderIsDeterminedByInitiativeRoll()
    {
        for (int round = 0; round < 100; round++) {
            MsgCtrl.msgln("CombatantOrderIsDeterminedByInitiativeRoll() round " + round);
	    	boolean heroGoesFirst = false;
	    	//Flip a coin
	    	if (new MetaDie(System.currentTimeMillis()).getRandom(1, 2) == 1)
	    	{
	    		heroGoesFirst = true;
	    	}
	    	
	    	Combatant player = new AutoCombatant.CombatantBuilder().withSpecificHit(20).withWeapon(CombatantWeapon.ONE_DAMAGE_WEAPON).withHP(1).build();
	    	CombatantInterface enemy = new AutoCombatant.CombatantBuilder().withInitiative(11).withType(CombatantType.ENEMY).withSpecificHit(20).withWeapon(CombatantWeapon.ONE_DAMAGE_WEAPON).withHP(1).build();
	    	
	    	if (heroGoesFirst)
	    	{
	    		player = new AutoCombatant.CombatantBuilder().withInitiative(11).withSpecificHit(20).withWeapon(CombatantWeapon.ONE_DAMAGE_WEAPON).withHP(1).build();
	    		enemy = new AutoCombatant.CombatantBuilder().withType(CombatantType.ENEMY).withSpecificHit(20).withWeapon(CombatantWeapon.ONE_DAMAGE_WEAPON).withHP(1).build();
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
    }
    
    @Test
    public void TwoAttackersCanGangUpOnSingleDefender()
    {
        MsgCtrl.msgln("TwoAttackersCanGangUpOnSingleDefender()");
    	Combatant player = new AutoCombatant.CombatantBuilder().withSpecificHit(20).withWeapon(CombatantWeapon.ONE_DAMAGE_WEAPON).withHP(1).build();
    	CombatantInterface enemy1 = new AutoCombatant.CombatantBuilder().withType(CombatantType.ENEMY).withSpecificHit(20).withWeapon(CombatantWeapon.ONE_DAMAGE_WEAPON).withHP(1).build();
    	CombatantInterface enemy2 = new AutoCombatant.CombatantBuilder().withType(CombatantType.ENEMY).withSpecificHit(20).withWeapon(CombatantWeapon.ONE_DAMAGE_WEAPON).withHP(1).build();
    	Battle battle = new Battle(player, enemy1, enemy2);
        assertTrue(battle.isInBattle(player));
        assertTrue(battle.isInBattle(enemy1));
        assertTrue(battle.isInBattle(enemy2));
    }
    
    @Test
    public void ThreeAttackersCanGangUpOnSingleDefender()
    {
        MsgCtrl.msgln("ThreeAttackersCanGangUpOnSingleDefender()");
    	Combatant player = new AutoCombatant.CombatantBuilder().withSpecificHit(20).withWeapon(CombatantWeapon.ONE_DAMAGE_WEAPON).withHP(1).build();
    	Combatant enemy1 = new AutoCombatant.CombatantBuilder().withType(CombatantType.ENEMY).withSpecificHit(20).withWeapon(CombatantWeapon.ONE_DAMAGE_WEAPON).withHP(1).build();
    	Combatant enemy2 = new AutoCombatant.CombatantBuilder().withType(CombatantType.ENEMY).withSpecificHit(20).withWeapon(CombatantWeapon.ONE_DAMAGE_WEAPON).withHP(1).build();
    	Combatant enemy3 = new AutoCombatant.CombatantBuilder().withType(CombatantType.ENEMY).withSpecificHit(20).withWeapon(CombatantWeapon.ONE_DAMAGE_WEAPON).withHP(1).build();
    	Battle battle = new Battle(new Combatant[] {player, enemy1, enemy2, enemy3});
        assertTrue(battle.isInBattle(player));
        assertTrue(battle.isInBattle(enemy1));
        assertTrue(battle.isInBattle(enemy2));
        assertTrue(battle.isInBattle(enemy3));
    }
    
    @Test
    public void AllCombatantsOnASideAttackEachRound()
    {
    	for (int round = 0; round < 25; round++) {
            MsgCtrl.msgln("AllCombatantsOnASideAttackEachRound() round " + round);
	    	int numberOfAttackers = _metadie.getRandom(5, 15);
	    	CombatantType attackerType = CombatantType.HERO;
	    	CombatantType defenderType = CombatantType.ENEMY;
	    	if (_metadie.getRandom(1,2) == 2) {
	    		attackerType = CombatantType.ENEMY;
	        	defenderType = CombatantType.HERO;
	    	}
	    	List<Combatant> battleMembers = new ArrayList<Combatant>(numberOfAttackers + 1);
	    	for (int i = 0; i < numberOfAttackers; i++) {
	    		battleMembers.add(i, new AutoCombatant.CombatantBuilder().withSpecificHit(10).withType(attackerType).build());
	    	}
			Combatant defender = new AutoCombatant.CombatantBuilder().withType(defenderType).withHP(numberOfAttackers).build();
	    	battleMembers.add(numberOfAttackers, defender);
	    	
	    	Battle battle = new Battle(battleMembers.toArray(new Combatant[numberOfAttackers + 1]));
	    	battle.advance();
	    	
	    	assertTrue(defender.isDefeated());
    	}
    }

    @Test
    public void CannotTargetDefeatedPlayerInBattle()
    {
        MsgCtrl.msgln("CannotTargetDefeatedPlayerInBattle()");
    	int numberOfAttackers = _metadie.getRandom(1,20);
    	CombatantType attackerType = CombatantType.HERO;
    	CombatantType defenderType = CombatantType.ENEMY;
    	if (_metadie.getRandom(1,2) == 1) {
    		attackerType = CombatantType.ENEMY;
    		defenderType = CombatantType.HERO;
    	}
    	
    	List<CombatantInterface> battleMembers = new ArrayList<CombatantInterface>(numberOfAttackers + 1);
    	for (int i = 0; i < numberOfAttackers; i++) {
    		battleMembers.add(i, new AutoCombatant.CombatantBuilder().withType(attackerType).withSpecificHit(10).build());
    	}
    	AutoCombatant defender = new AutoCombatant.CombatantBuilder().withType(defenderType).withHP(numberOfAttackers - 1).build();
    	battleMembers.add(numberOfAttackers, defender);
    	
    	Battle battle = new Battle(battleMembers.toArray(new CombatantInterface[numberOfAttackers + 1]));
    	battle.advance();
    	
    	assertTrue(defender.isDefeated());
    	assertTrue(defender.getAttackCount() == numberOfAttackers - 1);
    }
    
    @Test
    public void IfCombatantIsDefeatedAndOtherTargetsAvailableTheyAreSelected()
    {
        for (int round = 0; round < 10; round++) {
            MsgCtrl.msgln("IfCombatantIsDefeatedAndOtherTargetsAvailableTheyAreSelected() round " + round);
	    	int numberOfTargets = _metadie.getRandom(1,10);
	    	CombatantType attackerType = CombatantType.HERO;
	    	CombatantType defenderType = CombatantType.ENEMY;
	
	    	List<CombatantInterface> battleMembers = new ArrayList<CombatantInterface>(2 * numberOfTargets);
	    	for (int i = 0; i < numberOfTargets; i++) {
	    		battleMembers.add(new AutoCombatant.CombatantBuilder().withType(defenderType).withHP(1).build());
	    		battleMembers.add(new AutoCombatant.CombatantBuilder().withType(attackerType).withSpecificHit(10).build());
	    	}
	    	
	    	Battle battle = new Battle(battleMembers.toArray(new CombatantInterface[2 * numberOfTargets]));
	    	battle.advance();
	    	
	    	assertFalse(battle.isOngoing());
	    	for (CombatantInterface c : Combatant.findAllEnemies(battleMembers)) {
	    		assertTrue(c.isDefeated());
	    	}
        }
    }
    
    @Test
    public void WhenMultipleCombatantsOnASideHighestInitiativeRollGoesFirst()
    {
        for (int round = 0; round < 20; round++) {
            MsgCtrl.msgln("BattleDoesNotEndWhenNeitherPlayerIsDefeated() round " + round);
	    	int numberOfHeros = _metadie.getRandom(1, 20);
	    	int numberOfEnemies = _metadie.getRandom(1, 20);
	
	    	List<CombatantInterface> battleMembers = new ArrayList<CombatantInterface>(numberOfEnemies + numberOfHeros);
	    	for (int i = 0; i < numberOfHeros; i++) {
	    		battleMembers.add(new AutoCombatant.CombatantBuilder().withType(CombatantType.HERO).withHP(1).withSpecificHit(10).withInitiative(i).build());
	    	}
	    	
	    	for (int i = 0; i < numberOfEnemies; i++) {
	    		battleMembers.add(new AutoCombatant.CombatantBuilder().withType(CombatantType.ENEMY).withHP(1).withSpecificHit(10).withInitiative(i).build());
	    	}
	    	
	    	Battle battle = new Battle(battleMembers.toArray(new CombatantInterface[numberOfEnemies + numberOfHeros]));
	    	battle.advance();
	    	
	    	assertFalse(battle.isOngoing());
	    	if (numberOfEnemies > numberOfHeros) {
	    		for (CombatantInterface c : Combatant.findAllHeros(battleMembers)) {
	    			assertTrue(c.isDefeated());
	    		}    			
	    	} else {
	    		for (CombatantInterface c : Combatant.findAllEnemies(battleMembers)) {
	    			assertTrue(c.isDefeated());
	    		}
	    	}
	    }
    }
    
    //All combatants from a side must escape to end combat
    public void AllCombatantsMustEscapeForCombatToEnd()
    {
        MsgCtrl.msgln("BattleDoesNotEndWhenNeitherPlayerIsDefeated()");
    	int numberOfHeros = 0;
    }
    
    //All combatants from a side must be defeated to end combat
    
    //Combatants must be defeated/escape to end combat
    
    //Combatant can select with opponent to attack
}
