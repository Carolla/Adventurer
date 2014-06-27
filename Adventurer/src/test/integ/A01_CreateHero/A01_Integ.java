package test.integ.A01_CreateHero;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import pdc.character.Person;
import chronos.Chronos.ATTRIBUTE;
import chronos.civ.MiscKeys.Literacy;
import chronos.pdc.AttributeList;
import chronos.pdc.Occupation;
import chronos.pdc.Race;
import chronos.pdc.Skill;
import chronos.pdc.registry.OccupationRegistry;
import chronos.pdc.registry.RegistryFactory;
import chronos.pdc.registry.RegistryFactory.RegKey;
import chronos.pdc.registry.SkillRegistry;

public class A01_Integ
{

    @Test
    public void itValidatesTheName()
    {
        pdc.character.Person p = PersonMaker.makePerson("A Valid Name");
        assertPersonHasValidName(p);
    }

    private void assertPersonHasValidName(Person p)
    {
        assertTrue("Name should not be in database", nameIsInDatabase(p));
        assertTrue("Name should be less than 30 characters", p.getName().length() < 31);
    }

    private boolean nameIsInDatabase(Person p)
    {
        // TODO Auto-generated method stub
        return false;
    }

    @Test
    public void itAdjustsTraitsForGender()
    {
        Person p = PersonMaker.makePerson("MName", Race.MALE);
        Person p2 = PersonMaker.makePerson("FName", Race.FEMALE);
        assertGenderTraitsAreCorrect(p, p2);
    }

    private void assertGenderTraitsAreCorrect(Person p, Person p2)
    {
        AttributeList t = p.getTraits();
        AttributeList t2 = p2.getTraits();
        assertEquals("Male should have +1 Strength", t.get(ATTRIBUTE.STR), t2.get(ATTRIBUTE.STR) + 1);
        assertEquals("Male should have -1 Constitution", t.get(ATTRIBUTE.CON), t2.get(ATTRIBUTE.CON) - 1);
        assertEquals("Male should have -1 Charisma", t.get(ATTRIBUTE.CHR), t2.get(ATTRIBUTE.CHR) - 1);
    }

    @Test
    public void itAdjustsTraitsForRace()
    {
        Person p = PersonMaker.makePerson("MName", Race.MALE, "Human");
        Person p2 = PersonMaker.makePerson("MName", Race.MALE, "Half-Orc");
        Person p3 = PersonMaker.makePerson("MName", Race.MALE, "Half-Elf");
        Person p4 = PersonMaker.makePerson("MName", Race.MALE, "Elf");
        Person p5 = PersonMaker.makePerson("MName", Race.MALE, "Dwarf");
        Person p6 = PersonMaker.makePerson("MName", Race.MALE, "Hobbit");
        Person p7 = PersonMaker.makePerson("MName", Race.MALE, "Gnome");
        assertRaceTraitsAreCorrect(p, p2, p3, p4, p5, p6, p7);
    }

    private void assertRaceTraitsAreCorrect(Person p, Person p2, Person p3, Person p4,
            Person p5, Person p6, Person p7)
    {
        // TODO Auto-generated method stub
    }

    @Test
    public void itAssignsValidPhysicalCharacteristics()
    {
        Person p = PersonMaker.makePerson("MName", Race.MALE);
        Person p2 = PersonMaker.makePerson("MName", Race.FEMALE);
        assertHeightAndWeightAreWithinBounds(p);
        assertHeightAndWeightAreWithinBounds(p2);
    }

    private void assertHeightAndWeightAreWithinBounds(Person p)
    {
        // TODO Auto-generated method stub
        Assert.fail();
    }

    @Test
    public void itCreatesTheDescription()
    {
        Person p = PersonMaker.makePerson();
        assertNotNull(p.getDescription());
    }

    @Test
    public void itCalculatesTheStrengthModifiers()
    {
        Person p = PersonMaker.makePerson(new AttributeList(new int[] { 18, 10, 10, 10, 10, 10 }));
        Person p2 = PersonMaker.makePerson(new AttributeList(new int[] { 14, 10, 10, 10, 10, 10 }));
        assertStrengthModifiersAreAppropriate(p, p2);
    }

    private void assertStrengthModifiersAreAppropriate(Person p, Person p2)
    {
        // TODO Auto-generated method stub
        assertTrue("Greater STR should increase STR mods", p.getWtAllowance() > p2.getWtAllowance());
    }

