package chronos.test.pdc.character;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.Before;
import org.junit.Test;

import chronos.pdc.character.Klass;
import chronos.pdc.character.TraitList;
import chronos.pdc.character.TraitList.PrimeTraits;

public class TestKlass
{
  TraitList _defTraits = new TraitList(new int[] {10,10,10,10,10,10});
  private Klass testKlass; 

  @Before
  public void setup()
  {
    testKlass = new Klass(_defTraits, "name", PrimeTraits.STR, "5d1", "10d1");
  }
  
  @Test
  public void klassesAreConstructedByName()
  {
    for (String klassName : Klass.KLASS_LIST) {
      assertEquals(klassName, Klass.createKlass(klassName, null).className());
    }
  }

  @Test
  public void rollGoldReturnsTenTimesDieRoll()
  {
    Klass testKlass = new Klass(_defTraits, "name", PrimeTraits.STR, "d1", "10d1");
    assertEquals(100, testKlass.rollGold());
  }
  
  @Test
  public void rollGoldExactDieRoll()
  {
    assertEquals(5, testKlass.rollHP());
  }
 
  @Test
  public void byDefaultHeroCannotUseMagic()
  {
    assertFalse(testKlass.canUseMagic());
  }
}
