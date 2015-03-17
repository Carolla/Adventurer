package test.battle;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import mylib.MsgCtrl;
import mylib.pdc.MetaDie;

import org.junit.BeforeClass;
import org.junit.Test;

import battle.Combatant;
import battle.Combatant.CombatantArmor;
import battle.Combatant.CombatantWeapon;

public class TestCombatant
{
	private static MetaDie meta;

	@BeforeClass
	public static void setupClass()
	{
		meta = new MetaDie(System.currentTimeMillis());
	}
	
    @Test
    public void CombatantIsUnconsciousWhenHpLessThanZero()
    {
        MsgCtrl.msgln("\n\nCombatantIsUnconsciousWhenHpLessThanZero");
    	Combatant attacker = new AutoCombatant.CombatantBuilder().withWeapon(CombatantWeapon.MORNING_STAR).withSpecificHit(20).build();
    	Combatant victim = new AutoCombatant.CombatantBuilder().withHP(2).build();
        assertFalse(victim.isUnconscious());
        attacker.attack(victim);
        assertTrue(victim.isUnconscious());
    }

    @Test
    public void CombatantDoesVariableDamage()
    {
        MsgCtrl.msgln("\n\nCombatantDoesVariableDamage");
    	Combatant attacker1 = new AutoCombatant.CombatantBuilder().withWeapon(CombatantWeapon.ONE_DAMAGE_WEAPON).withSpecificHit(20).build();
    	Combatant attacker2 = new AutoCombatant.CombatantBuilder().withWeapon(CombatantWeapon.MORNING_STAR).withSpecificHit(20).build();
    	Combatant victim1 = new AutoCombatant.CombatantBuilder().withHP(2).build();
    	Combatant victim2 = new AutoCombatant.CombatantBuilder().withHP(2).build();
    	attacker1.attack(victim1);
    	attacker2.attack(victim2);
    	assertFalse(victim1.isUnconscious());
    	assertTrue(victim2.isUnconscious());
    }
    
    @Test
    public void CombatantArmorClassDeterminesHit()
    {
        MsgCtrl.msgln("\n\nCombatantArmorClassDeterminesHit");
        Combatant attacker1 = new AutoCombatant.CombatantBuilder().withSpecificHit(10).build();
        Combatant attacker2 = new AutoCombatant.CombatantBuilder().withSpecificHit(11).build();
        Combatant victim = new AutoCombatant.CombatantBuilder().withAC(11).withHP(1).build();
    	attacker1.attack(victim);
    	assertFalse(victim.isUnconscious());
    	attacker2.attack(victim);
    	assertTrue(victim.isUnconscious());
    }
    
    @Test
    public void CombatantCanHaveDifferentArmorClass()
    {
        MsgCtrl.msgln("\n\nCombatantCanHaveDifferentArmorClass");
        Combatant attacker = new AutoCombatant.CombatantBuilder().withSpecificHit(10).build();
        Combatant victim1 = new AutoCombatant.CombatantBuilder().withAC(11).withHP(1).build();
        Combatant victim2 = new AutoCombatant.CombatantBuilder().withAC(10).withHP(1).build();
        attacker.attack(victim1);
    	assertFalse(victim1.isUnconscious());
    	attacker.attack(victim2);
    	assertTrue(victim2.isUnconscious());
    }
    
    @Test
    public void CombatantAttackHitsWhenEqualToAC()
    {
        MsgCtrl.msgln("\n\nCombatantAttackHitsWhenEqualToAC");
    	for (int i = 0; i < 20; i++)
    	{
	    	int attackAndAc = meta.getRandom(2, 19);
	    	Combatant attacker = new AutoCombatant.CombatantBuilder().withSpecificHit(attackAndAc).build();
	    	Combatant victim = new AutoCombatant.CombatantBuilder().withAC(attackAndAc).withHP(1).build();
	        attacker.attack(victim);
	    	assertTrue(victim.isUnconscious());
    	}
    }
    
    @Test
    public void CombatantAttackMissWhenLessThanAC()
    {
        MsgCtrl.msgln("\n\nCombatantAttackMissWhenLessThanAC");
    	for (int i = 0; i < 20; i++)
    	{
	    	int attackRoll = meta.getRandom(2, 19);
	    	Combatant attacker = new AutoCombatant.CombatantBuilder().withSpecificHit(attackRoll).build();
	    	Combatant victim = new AutoCombatant.CombatantBuilder().withAC(attackRoll + 1).withHP(1).build();
	        attacker.attack(victim);
	    	assertFalse(victim.isUnconscious());
    	}
    }
    
    @Test
    public void CombatantAttackHitsWhenGreaterThanAC()
    {
        MsgCtrl.msgln("\n\nCombatantAttackHitsWhenGreaterThanAC");
    	for (int i = 0; i < 20; i++)
    	{
	    	int ac = meta.getRandom(2, 18);
	    	Combatant attacker = new AutoCombatant.CombatantBuilder().withSpecificHit(ac + 1).build();
	    	Combatant victim = new AutoCombatant.CombatantBuilder().withAC(ac).withHP(1).build();
	        attacker.attack(victim);
	    	assertTrue(victim.isUnconscious());
    	}
    }
    
