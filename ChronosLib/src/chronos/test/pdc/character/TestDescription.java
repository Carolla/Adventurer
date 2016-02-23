package chronos.test.pdc.character;

import static org.junit.Assert.*;

import org.junit.Test;

import chronos.pdc.character.Description;

public class TestDescription
{

  @Test
  public void beginsWithVowelFindsVowels()
  {
    assertTrue(Description.beginsWithVowel("a"));
    assertTrue(Description.beginsWithVowel("Easdf"));
    assertFalse(Description.beginsWithVowel("bba"));;
    assertFalse(Description.beginsWithVowel("Cbba"));
  }

  @Test
  public void articleGetsAandAnRight()
  {
    assertEquals("A ", Description.article("Friend"));
    assertEquals("An ", Description.article("Enemy"));
  }
}
