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

import chronos.pdc.Chronos;
import chronos.pdc.character.Hero;
import mylib.ApplicationException;
import mylib.dmc.DbReadWriter;
import mylib.pdc.Registry;

/**
 * Contains all Heros in the game. 
 * This is the only {@code Registry} that currently uses {@code db4o} for persistence;
 * the other Registries are in-memory copies and must be initialized each time.
 * 
 * @author Tim Armstrong
 * @version Mar 13, 2013 // original <br>
 *          Dec 9 2015 // added a few interfacing methods to Registry <br>
 *          Dec 25 2015 // ABC added GetAllHeroes() <br>
 *          Mar 25 2016 // ABC Added getNamplares() <br>
 *          Mar 28 2016 // ABC Extended HeroRegistry to use DbReadWriter as subclass override <br>
 */
public class HeroRegistry extends Registry<Hero>
{
  /** Requires an actual persistence database instead of in-memory List */
  private DbReadWriter<Hero> _db;
  
  
  /**
   * Default constructor
   */
  public HeroRegistry()
  {
    super(Chronos.PersonRegPath);
  }


  /**
   * Loads the in-memory HeroRegistry from the persistence database.
   */
  @Override
  public void initialize()
  { 
    _db = new DbReadWriter<Hero>(Chronos.HeroRegPath);
    _regRW = _db.getAll();
  }


  // ========================================================
  //  PUBLIC METHODS
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

  /**
   * Retrieves all Heroes in the HeroRegistry
   * 
   * @return a list of Heroes
   */
  @Override
  public List<Hero> getAll()
  {
    List<Hero> heroList = super.getAll();
    return heroList;
  }

  /**
   * Retrieves all Heroes in the HeroRegistry
   * 
   * @return a list of Heroes
   */
  public List<String> getNamePlates()
  {
    List<Hero> heroList = getAll();
    List<String> plateList = new ArrayList<String>(heroList.size());
    for (Hero h : heroList) {
      plateList.add(h.toNamePlate());
    }
    return plateList;
  }


  /**
   * Save new Hero, both to db and Registry
   */
  public void saveHero(Hero hero)
  {
    super.add(hero);
    _db.addElement(hero);
  }

  
  // ========================================================
  //  Inner Class: MockHeroRegistry
  // ========================================================

  public class MockHeroRegistry
  {
    
    public MockHeroRegistry(){}
    
    public DbReadWriter<Hero> getDb()
    {
      return HeroRegistry.this._db;
    }
    
    public List<Hero> getList()
    {
      return HeroRegistry.this._regRW;
    }
    
  }
  
} // end of HeroRegistry class

