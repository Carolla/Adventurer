package test.battle;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import mylib.MsgCtrl;

import org.junit.Before;
import org.junit.Test;

import test.battle.AutoCombatant.CombatantType;
import battle.Battle;
import battle.Combatant;

/**
##########################Use Case Brief##############################
The player will enter into a battle with the enemy. If a melee begins, 
then in a series of alternating turns, the player and enemy will attempt
to subdue each other. Finally, a battle comes to end when 
the player or enemy has been defeated.
######################################################################

Notes: 
// THIS IS A USE CASE!!! not a user story.

1) The player may enter into combat many ways:
   surprise, capture, random encounter, etc.

2) Before coming to blows the player may overcome or escape 
   the enemy by wit or guile.

3) There are many various forms of attack, including:
   items, potions, weapons, magic, etc.

4) Defeat may occur in many ways:
   unconsciousness, death, interruption in some way (flight/retreat, intercession, etc.)*/

public class BattleUseCase {
    @Before
    public void setup()
    {
        MsgCtrl.auditMsgsOn(true);
    }

	@Test
	public void ThePlayerWantsToDoCombatWithTheEnemy()
	{
	    MsgCtrl.msgln("ThePlayerWantsToDoCombatWithTheEnemy()");
		Combatant player = new DummyCombatant(true);
		Combatant enemy = new DummyCombatant(false);
		Battle battle = new Battle(player, enemy);
		assertTrue(battle.isInBattle(player));
		assertTrue(battle.isInBattle(enemy));
	}
	
	@Test
	public void ThePlayerWantsToDefeatTheEnemyInBattle()
	{
	    MsgCtrl.msgln("ThePlayerWantsToDefeatTheEnemyInBattle()");
		Combatant player = new DummyCombatant(true);
		Combatant enemy = new DummyCombatant(false);
		Battle battle = new Battle(player, enemy);
		battle.advance();
		assertFalse(player.isDefeated());
		assertTrue(enemy.isDefeated());
	}
	
	@Test
	public void ThePlayerWantsToDamageTheEnemyInBattle()
	{
        MsgCtrl.msgln("ThePlayerWantsToDamageTheEnemyInBattle()");
		Combatant player = new AutoCombatant(CombatantType.HERO);
		Combatant enemy = new AutoCombatant(CombatantType.ENEMY);
		Battle battle = new Battle(player, enemy);
		assertTrue(enemy.hasFullHP());
		while (battle.isOngoing()) {
		    battle.advance();
		}
		assertFalse(enemy.hasFullHP());
	}
	
	@Test
	public void ThePlayerWantsToKnockTheEnemyUnconscious() 
	{
        MsgCtrl.msgln("ThePlayerWantsToKnockTheEnemyUnconscious()");
		Combatant player = new AutoCombatant(CombatantType.HERO);
		Combatant enemy = new AutoCombatant(CombatantType.WEAK_ENEMY);
		Battle battle = new Battle(player, enemy);
		assertTrue(enemy.hasFullHP());
		while (battle.isOngoing()) {
		    battle.advance();
		}
		assertFalse(enemy.hasFullHP());
		assertTrue(enemy.isUnconscious());
	}
	
	@Test
	public void TheDMWantsThePlayerToBeDefeatedByTheEnemy() 
	{
        MsgCtrl.msgln("ThePlayerWantsToKnockTheEnemyUnconscious()");
		Combatant player = new AutoCombatant(CombatantType.IMMOBILE_HERO);
		Combatant enemy = new AutoCombatant(CombatantType.STRONG_ENEMY);
		Battle battle = new Battle(player, enemy);
		assertFalse(player.isDefeated());
		while (battle.isOngoing()) {
		    battle.advance();
		}
		assertTrue(player.isDefeated());
	}

	@Test
	public void TheDMWantsThePlayerToBeHitByTheEnemy() { }
	{
        MsgCtrl.msgln("TheDMWantsThePlayerToBeHitByTheEnemy()");
		Combatant player = new AutoCombatant(CombatantType.HERO);
		Combatant enemy = new AutoCombatant(CombatantType.STRONG_ENEMY);
		Battle battle = new Battle(player, enemy);
		assertTrue(player.hasFullHP());
		while (battle.isOngoing()) {
		    battle.advance();
		}
		assertFalse(player.hasFullHP());
	}

	@Test
	public void TheDMWantsThePlayerToBeKnockedOutByTheEnemy() 
	{
        MsgCtrl.msgln("TheDMWantsThePlayerToBeKnockedOutByTheEnemy()");
		Combatant player = new AutoCombatant(CombatantType.IMMOBILE_HERO);
		Combatant enemy = new AutoCombatant(CombatantType.STRONG_ENEMY);
		Battle battle = new Battle(player, enemy);
		assertFalse(player.isUnconscious());
		while (battle.isOngoing()) {
		    battle.advance();
		}
		assertTrue(player.isUnconscious());
	}
	
	@Test
	public void ThePlayerWantsToEscapeIfTheDMIsBeingMean()
	{
        MsgCtrl.msgln("TheDMWantsThePlayerToBeKnockedOutByTheEnemy()");
		Combatant player = new AutoCombatant(CombatantType.IMMOBILE_HERO);
		Combatant enemy = new AutoCombatant(CombatantType.WEAK_ENEMY);
		Battle battle = new Battle(player, enemy);
		assertFalse(player.isUnconscious());
		while (battle.isOngoing()) {
		    battle.advance();
		}
		assertTrue(battle.combatantEscaped(player));
		assertTrue(player.isDefeated());
		assertFalse(player.isUnconscious());
	}
}
