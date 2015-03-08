package test.battle;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import test.battle.AutoCombatant.CombatantAttack;
import test.battle.AutoCombatant.CombatantDamage;
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

    @Test
    public void CombatantDoesVariableDamage()
    {
    	AutoCombatant attacker1 = new AutoCombatant.CombatantBuilder().withDamage(CombatantDamage.FIST).withHit(CombatantAttack.AUTO_HIT).build();
    	AutoCombatant attacker2 = new AutoCombatant.CombatantBuilder().withDamage(CombatantDamage.MORNING_STAR).withHit(CombatantAttack.AUTO_HIT).build();
    	AutoCombatant victim1 = new AutoCombatant.CombatantBuilder().withHP(2).build();
    	AutoCombatant victim2 = new AutoCombatant.CombatantBuilder().withHP(2).build();
    	attacker1.attack(victim1);
    	attacker2.attack(victim2);
    	assertFalse(victim1.isUnconscious());
    	assertTrue(victim2.isUnconscious());
    }
    
    @Test
    public void CombatantArmorClassDeterminesHit()
    {
    	AutoCombatant attacker1 = new AutoCombatant.CombatantBuilder().withSpecificHit(10).build();
    	AutoCombatant attacker2 = new AutoCombatant.CombatantBuilder().withSpecificHit(11).build();
    	AutoCombatant victim = new AutoCombatant.CombatantBuilder().withAC(11).withHP(1).build();
    	attacker1.attack(victim);
    	assertFalse(victim.isUnconscious());
    	attacker2.attack(victim);
    	assertTrue(victim.isUnconscious());
    }
    
    @Test
    public void CombatantCanHaveDifferentArmorClass()
    {
    	AutoCombatant attacker = new AutoCombatant.CombatantBuilder().withSpecificHit(10).build();
    	AutoCombatant victim1 = new AutoCombatant.CombatantBuilder().withAC(11).withHP(1).build();
    	AutoCombatant victim2 = new AutoCombatant.CombatantBuilder().withAC(10).withHP(1).build();
        attacker.attack(victim1);
    	assertFalse(victim1.isUnconscious());
    	attacker.attack(victim2);
    	assertTrue(victim2.isUnconscious());
    }
    
    @Test
    public void CombatantAlwaysMissesWithZero()
    {
    	AutoCombatant attacker = new AutoCombatant.CombatantBuilder().withSpecificHit(0).build();
    	AutoCombatant victim = new AutoCombatant.CombatantBuilder().withAC(0).withHP(1).build();
        attacker.attack(victim);
    	attacker.attack(victim);
    	assertFalse(victim.isUnconscious());
    }
}
