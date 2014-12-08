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
 * Arena. {@code AdventureRegistry} is called by reflection from the {@code Registry.createRegistry}
 * base class method.
 * 
 * @author Alan Cline
 * @version Jan 1 2010 // original <br>
 *          Jun 28 2014 // make available only through Class reflection call; some cleanup <br>
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
      "Welcome to BilJur'Baz, a small village nestled in the shadow of the great castle and fortress "
          + "Quasqueton, the domain of Rogahn the mighty warrior, and Zelligar the powerful mage. "
          + "Together they ovethrew evil and made this frontier land safe for its "
          + "humble and grateful inhabitants. \n\n"
          + "About a year ago, Rogahn and Zelligar no longer frequented this quiet little town. "
          + "No longer did the comforting presence of Rogahn laugh at the Inn, no longer did the "
          + "mysterious Zelligar patrol the streets. "
          + "At first the good people of Biljur'Baz thought R & Z were continuing construction on "
          + "their castle, but then as time went on, the people thought that their great protectors "
          + "were on quest, which they undertook from time to time.\n\n"
          + "Recently, trade caravans have been attacked, villages looted, and merchants have "
          + "disappeared. Evil has taken up residence in castle Quasqueton. What once was the home "
          + "of safety and protection is now headquarters for dark creatures and evil people.\n\n"
          + "The local sheriff investigated two months ago, but has never returned from his visit to "
          + "the Q. The local Guilds are offering rewards and fame to adventurers who are brave "
          + "enough to investigate and pull back this veil of mystery.\n\n"
          + "Of course, any loot that can be found or taken is always subject to the Common Law of "
          + "Salvage:\n\n"
          + "      What can be carried away, can be kept.\n\n"
          + "Any adventurous soul, well equipped with bravery, determination, a stout heart, and the "
          + "right weapons, can make a name for him or herself, achieve fame and glory and wealth, "
          + "and once again allow safety and goodness to prevail in this land.\n\n"
          + "If you think you are up to the challenge, you can enter and explore the great castle "
          + "Quasqueton and drive out the villains.\n\n"
          + "But first! Visit the buildings in this town, and prepare yourself: \n"
          + "Collect information from the people at the Inn, purchase equipment and weapons from the "
          + "General Store, perhaps even borrow money or make a will from the Bank. "
          + "Don't get in trouble, we also have a Jail.\n\n"
          + "Count your assets before beginning your Quest. You should find more here than a "
          + "holocaust cloak.\n\n";


  // ===========================================================================
  // CONSTRUCTOR(S) AND RELATED METHODS
  // ===========================================================================

  /**
   * This method is a wrapper to the base class which passes the data file path and not the source code
   */
  protected AdventureRegistry()
  {
    super(Chronos.AdventureRegPath);
  }


  /**
   * Create the default Adventure Registry, containing only a single Adventure. It is called by the
   * base {@code Registry} class
   */
  @Override
  protected void initialize()
  {
    // Load the default data into this Adventure
    Adventure adv = new Adventure(DEF_ADVENTURE, DEF_TOWN, DEF_ARENA, DEF_OVERVIEW);
    super.add((IRegistryElement) adv);
  }


  // ===========================================================================
  // PUBLIC METHODS
  // ===========================================================================


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

  // ===========================================================================
  // PRIVATE METHODS
  // ===========================================================================

  // ===========================================================================
  // INNER CLASS: MockAdventureRegistry for Testing
  // ===========================================================================

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

  } // end of MockAdventureRegistry class


} // end of AdventureRegistry class

