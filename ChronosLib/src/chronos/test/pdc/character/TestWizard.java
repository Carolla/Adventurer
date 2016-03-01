
package chronos.test.pdc.character;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import chronos.pdc.character.Inventory;
import chronos.pdc.character.TraitList;
import chronos.pdc.character.Wizard;

public class TestWizard
{
  TraitList defTraits = new TraitList(new int[] {10, 10, 10, 10, 10, 10});
  private Wizard w;

  @Before
  public void setup()
  {
    w = new Wizard(defTraits);
  }

  @Test
  public void wizardGets20to80Gold()
  {
    int minGold = 80;
    int maxGold = 20;
    for (int i = 0; i < 10000; i++) {
      int gold = w.rollGold();
      if (gold > maxGold) {
        maxGold = gold;
      } else if (gold < minGold) {
        minGold = gold;
      }
    }
    assertEquals(80, maxGold);
    assertEquals(20, minGold);
  }

  @Test
  public void wizardHas1to4Hp()
  {
    Wizard w = new Wizard(defTraits);
    int maxHp = 0;
    int minHp = 10;
    for (int i = 0; i < 10000; i++) {
      int hp = w.rollHP();
      if (hp > maxHp) {
        maxHp = hp;
      } else if (hp < minHp) {
        minHp = hp;
      }
    }
    assertEquals(4, maxHp);
    assertEquals(1, minHp);
  }

  @Test
  public void canUseMagic()
  {
    assertTrue(w.canUseMagic());
  }

  @Test
  public void hasSpells()
  {
    w.addKlassSpells();
    assertTrue(w.getSpells().size() > 0);
  }

  @Test
  public void hasKlassItems()
  {
    Inventory inventory = new Inventory();
    w.addKlassItems(inventory);
    assertTrue(inventory.size() > 0);
  }
}
