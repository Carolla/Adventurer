
package chronos.test.pdc.character;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import chronos.pdc.character.Inventory;
import chronos.pdc.character.Thief;
import chronos.pdc.character.TraitList;

public class TestThief
{
  TraitList defTraits = new TraitList(new int[] {10, 10, 10, 10, 10, 10});
  private Thief t;
  @Before
  public void setup()
  {
    t = new Thief(defTraits);
  }

  @Test
  public void thiefGets20to120Gold()
  {
    int minGold = 120;
    int maxGold = 20;
    for (int i = 0; i < 10000; i++) {
      int gold = t.rollGold();
      if (gold > maxGold) {
        maxGold = gold;
      } else if (gold < minGold) {
        minGold = gold;
      }
    }
    assertEquals(120, maxGold);
    assertEquals(20, minGold);
  }
  
  @Test
  public void thiefHas1to6Hp()
  {
    int maxHp = 0;
    int minHp = 10;
    for (int i = 0; i < 10000; i++) {
      int hp = t.rollHP();
      if (hp > maxHp) {
        maxHp = hp;
      } else if (hp < minHp) {
        minHp = hp;
      }
    }
    assertEquals(6, maxHp);
    assertEquals(1, minHp);
  }
  
  @Test
  public void hasKlassItems()
  {
    Inventory inventory = new Inventory();
    t.addKlassItems(inventory);
    assertTrue(inventory.size() > 0);
  }
}
