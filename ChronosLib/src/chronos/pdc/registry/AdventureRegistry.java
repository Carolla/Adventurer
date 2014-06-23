/**
 * AdventureRegistry.java Copyright (c) 2009, Carolla Development, Inc. All Rights Reserved
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
import mylib.dmc.IRegistryElement;
import mylib.pdc.Registry;
import chronos.Chronos;
import chronos.pdc.Adventure;

import com.db4o.query.Predicate;

/**
 * Contains a set of Adventure objects, where each Adventure contains the name of a Town and an
 * Arenda
 * 
 * @author Alan Cline
 * @version Jan 1 2010 // original
 */
public class AdventureRegistry extends Registry
{
  /** Default Adventure */
  private final String DEF_ADVENTURE = "The Quest for Rogahn and Zelligar";
  /** Default Town to start AdventureRegistry with */
  private final String DEF_TOWN = "Biljur'Baz";
  /** Default Arena to start AdventureRegistry with */
  private final String DEF_ARENA = "Quasqueton";
  /** Overview for Quasqueton Adventure */
  private final String DEF_OVERVIEW =
      "This is the domain of Rogahn the mighty warrior, and Zelligar the powerful mage. "
          + "Together they ovethrew evil and made this frontier land safe for its humble and grateful "
          + " inhabitants. \n\n"
          + "About a year ago, Rogahn and Zelligar no longer frequented the town. "
          + "At first the good people of Biljur'Baz, the local community, thought R & Z were on one of "
          + "their many quests that they undertook from time to time.\n\n "
          + "Recently, trade caravans have been attacked, villages looted, and merchants disappearing. "
          + "Evil has taken up residence in castle Quasqueton, Rogahn and Zelligar's home "
          + "and fortress. What once was the home of safety and protection is now headquarters for "
          + "evil people and dark creatures.\n\n"
          + "The local sheriff investigated two months ago, but has never returned from his visit to the Q. "
          + "The local guilds are offering rewards and fame to adventurers who are brave enough to "
          + "investigate and pull back the veil of this mystery.\n\n "
          + "Of course, any loot that can be found or taken is always subject to the Common Law of "
          + "Salvage: "
          + "\n\n\tWhat can be carried away, can be kept.\n"
          + "\nWhether a Guild member or a peasant, an adventurous soul, well equipped with "
          + "bravery, determination, a stout heart, and the right weapons, can make a name for him "
          + "or herself, achieve fame and glory and wealth, and once again allow safety and "
          + "goodness to prevail in this land.\n";


  /*
   * CONSTRUCTOR(S) AND RELATED METHODS 
   */

  protected AdventureRegistry()
  {
    super(Chronos.AdventureRegPath);
  }


  // protected AdventureRegistry getInstance()
  // {
  // AdventureRegistry reg = (AdventureRegistry) new AdventureRegistry();
  // return reg;
  // }


  /**
   * Return the Registry if there is one.
   * 
   * @return a AdventureRegistry singleton
   */
  // static private AdventureRegistry getInstance()
  // {
  // if (_thisReg == null) {
  // _thisReg = new AdventureRegistry();
  // }
  // return _thisReg;
  // }

  /**
   * Create the default Adventure Registry, containing only a single Adventure it is called by the
   * base <code>Registry</code> class
   */
  @Override
  protected void initialize()
  {
    // Load the default data into this Adventure
    Adventure adv = new Adventure(DEF_ADVENTURE, DEF_TOWN, DEF_ARENA, DEF_OVERVIEW);
    // Do not use _thisReg, it is not created yet. You are still in the constructor
    super.add((IRegistryElement) adv);

  }


  /**
   * ++++++++++++++++++++++++++++++++++++++++++++++++++++++ PUBLIC METHODS
   * ++++++++++++++++++++++++++++++++++++++++++++++++++++++
   */

  // /** Close db, destroy the dbReadWriter and set this registry to null
  // * @param eraseFile if true, erase registry file; else not
  // */
  // public void closeRegistry()
  // {
  // super.close();
  // // _thisReg = null;
  // }
  //
  //
  // /** Deletes the registry file in addition to the normal closing
  // * @param eraseFile if true, erase registry file; else not
  // */
  // public void deleteRegistry()
  // {
  // super.delete();
  // // _thisReg = null;
  // }


  /**
   * Get a particlar Adventure by name
   * 
   * @param name of the desired Adventure
   * @return the Item
   */
  public Adventure getAdventure(String name)
  {
    try {
      return (Adventure) getUnique(name);
    } catch (ApplicationException ex) {
      return null;
    }
  }


  /**
   * Retrieve all Adventures in the Registry
   * 
   * @return the adventure List
   */
  public ArrayList<Adventure> getAdventureList()
  {
    List<IRegistryElement> advSet = get(new Predicate<IRegistryElement>() {
      public boolean match(IRegistryElement candidate)
      {
        return true;
      }
    });
    ArrayList<Adventure> advList = new ArrayList<Adventure>(advSet.size());
    for (IRegistryElement e : advSet) {
      advList.add((Adventure) e);
    }
    return advList;
  }


  /*
   * ++++++++++++++++++++++++++++++++++++++++++++++++++++++ INNER CLASS: MockAdventureRegistry for
   * Testing ++++++++++++++++++++++++++++++++++++++++++++++++++++++
   */

  /** Inner class for testing Person */
  public class MockAdventureRegistry
  {
    /** Default constructor */
    public MockAdventureRegistry()
    {}


    /** Diagnostic to dump all skills in the Registry */
    public void dump()
    {}

    /** Return the path for the registry file */
    public String getPath()
    {
      return Chronos.AdventureRegPath;
    }

    /** Return the path for the registry file */
    public void setPath(String testPath)
    {
      Chronos.AdventureRegPath = testPath;
    }

    // /** Get the Registry object */
    // public AdventureRegistry getRegistry()
    // {
    // return (AdventureRegistry) _thisReg;
    // }

  } // end of MockAdventureRegistry class


} // end of AdventureRegistry class