    @Test
    public void CombatantAlwaysMissesWithOne()
    {
        MsgCtrl.msgln("\n\nCombatantAlwaysMissesWithZero");
        Combatant attacker = new AutoCombatant.CombatantBuilder().withSpecificHit(1).build();
        Combatant victim = new AutoCombatant.CombatantBuilder().withAC(1).withHP(1).build();
        attacker.attack(victim);
    	attacker.attack(victim);
    	assertFalse(victim.isUnconscious());
    }
    
    @Test
    public void CombatantAlwaysHitsWithTwenty()
    {
        MsgCtrl.msgln("\n\nCombatantAlwaysHitsWithTwenty");
        Combatant attacker = new AutoCombatant.CombatantBuilder().withSpecificHit(20).build();
        Combatant victim = new AutoCombatant.CombatantBuilder().withAC(21).withHP(1).build();
        attacker.attack(victim);
    	assertTrue(victim.isUnconscious());
    }
    
    @Test
    public void CombatantCanEquipArmorAndChangeAc()
    {
        MsgCtrl.msgln("\n\nCombatantCanEquipArmorAndChangeAc");
        Combatant attacker = new AutoCombatant.CombatantBuilder().withWeapon(CombatantWeapon.ONE_DAMAGE_WEAPON).withSpecificHit(10).build();
        Combatant victim1 = new AutoCombatant.CombatantBuilder().withHP(1).build();
        Combatant victim2 = new AutoCombatant.CombatantBuilder().withHP(1).withArmor(CombatantArmor.HELMET).build();
    	attacker.attack(victim1);
    	attacker.attack(victim2);
    	assertFalse(victim2.isUnconscious());
    	assertTrue(victim1.isUnconscious());
    }
    
    @Test
    public void CombatantCanChangeEquippedArmorDuringBattleAndChangeAc()
    {
        MsgCtrl.msgln("\n\nCombatantCanEquipArmorAndChangeAc");
        Combatant attacker = new AutoCombatant.CombatantBuilder().withWeapon(CombatantWeapon.ONE_DAMAGE_WEAPON).withSpecificHit(10).build();
        Combatant victim1 = new AutoCombatant.CombatantBuilder().withHP(2).build();
        Combatant victim2 = new AutoCombatant.CombatantBuilder().withHP(2).build();
    	attacker.attack(victim1);
    	attacker.attack(victim2);
    	victim2.equip(CombatantArmor.SHIELD);
    	attacker.attack(victim1);
    	attacker.attack(victim2);
    	assertFalse(victim2.isUnconscious());
    	assertTrue(victim1.isUnconscious());
    }
    
    @Test
    public void CombatantCanChangeEquippedWeaponDuringBattleAndChangeDamage()
    {
        MsgCtrl.msgln("\n\nCombatantCanChangeEquippedWeaponDuringBattleAndChangeDamage");
        Combatant attacker = new AutoCombatant.CombatantBuilder().withWeapon(CombatantWeapon.ONE_DAMAGE_WEAPON).withSpecificHit(10).build();
        Combatant victim1 = new AutoCombatant.CombatantBuilder().withHP(2).build();
        Combatant victim2 = new AutoCombatant.CombatantBuilder().withHP(2).build();
    	attacker.attack(victim1);
    	attacker.equip(CombatantWeapon.MORNING_STAR);
    	attacker.attack(victim2);
    	assertTrue(victim2.isUnconscious());
    	assertFalse(victim1.isUnconscious());
    }
    
    @Test
    public void CombatantCanUnequipArmorDuringBattle()
    {
        MsgCtrl.msgln("\n\nCombatantCanUnequipArmorDuringBattle");
        Combatant attacker = new AutoCombatant.CombatantBuilder().withWeapon(CombatantWeapon.ONE_DAMAGE_WEAPON).withSpecificHit(10).build();
        Combatant victim = new AutoCombatant.CombatantBuilder().withHP(2).build();
    	attacker.attack(victim);
    	victim.equip(CombatantArmor.SHIELD);
    	attacker.attack(victim);
    	assertFalse(victim.isUnconscious());
    	victim.unequip(CombatantArmor.SHIELD);
    	attacker.attack(victim);
    	assertTrue(victim.isUnconscious());
    }
    
