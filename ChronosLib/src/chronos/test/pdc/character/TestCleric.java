
package chronos.test.pdc.character;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import chronos.pdc.character.Cleric;
import chronos.pdc.character.TraitList;

public class TestCleric
{

  TraitList defTraits = new TraitList(new int[] {10, 10, 10, 10, 10, 10});
  private Cleric c;

  @Before
  public void setup()
  {
    c = new Cleric(defTraits);
  }

  @Test
  public void clericGets30to180Gold()
  {
    int minGold = 180;
    int maxGold = 30;
    for (int i = 0; i < 10000; i++) {
      int gold = c.rollGold();
      if (gold > maxGold) {
        maxGold = gold;
      } else if (gold < minGold) {
        minGold = gold;
      }
    }
    assertEquals(180, maxGold);
    assertEquals(30, minGold);
  }

  @Test
  public void clericHas1to8Hp()
  {
    int maxHp = 0;
    int minHp = 10;
    for (int i = 0; i < 10000; i++) {
      int hp = c.rollHP();
      if (hp > maxHp) {
        maxHp = hp;
      } else if (hp < minHp) {
        minHp = hp;
      }
    }
    assertEquals(8, maxHp);
    assertEquals(1, minHp);
  }

  @Test
  public void canUseMagic()
  {
    assertTrue(c.canUseMagic());
  }
  
  @Test
  public void hasSpells()
  {
    c.addKlassSpells();
    assertTrue(c.getSpells().size() > 0);
  }
}