    @Test
    public void itCalculatesTheIntelligenceModifiers()
    {
        Person p = PersonMaker.makePerson(new AttributeList(new int[] { 10, 10, 18, 10, 10, 10 }));
        Person p2 = PersonMaker.makePerson(new AttributeList(new int[] { 14, 10, 14, 10, 10, 10 }));
        assertIntelligenceModifiersAreAppropriate(p, p2);
    }

    private void assertIntelligenceModifiersAreAppropriate(Person p, Person p2)
    {
        // TODO Auto-generated method stub
        assertTrue("Greater INT should increase INT mods", p.getWtAllowance() > p2.getWtAllowance());
    }

    @Test
    public void itDeterminesLiteracyAppropriately()
    {
        Person p = PersonMaker.makePerson(new AttributeList(new int[] { 10, 10, 18, 10, 10, 10 }));
        Person p2 = PersonMaker.makePerson(new AttributeList(new int[] { 14, 10, 11, 10, 10, 10 }));
        Person p3 = PersonMaker.makePerson(new AttributeList(new int[] { 14, 10, 8, 10, 10, 10 }));
        assertTrue("INT > 12 should ensure writing", p.getLiteracy().equals(Literacy.WRITING));
        assertTrue("10 < INT < 12 should ensure literacy", p2.getLiteracy().equals(Literacy.READING));
        assertTrue("INT < 10 should ensure illiteracy", p3.getLiteracy().equals(Literacy.ILLITERATE));
    }

    @Test
    public void itDeterminesExtraLanguagesCorrectly()
    {
        Person p = PersonMaker.makePerson(new AttributeList(new int[] { 10, 10, 18, 10, 10, 10 }));
        Person p2 = PersonMaker.makePerson(new AttributeList(new int[] { 14, 10, 11, 10, 10, 10 }));
        assertTrue("Greater INT should equate to more languages learnable", p.getMaxLangs() > p2.getMaxLangs());
    }

    @Test
    public void itDeterminesWisdomModifiersProperly()
    {
        Person p = PersonMaker.makePerson(new AttributeList(new int[] { 10, 10, 12, 14, 10, 10 }));
        Person p2 = PersonMaker.makePerson(new AttributeList(new int[] { 14, 10, 11, 10, 10, 10 }));
        assertTrue("Greater WIS shoudl increase Magic Attack Mod", p.getMagicAttackMod() > p2.getMagicAttackMod());
    }

    @Test
    public void itDeterminesConstitutionModifiersProperly()
    {
        Person p = PersonMaker.makePerson(new AttributeList(new int[] { 10, 10, 12, 14, 16, 10 }));
        Person p2 = PersonMaker.makePerson(new AttributeList(new int[] { 14, 10, 11, 10, 12, 10 }));
        assertTrue("Greater CON shoudl increase HP Mod", p.getHitPointAdj() > p2.getHitPointAdj());
    }

    @Test
    public void itCalculatesDexterityModifiersProperly()
    {
        Person p = PersonMaker.makePerson(new AttributeList(new int[] { 10, 15, 12, 14, 16, 10 }));
        Person p2 = PersonMaker.makePerson(new AttributeList(new int[] { 14, 12, 11, 10, 12, 10 }));
        assertTrue("Greater DEX shoudl increase AC Mod", p.getACMod() > p2.getACMod());
        assertTrue("Greater DEX shoudl increase To Hit Missle Mod", p.getToHitMissileMod() > p2.getToHitMissileMod());
    }

    @Test
    public void itCalculatesSatietyPointsCorrectly()
    {
        Person p = PersonMaker.makePerson("MName", Race.MALE);
        Person p2 = PersonMaker.makePerson("FName", Race.FEMALE);
        assertThatSatietyPointsAreRight(p);
        assertThatSatietyPointsAreRight(p2);
    }

    private void assertThatSatietyPointsAreRight(Person person)
    {
        assertEquals("Satiety is determined by weight only, initialized to full",
                person.getWeight() * 15, (int) person.getCurSatiety());
    }

    @Test
    public void itStartsAHeroWithNoExperience()
    {
        Person p = PersonMaker.makePerson();
        assertEquals("Hero should start at level 0", 0, p.getLevel());
        assertEquals("Hero should start with 0 experience", 0, p.getXP());
    }

    @Test
    public void itAssignsRightStartingHP()
    {
        Person p = PersonMaker.makePerson();
        assertEquals("Hero HP should be equal to 10 + HP Mod", 10 + p.getHitPointAdj(), p.getHP());
    }

