package chronos.pdc.race;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class RangedValueTest
{

  private RangedValue evenRangedValue;

  @Before
  public void setup()
  {
    evenRangedValue = new RangedValue(10, "d10");
  }
  
  @Test
  public void resultsCenterAroundAverage()
  {
    int total = 0;
    for (int i = 0; i < 5000; i++) {
      total += evenRangedValue.calcValue(); 
    }
    
    assertEquals(50000, total, 1250);
  }

}