    @Test
    public void UnequippingArmorNotEquippedDoesNotChangeAc()
    {
        MsgCtrl.msgln("\n\nUnequippingArmorNotEquippedDoesNotChangeAc");
        Combatant attacker = new AutoCombatant.CombatantBuilder().withWeapon(CombatantWeapon.ONE_DAMAGE_WEAPON).withSpecificHit(10).build();
        Combatant victim = new AutoCombatant.CombatantBuilder().withHP(2).build();
    	attacker.attack(victim);
    	victim.equip(CombatantArmor.SHIELD);
    	attacker.attack(victim);
    	victim.unequip(CombatantArmor.HELMET);
    	attacker.attack(victim);
    	assertFalse(victim.isUnconscious());
    }
    
    @Test
    public void CannotEquipSamePieceOfArmorMultipleTimes()
    {
    	fail("Not yet implemented");
    }
    
    @Test
    public void CombatantCanUnequipWeaponDuringBattle()
    {
        MsgCtrl.msgln("\n\nCombatantCanUnequipWeaponDuringBattle");
        Combatant attacker = new AutoCombatant.CombatantBuilder().withWeapon(CombatantWeapon.ONE_DAMAGE_WEAPON).withSpecificHit(10).build();
        Combatant victim1 = new AutoCombatant.CombatantBuilder().withHP(2).build();
        Combatant victim2 = new AutoCombatant.CombatantBuilder().withHP(2).build();
        Combatant victim3 = new AutoCombatant.CombatantBuilder().withHP(2).build();
    	attacker.attack(victim1);
    	attacker.equip(CombatantWeapon.MORNING_STAR);
    	attacker.attack(victim2);
    	attacker.unequip(CombatantWeapon.MORNING_STAR);
    	attacker.attack(victim3);
    	assertFalse(victim1.isUnconscious());
    	assertTrue(victim2.isUnconscious());
    	assertFalse(victim3.isUnconscious());
    }
    
    @Test
    public void UnequippingWeaponNotEquippedDoesNotChangeDamage()
    {
        MsgCtrl.msgln("\n\nUnequippingWeaponNotEquippedDoesNotChangeDamage");
        Combatant attacker = new AutoCombatant.CombatantBuilder().withWeapon(CombatantWeapon.ONE_DAMAGE_WEAPON).withSpecificHit(10).build();
        Combatant victim1 = new AutoCombatant.CombatantBuilder().withHP(3).build();
        Combatant victim2 = new AutoCombatant.CombatantBuilder().withHP(3).build();
        Combatant victim3 = new AutoCombatant.CombatantBuilder().withHP(3).build();
    	attacker.attack(victim1);
    	attacker.equip(CombatantWeapon.MORNING_STAR);
    	attacker.attack(victim2);
    	attacker.unequip(CombatantWeapon.DAGGER);
    	attacker.attack(victim3);
    	assertFalse(victim1.isUnconscious());
    	assertTrue(victim2.isUnconscious());
    	assertTrue(victim3.isUnconscious());
    }
    
    @Test
    public void WhenAttackRollIsOneAttackerFumblesWeapon()
    {
        MsgCtrl.msgln("\n\nWhenAttackRollIsOneAttackerFumblesWeapon");
        Combatant attacker = new AutoCombatant.CombatantBuilder().withWeapon(CombatantWeapon.MORNING_STAR).withSpecificHit(1).build();
        Combatant victim1 = new AutoCombatant.CombatantBuilder().withHP(1).build();
        attacker.attack(victim1);
        assertTrue(attacker.equip(CombatantWeapon.MORNING_STAR));
    }
    
    @Test
    public void IncreaseStrengthCanCauseAdditionalDamageWhenAttacking()
    {
        MsgCtrl.msgln("\n\nIncreaseStrengthCanCauseAdditionalDamageWhenAttacking");
        Combatant attacker1 = new AutoCombatant.CombatantBuilder().withStrength(16).withSpecificHit(10).build();
        Combatant attacker2 = new AutoCombatant.CombatantBuilder().withStrength(15).withSpecificHit(10).build();
        Combatant victim1 = new AutoCombatant.CombatantBuilder().withHP(2).build();
        Combatant victim2 = new AutoCombatant.CombatantBuilder().withHP(2).build();
        attacker1.attack(victim1);
        attacker2.attack(victim2);
    	assertTrue(victim1.isUnconscious());
    	assertFalse(victim2.isUnconscious());
    }
    
    @Test
    public void DecreasedStrengthCanCauseLessDamageWhenAttacking()
    {
        MsgCtrl.msgln("\n\nDecreasedStrengthCanCauseLessDamageWhenAttacking");
        Combatant attacker1 = new AutoCombatant.CombatantBuilder().withStrength(6).withSpecificHit(10).build();
        Combatant attacker2 = new AutoCombatant.CombatantBuilder().withStrength(5).withSpecificHit(10).build();
        Combatant victim1 = new AutoCombatant.CombatantBuilder().withHP(1).build();
        Combatant victim2 = new AutoCombatant.CombatantBuilder().withHP(1).build();
        attacker1.attack(victim1);
        attacker2.attack(victim2);
    	assertTrue(victim1.isUnconscious());
    	assertFalse(victim2.isUnconscious());
    }
}
