
package test.pdc.character;

import org.junit.Test;

import test.integ.IntegrationTest;
import chronos.pdc.character.Hero;

public class SampleHeroGenerator extends IntegrationTest
{

  @Test
  public void generateAHero()
  {
    Hero h = null;
    for (int i = 0; i < 10; i++) {
      h = new Hero("Yoggoth", "female", "White", "Elf");
      System.out.println(h);
    }
    System.out.println("\n");
    
    for (int i = 0; i < 10; i++) {
      h = new Hero("Perrin", "male", "Orange", "Gnome");
      System.out.println(h);
    }
    System.out.println("\n");

    for (int i = 0; i < 10; i++) {
      h = new Hero("Sharmar", "female", "black", "Half-Orc");
      System.out.println(h);
    }
    System.out.println("\n");
    
    for (int i = 0; i < 10; i++) {
      h = new Hero("Swee", "male", "blue", "Hobbit");
      System.out.println(h);
    }
    System.out.println("\n");
    
    for (int i = 0; i < 10; i++) {
      h = new Hero("Poppal", "female", "blonde", "Human");
      System.out.println(h);
    }
    System.out.println("\n");
    
    for (int i = 0; i < 10; i++) {
      h = new Hero("Denaeri", "male", "silver", "Half-Elf");
      System.out.println(h);
    }
    System.out.println("\n");
    
    for (int i = 0; i < 10; i++) {
      h = new Hero("Shmer", "male", "brown", "Dwarf");
      System.out.println(h);
    }
  }
}
