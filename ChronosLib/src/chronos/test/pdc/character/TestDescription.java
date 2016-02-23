
package chronos.test.pdc.character;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import chronos.pdc.character.Description;
import chronos.pdc.character.Gender;

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

  @Test
  public void baldIsBald()
  {
    Description desc = new Description(0, "", "bald", new Gender("Female"), 0, 0);
    assertTrue(desc.hairDescription().contains("bald"));
  }

  @Test
  public void hairColorIsDescribed()
  {
    String[] hairColors = {"red", "orange", "Blue", "Green"};
    for (String color : hairColors) {
      Description desc = new Description(0, "", color, new Gender("Female"), 0, 0);
      assertTrue(desc.hairDescription().contains(color));
    }
  }
  
  @Test
  public void twoIdenticalDescriptionsAreEqual()
  {
    Description desc1 = new Description(10, "red-eyed ", "red", new Gender("Female"), 50, 160);
    Description desc2 = new Description(10, "red-eyed ", "red", new Gender("Female"), 50, 160);
    
    assertEquals(desc1, desc2);
  }
}
