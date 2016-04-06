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

  /**
   * Default constructor
   */
  public HeroRegistry()
  {
    super(Chronos.PersonRegPath);
<<<<<<< HEAD
    if (_shouldInitialize) {
      initialize();
    }
  }


  /**
   * Loads the in-memory HeroRegistry from the database.
   */
  @Override
  public void initialize()
  { 
    _db = new DbReadWriter<Hero>(Chronos.HeroRegPath);
    _regRW = _db.getAll();
=======
>>>>>>> 18c7205744d12480b19e6fc1a43bbbcd874a2815
  }

  // ========================================================
  // PUBLIC METHODS
  // ========================================================

  /**
   * Save new Hero, both to db and Registry
   */
  @Override
  public boolean add(Hero hero)
  {
    boolean retval = super.add(hero);
    try {
      _db.addElement(hero);
    } catch (Exception ex) {
      // TODO Auto-generated catch block
      ex.printStackTrace();
    }
    return retval;
  }


  /**
   * Close the HeroRegistry, which closes the database
   */
  public void close()
  {
    _db.close();
  }

  
  
  /**
   * Verifies if the given object exists in the database. The object's equal() method is called.
   * 
   * @param target object to match against for comparison
   * @return true if the registry contains the element, else false
   */
  public boolean dbContains(Hero target)
  {
    return (_db.containsElement(target)) ? true : false;
  }


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
  public List<String> getNamePlates()
  {
    @SuppressWarnings("serial")
    List<Hero> heroList = _regRW.query(new Predicate<Hero>() {
      @Override
      public boolean match(Hero h)
      {
        return true;
      }

    });
    List<String> plateList = new ArrayList<String>(heroList.size());
    for (Hero h : heroList) {
      plateList.add(h.toNamePlate());
    }
    return plateList;
  }

<<<<<<< HEAD

  

  
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
    
    /** Clear the database and the memory list */
    public void clear()
    {
      _db.clear();
      _regRW.clear();
    }
    
  } // end of MockHeroRegistry inner class
  
=======
>>>>>>> 18c7205744d12480b19e6fc1a43bbbcd874a2815
} // end of HeroRegistry class

