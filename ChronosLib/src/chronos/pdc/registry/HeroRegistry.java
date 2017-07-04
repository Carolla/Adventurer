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
import chronos.pdc.Chronos;
import chronos.pdc.character.Hero;
import chronos.test.pdc.ConcreteRegistry;

import com.db4o.query.Predicate;

/**
 * Contains all Heros in the game. This is the only {@code Registry} that currently uses
 * {@code db4o} for persistence; the other Registries are in-memory copies and must be initialized
 * each time.
 * 
 * @author Tim Armstrong
 * @version Mar 13, 2013 // original <br>
 *          Dec 9 2015 // added a few interfacing methods to Registry <br>
 *          Dec 25 2015 // ABC added GetAllHeroes() <br>
 *          Mar 25 2016 // ABC Added getNamplares() <br>
 *          Mar 28 2016 // ABC Extended HeroRegistry to use DbReadWriter as subclass override <br>
 */
public class HeroRegistry extends ConcreteRegistry<Hero>
{
  @SuppressWarnings("serial")
  private static final Predicate<Hero> _heroQuery = new Predicate<Hero>() {
    @Override
    public boolean match(Hero h)
    {
      return true;
    }
  };

  /**
   * Default constructor
   */
  public HeroRegistry()
  {
    super(Chronos.PersonRegPath);
  }

  // ========================================================
  // PUBLIC METHODS
  // ========================================================

  /**
   * Retrieves the Hero with the requested unique name
   * 
   * @param name name of the Hero to retrieve
   * @return the Hero object; or null if not unique
   * @throws ApplicationException if trying to retrieve non-unique object
   */
  public Hero getHero(String name)
  {
    return super.get(name);
  }

  @Override
  public List<Hero> getAll()
  {
    return _regRW.query(_heroQuery);
  }
  
}   // end of HeroRegistry class