    @Test
    public void itAssignsTheCorrectStartingMoney()
    {
        Person p = PersonMaker.makePerson();
        assertEquals("Hero starts with 15 gold", 15, p.getInventory().getItem("Gold pieces"));
        assertEquals("Hero starts with 8 silver", 8, p.getInventory().getItem("Silver pieces"));
    }

    @Test
    public void itCreatesTheCorrectStartingInventory()
    {
        Person p = PersonMaker.makePerson();
        assertHasStartingInventory(p);
    }

    private void assertHasStartingInventory(Person p)
    {
        String[] startList = {
                "Gold pieces", "Silver pieces", "Backpack", "Cloak", "Belt",
                "Belt pouch, small", "Breeches", "Pair of Boots", "Shirt",
                "Tinderbox, Flint & Steel", "Torches", "Rations", "Water skein", "Quarterstaff" };
        for (String s : startList) {
            assertTrue("Starting inventory should contain " + s, p.getInventory().hasItem(s));
        }
    }

    @Test
    public void itCalculatesActionPointsProperly()
    {
        Person p = PersonMaker.makePerson();
        assertThatAPAreCorrect(p);
    }

    private void assertThatAPAreCorrect(Person p)
    {
        AttributeList list = p.getTraits();
        assertEquals("Action Points equal STR + DEX",
                list.get(ATTRIBUTE.STR) + list.get(ATTRIBUTE.DEX), p.getAP());
    }

    @Test
    public void itCalculatesArmorClassProperly()
    {
        Person p = PersonMaker.makePerson();
        assertEquals("Hero AC should be equal to 10 + DEX Mod", 10 + p.getACMod(), p.getAC());
    }

    @Test
    public void itCalculatesBaseMovementCorrectly()
    {
        Person p = PersonMaker.makePerson();
        assertBaseMovementIsCorrect(p);

    }

    private void assertBaseMovementIsCorrect(Person p)
    {
        int ap = p.getAP();
        int baseSpd = calcBaseSpd(ap);
        int spdAj = calcSpdAdj(p.getHeight());
        assertEquals("Speed should be determined by height and AP", baseSpd + spdAj, (int) p.calcSpeed());
    }

    private int calcSpdAdj(int height)
    {
        if (height > 78) {
            return 1;
        } else if (height < 48) {
            return -1;
        } else {
            return 0;
        }
    }

    private int calcBaseSpd(int ap)
    {
        int expectedSpd;
        if (ap < 16) {
            expectedSpd = 2;
        } else if (ap < 24) {
            expectedSpd = 3;
        } else if (ap < 33) {
            expectedSpd = 4;
        } else {
            expectedSpd = 5;
        }
        return expectedSpd;
    }

    @Test
    public void itDeterminesCorrectEncumbrance()
    {
        Person p = PersonMaker.makePerson();
        assertEquals("Encumbrance should start at 0", 0, (int) p.calcEncumberance());
    }

    @Test
    public void itDeterminesNonLethalFightingSkillsCorrectly()
    {
        Assert.fail("These values are not yet included in Person");
    }

    @Test
    public void itAddsTheOccupationalSkill()
    {
      Occupation occ = ((OccupationRegistry) RegistryFactory.getRegistry(RegKey.OCP)).getOccupation("Fisher");
//      Occupation occ = ((OccupationRegistry) AdvRegistryFactory.getRegistry(RegKey.OCP)).getOccupation("Fisher");
        Person p = PersonMaker.makePerson(occ);
        assertTrue(p.getSkills().contains(occ));
    }
    
    @Test
    public void itAddsTheRacialSkills() {
        Person p = PersonMaker.makePerson("MName", Race.MALE, "Elf");
        Person p2 = PersonMaker.makePerson("MName", Race.MALE, "Human");
        assertPersonHasElfSkills(p);
        assertPersonHasHumanSkills(p2);
    }

    private void assertPersonHasHumanSkills(Person p2)
    {
        assertTrue("Human only has 1 skill", p2.getSkills().size() == 1);
    }

    private void assertPersonHasElfSkills(Person p)
    {
        String[] elfSkills = { "Infravision", "Resistance to Sleep",
        "Resistance to Charm", "Archery", "Tingling", "Move Silently"};
        List<Skill> skills = p.getSkills();
        for (String s : elfSkills) {
          Skill skl = ((SkillRegistry) RegistryFactory.getRegistry(RegKey.SKILL)).getSkill(s);
//          Skill skl = ((SkillRegistry) AdvRegistryFactory.getRegistry(RegKey.SKILL)).getSkill(s);
            assertTrue(skills.contains(skl));
        }
        // TODO Auto-generated method stub
    }
};
