package test.battle;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import battle.Attack;

public class TestCombatant
{
    @Test
    public void CombatantIsUnconsciousWhenHpLessThanZero()
    {
        AutoCombatant a = new AutoCombatant.CombatantBuilder().build();
        assertFalse(a.isUnconscious());
        a.attacked(new Attack(20,11));
        assertTrue(a.isUnconscious());
    }

    
//    @Test
//    public void CombatantIsDefeatedWhenEscapingBattle()
//    {
//        fail("Not yet implemented");
//    }
}
