/**
 * HeroRegistry.java Copyright (c) 2009, Carolla Development, Inc. All Rights Reserved
 * 
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists, requires
 * prior specific permission and/or a fee. Request permission to use from Carolla Development, Inc.
 * by email: acline@carolla.com
 */


package chronos.pdc.registry;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import chronos.Chronos;
import mylib.ApplicationException;
import mylib.dmc.IRegistryElement;
import mylib.pdc.Registry;

/**
 * Contains all Heros in the game. {@code HeroRegistry} is a singleton and is only
 * initialized once.
 * 
 * @author Tim Armstrong
 * @version Mar 13, 2013 // original <br>
 */
public class HeroRegistry extends Registry
{


  /*
   * CONSTRUCTOR(S) AND RELATED METHODS
   */

  /**
   * Init this Hero Registry
   * 
   * @throws IOException if the Registry is not on the disk
   */
  protected HeroRegistry() throws IOException
  {
    super(Chronos.PersonRegPath);
  }


  /**
   * Create the Hero Registry with the tables given (none), converting each element to a Hero
   * object and saving it in the database.
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
  public IRegistryElement getHero(final String name)
  {
    // ExtObjectContainer db = _regRW.getDB();
    // Retrieve all skills that match the skillname; should be only one
    // List<Hero> list = db.query(new HeroPredicate(name));

    List<IRegistryElement> elist = get(name);

    // Ensure uniqueness
    if (elist.size() == 1) {
      return elist.get(0);
    } else {
      return null;
    }
  }

  // TODO: Do we really need this?
//  /**
//   * Retrieve multiple Heros
//   * 
//   * @param target the object to find in the database
//   * @return the Hero List
//   */
//  public IRegistryElement> getHeroList(Hero target)
//  {
//    List<IRegistryElement> HeroSet = getAll();
//    ArrayList<Hero> HeroList = new ArrayList<Hero>(HeroSet.size());
//    for (Object o : HeroSet) {
//      HeroList.add((Hero) o);
//    }
//    return HeroList;
//  }


  /*
   * PRIVATE METHODS
   */


//  @SuppressWarnings("serial")
//  private final class HeroPredicate extends Predicate<Hero>
//  {
//    private final String name;
//
//    private HeroPredicate(String name)
//    {
//      this.name = name;
//    }
//
//    public boolean match(Hero candidate)
//    {
//      return candidate.getName().equals(name);
//    }
//  }

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


  /*
   * ++++++++++++++++++++++++++++++++++++++++++++++++++++++ INNER CLASS: MockHeroRegistry for
   * Testing ++++++++++++++++++++++++++++++++++++++++++++++++++++++
   */

  /** Inner class for testing Hero */
  public class MockHeroRegistry
  {
    /** Default constructor */
    public MockHeroRegistry()
    {}
    //
    // /** Total number of objects expected to be stored in the db */
    // public long getOverhead()
    // {
    // return HeroRegistry.this.getOverhead();
    // }

  } // end of MockHeroRegistry inner class


  // /** Close db, destroy the dbReadWriter and set this registry to null
  // * @param eraseFile if true, erase registry file; else not
  // */
  // public void closeRegistry()
  // {
  // super.close();
  // // _thisReg = null;
  // }
  //
  // public void deleteRegistry()
  // {
  // super.delete();
  // // _thisReg = null;
  // }

} // end of HeroRegistry class

