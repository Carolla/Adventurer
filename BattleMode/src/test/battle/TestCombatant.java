package test.battle;

import static org.junit.Assert.*;

import org.junit.Test;

import test.battle.AutoCombatant.CombatantType;
import battle.Attack;

public class TestCombatant
{
    @Test
    public void CombatantIsUnconsciousWhenHpLessThanZero()
    {
        AutoCombatant a = new AutoCombatant(CombatantType.HERO);
        assertFalse(a.isUnconscious());
        a.attack(new Attack(20,11));
        assertTrue(a.isUnconscious());
    }
    
    @Test
    public void CombatantIsDefeatedWhenEscapingBattle()
    {
        fail("Not yet implemented");
    }
}
