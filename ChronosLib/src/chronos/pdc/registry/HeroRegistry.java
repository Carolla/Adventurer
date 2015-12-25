/**
 * HeroRegistry.java Copyright (c) 2009, Carolla Development, Inc. All Rights Reserved
 * 
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists, requires
 * prior specific permission and/or a fee. Request permission to use from Carolla Development, Inc.
 * by email: acline@carolla.com
 */


package chronos.pdc.registry;

import java.util.ArrayList;
import java.util.List;

import chronos.Chronos;
import chronos.pdc.character.Hero;
import mylib.ApplicationException;
import mylib.dmc.IRegistryElement;
import mylib.pdc.Registry;

/**
 * Contains all Heros in the game.
 * 
 * @author Tim Armstrong
 * @version Mar 13, 2013 // original <br>
 *          Dec 9 2015 // added a few interfacing methods to Registry <br>
 *          Dec 25 2015 // ABC: added GetAllHeroes() <br>
 */
public class HeroRegistry extends Registry<Hero>
{
  /*
   * CONSTRUCTOR(S) AND RELATED METHODS
   */

  /**
   * Init this Hero Registry
   */
  public HeroRegistry()
  {
    super(Chronos.PersonRegPath);
  }


  /**
   * Create the Hero Registry with the tables given (none), converting each element to a Hero object
   * and saving it in the database.
   */
  @Override
  public void initialize()
  {
    // No default Heroes in HeroRegistry
  }


  /*
   * PUBLIC METHODS
   */

  /**
   * Retrieves the Hero with the requested unique name
   * 
   * @param name name of the Hero to retrieve
   * @return the Hero object; or null if not unique
   * @throws ApplicationException if trying to retrieve non-unique object
   */
  public Hero getHero(String name)
  {
    return (Hero) get(name);
  }


  // TODO Move all all the list casting methods like this one into base class Registry
  /**
   * Retrieves all Heroes in the HeroRegistry
   * 
   * @return a list of Heroes
   */
  public List<Hero> getHeroList()
  {
    List<IRegistryElement> results = getAll();
    // Convert to Hero type
    List<Hero> heroList = new ArrayList<Hero>(results.size());
    for (IRegistryElement elem : results) {
      heroList.add((Hero) elem);
    }
    return heroList;
  }

  
  /** Save a Hero into the HeroRegistry */
  public boolean save(Hero h)
  {
    return add(h);
  }


  /*
   * PRIVATE METHODS
   */


  // @SuppressWarnings("serial")
  // private final class HeroPredicate extends Predicate<Hero>
  // {
  // private final String name;
  //
  // private HeroPredicate(String name)
  // {
  // this.name = name;
  // }
  //
  // public boolean match(Hero candidate)
  // {
  // return candidate.getName().equals(name);
  // }
  // }

  // /** Load a table of Heros into the HeroRegistry
  // *
  // * @param table the initial Heros to load
  // * @return false if a problem occurs, else true
  // *@throw ApplicationException if the Hero could not be added to the db
  // */
  // private void loadTable(String[][] table) throws ApplicationException
  // {
  // // Save the Heros required for the new Hero's inventory
  // for (int k = 0; k < table.length; k++) {
  // HeroCategory cat = HeroCategory.valueOf(table[k][0]);
  // String name = table[k][1];
  // int weight = Integer.valueOf(table[k][2]);
  // int qty = Integer.valueOf(table[k][3]);
  // Hero Hero = new Hero(cat, name, weight, qty);
  // if (super.add(Hero) == false) {
  // throw new ApplicationException("loadTable() error while adding to db " +
  // Hero.getName());
  // }
  // }
  // }


} // end of HeroRegistry class

