/**
 * HeroRegistry.java Copyright (c) 2009, Carolla Development, Inc. All Rights Reserved
 * 
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists, requires
 * prior specific permission and/or a fee. Request permission to use from Carolla Development, Inc.
 * by email: acline@carolla.com
 */


package chronos.pdc.registry;

import java.util.List;

import mylib.ApplicationException;
import mylib.pdc.Registry;
import chronos.Chronos;
import chronos.pdc.character.Hero;

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

  /**
   * Retrieves all Heroes in the HeroRegistry
   * 
   * @return a list of Heroes
   */
  public List<Hero> getHeroList()
  {
    return getAll();
  }

  
  /** Save a Hero into the HeroRegistry */
  public boolean save(Hero h)
  {
    return add(h);
  }
} // end of HeroRegistry class

