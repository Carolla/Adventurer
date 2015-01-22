package test.battle;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

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

	@Test
	public void ThePlayerWantsToDoCombatWithTheEnemy()
	{
		Combatant player = new DummyCombatant(true);
		Combatant enemy = new DummyCombatant(false);
		Battle battle = new Battle(player, enemy);
		assertTrue(battle.isInBattle(player));
		assertTrue(battle.isInBattle(enemy));
	}
	
	@Test
	public void ThePlayerWantsToDefeatTheEnemyInBattle()
	{
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
		Combatant player = new AutoCombatant(CombatantType.HERO);
		Combatant enemy = new AutoCombatant(CombatantType.ENEMY);
		Battle battle = new Battle(player, enemy);
		assertTrue(enemy.hasFullHP());
		while (battle.isOngoing()) {
		    battle.advance();
		}
		assertFalse(enemy.hasFullHP());
		assertTrue(enemy.isUnconscious());
	}
}
