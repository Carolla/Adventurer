package test.battle;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import mylib.MsgCtrl;

import org.junit.Before;
import org.junit.Test;

import battle.Battle;
import battle.Combatant;
import battle.Combatant.CombatantType;
import battle.Combatant.CombatantWeapon;

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
	    MsgCtrl.msgln("\n\nThePlayerWantsToDoCombatWithTheEnemy()");
		Combatant player = new Combatant();
		Combatant enemy = new AutoCombatant.CombatantBuilder().withType(CombatantType.ENEMY).build();
		Battle battle = new Battle(player, enemy);
		assertTrue(battle.isInBattle(player));
		assertTrue(battle.isInBattle(enemy));
	}
	
	@Test
	public void ThePlayerWantsToDefeatTheEnemyInBattle()
	{
	    MsgCtrl.msgln("\n\nThePlayerWantsToDefeatTheEnemyInBattle()");
		Combatant player = new AutoCombatant.CombatantBuilder().withWeapon(CombatantWeapon.MORNING_STAR).withSpecificHit(20).build();
		Combatant enemy = new AutoCombatant.CombatantBuilder().withType(CombatantType.ENEMY).build();
		Battle battle = new Battle(player, enemy);
		while (battle.isOngoing()) {
			battle.advance();
		}
		assertFalse(player.isDefeated());
		assertTrue(enemy.isDefeated());
	}
	
	@Test
	public void ThePlayerWantsToDamageTheEnemyInBattle()
	{
        MsgCtrl.msgln("\n\nThePlayerWantsToDamageTheEnemyInBattle()");
		Combatant player = new AutoCombatant.CombatantBuilder().build();
		Combatant enemy = new AutoCombatant.CombatantBuilder().withType(CombatantType.ENEMY).build();
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
        MsgCtrl.msgln("\n\nThePlayerWantsToKnockTheEnemyUnconscious()");
		Combatant player = new AutoCombatant.CombatantBuilder().build();
		Combatant enemy = new AutoCombatant.CombatantBuilder().withType(CombatantType.ENEMY).withSpecificHit(0).build();
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
        MsgCtrl.msgln("\n\nThePlayerWantsToKnockTheEnemyUnconscious()");
		Combatant player = new AutoCombatant.CombatantBuilder().withHP(2).build();
		Combatant enemy = new AutoCombatant.CombatantBuilder().withType(CombatantType.ENEMY).withSpecificHit(20).build();
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
        MsgCtrl.msgln("\n\nTheDMWantsThePlayerToBeHitByTheEnemy()");
		Combatant player = new AutoCombatant.CombatantBuilder().build();
		Combatant enemy = new AutoCombatant.CombatantBuilder().withType(CombatantType.ENEMY).withSpecificHit(20).build();
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
        MsgCtrl.msgln("\n\nTheDMWantsThePlayerToBeKnockedOutByTheEnemy()");
		Combatant player = new AutoCombatant.CombatantBuilder().withSpecificHit(0).build();
		Combatant enemy = new AutoCombatant.CombatantBuilder().withType(CombatantType.ENEMY).withWeapon(CombatantWeapon.MORNING_STAR).build();
		Battle battle = new Battle(player, enemy);
		assertFalse(player.isUnconscious());
		while (battle.isOngoing()) {
		    battle.advance();
		}
		assertTrue(player.isUnconscious());
	}
	
	@Test
	public void ThePlayerWantsToEscapeIfAboutToDie()
	{
        MsgCtrl.msgln("\n\nTheDMWantsThePlayerToBeKnockedOutByTheEnemy()");
		Combatant player = new AutoCombatant.CombatantBuilder().withSpecificHit(0).shouldTryEscaping().build();
		Combatant enemy = new AutoCombatant.CombatantBuilder().withType(CombatantType.ENEMY).withSpecificHit(20).withWeapon(CombatantWeapon.ONE_DAMAGE_WEAPON).build();
		Battle battle = new Battle(player, enemy);
		assertFalse(player.isUnconscious());
		assertFalse(battle.combatantEscaped(player));
		while (battle.isOngoing()) {
		    battle.advance();
		}
		assertTrue(battle.combatantEscaped(player));
		assertFalse(battle.combatantEscaped(enemy));
		assertFalse(player.isUnconscious());
	}
}
