
package test.pdc.character;

import org.junit.Test;

import test.integ.IntegrationTest;
import chronos.pdc.character.Hero;

public class SampleHeroGenerator extends IntegrationTest
{

  @Test
  public void generateAHero()
  {
    for (int i = 0; i < 10; i++) {
      Hero h = new Hero("Yoggoth", "female", "White", "Elf", "Cleric");
      System.out.println(h);
      h = new Hero("Perrin", "male", "Orange", "Gnome", "Thief");
      System.out.println(h);
      h = new Hero("Sharmar", "female", "black", "Half-Orc", "Fighter");
      System.out.println(h);
      h = new Hero("Swee", "male", "blue", "Hobbit", "Wizard");
      System.out.println(h);
      h = new Hero("Poppal", "female", "blonde", "Human", "Fighter");
      System.out.println(h);
      h = new Hero("Denaeri", "male", "silver", "Half-Elf", "Cleric");
      System.out.println(h);
      h = new Hero("Shmer", "male", "brown", "Dwarf", "Wizard");
      System.out.println(h);
      System.out.println("\n");
    }
  }
}
