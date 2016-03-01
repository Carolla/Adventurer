
package chronos.test.pdc.character;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import chronos.pdc.character.Fighter;
import chronos.pdc.character.Inventory;
import chronos.pdc.character.TraitList;

public class TestFighter
{

  TraitList defTraits = new TraitList(new int[] {10, 10, 10, 10, 10, 10});
  private Fighter f;

  @Before
  public void setup()
  {
    f = new Fighter(defTraits);
  }

  @Test
  public void figherGets50to200Gold()
  {
    int minGold = 200;
    int maxGold = 50;
    for (int i = 0; i < 10000; i++) {
      int gold = f.rollGold();
      if (gold > maxGold) {
        maxGold = gold;
      } else if (gold < minGold) {
        minGold = gold;
      }
    }
    assertEquals(200, maxGold);
    assertEquals(50, minGold);
  }

  @Test
  public void fighterHas1to10Hp()
  {
    Fighter f = new Fighter(defTraits);
    int maxHp = 0;
    int minHp = 10;
    for (int i = 0; i < 10000; i++) {
      int hp = f.rollHP();
      if (hp > maxHp) {
        maxHp = hp;
      } else if (hp < minHp) {
        minHp = hp;
      }
    }
    assertEquals(10, maxHp);
    assertEquals(1, minHp);
  }
  
  @Test
  public void cantUseMagic()
  {
    assertFalse(f.canUseMagic());
  }
  
  @Test
  public void hasSpells()
  {
    f.addKlassSpells();
    assertEquals(0, f.getSpells().size());
  }
  
  @Test
  public void hasKlassItems()
  {
    Inventory inventory = new Inventory();
    f.addKlassItems(inventory);
    assertTrue(inventory.size() > 0);
  }
}
