/**
 * HeroRegistry.java Copyright (c) 2009, Carolla Development, Inc. All Rights Reserved
 * 
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists, requires
 * prior specific permission and/or a fee. Request permission to use from Carolla Development, Inc.
 * by email: acline@carolla.com
 */


package chronos.pdc.registry;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.StreamCorruptedException;
import java.util.List;

import chronos.pdc.Chronos;
import chronos.pdc.character.Hero;
import mylib.ApplicationException;
import mylib.pdc.Registry;

/**
 * Manages a collection of Hero objects, although each Hero is saved into its own individual file.
 * This is the only {@code Registry} subclass that currently saves its data to a file. The other
 * Registries are read-only, in-memory copies that must be initialized each time.
 * 
 * @author Tim Armstrong, Alan Cline
 * @version Mar 13, 2013 // original <br>
 *          Dec 9 2015 // added a few interfacing methods to Registry <br>
 *          Dec 25 2015 // ABC added GetAllHeroes() <br>
 *          Mar 25 2016 // ABC Added getNamplares() <br>
 *          Mar 28 2016 // ABC Extended HeroRegistry to use DbReadWriter as subclass override <br>
 *          Aug 1, 2017 // replaced dbReadWriter with File; and updated per QATool <br>
 *          Aug 4, 2017 // solved from serialization problems <br>
 *          Aug 6, 2017 // moved from a collection of Hero objects to a collection of names plates
 *          for referencing the Hero files. <br>
 */
// public class HeroRegistry extends ConcreteRegistry<Hero>
public class HeroRegistry extends Registry<Hero>
{
  // Required for serialization
  static final long serialVersionUID = 1040L;

  /** Directory of where all the Heroes are stored; the HeroRegistry is not stored as a file */
  private String _dirname;
  /** Extension for all hero file names in the registry directory: "dormitory" */
  private final String HEROFILE_EXT = ".chr";

  // /** HeroRegistry is serialized to an object stream between sessions. */
  // private ObjectOutputStream _outStream = null;

  // /** This file is needed to allow calling objects to have access to file stats */
  // private File _regFile;


  // ========================================================
  // CONSTRUCTOR
  // ========================================================

  /**
   * Create a Registry to contain all the Heroes
   */
  public HeroRegistry()
  {
    super(Chronos.HeroRegPath);
    _dirname = Chronos.HeroRegPath;
  }


  // ========================================================
  // PUBLIC METHODS
  // ========================================================

  // /**
  // * Compare two registries, although only one should be instantiated. Required for serialization.
  // * The only field to compare is filename where the registry is stored.
  // *
  // * @param obj some other possible HeroRegistry to compare with
  // */
  // @Override
  // public boolean equals(Object obj)
  // {
  // if (this == obj)
  // return true;
  // if (obj == null)
  // return false;
  // if (getClass() != obj.getClass()) {
  // return false;
  // }
  // // HeroeRegistries are equal if directory name is same, and sizes are same
  // HeroRegistry other = (HeroRegistry) obj;
  // if (!_dirname.equals(other._dirname)) {
  // return false;
  // }
  // if (!(_list.size() == other._list.size())) {
  // return false;
  // }
  // return true;
  // }


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
    return super.getAll();
  }


  // /** Required for serialization */
  // @Override
  // public int hashCode()
  // {
  // final int prime = 31;
  // int result = 1;
  // result = prime * result + ((_dirname == null) ? 0 : _dirname.hashCode());
  // return result;
  // }


  /** Calls {@code initialize(String filename)} */
  @Override
  protected void initialize()
  {
    initialize(Chronos.HeroRegPath);
  }


  /**
   * Call the Hero's save method to dump all Heroes to individual files
   */
  public void saveAll()
  {
    for (int k = 0; k < _list.size(); k++) {
      saveHero(_list.get(k).getName());
    }
  }


  /**
   * Call the Hero's save method to serialize a file using the Hero's name.
   * 
   * @param key unique reference to hero to be serialized into the hero folder, in this case, the
   *        Hero's name
   */
  public void saveHero(String name)
  {
    Hero hero = getHero(name);
    if (hero != null) {
      String heroPath = _dirname + name + HEROFILE_EXT;
      hero.save(heroPath);
    }
  }


  // ========================================================
  // PRIVATE METHODS
  // ========================================================

  /**
   * Reads each Hero file and extracts their nameplate (name, gender, race, klass)
   * 
   * @param dirname directory containing all the serialized Heroes
   */
  private void initialize(String dirname)
  {
    // Create a File to point to the directory from which to read
    File regFile = new File(dirname);
    File[] heroList = regFile.listFiles();
    if (heroList.length != 0) {
      FileInputStream fileIn = null;
      ObjectInputStream ois = null;
      try {
        for (int k = 0; k < heroList.length; k++) {
          fileIn = new FileInputStream(heroList[k]);
          ois = new ObjectInputStream(fileIn);
          Hero hero = (Hero) ois.readObject();
          _list.add(hero);
          ois.close();
        }
      } catch (StreamCorruptedException scex) {
        System.err.println("Input stream header corrupted; could not read back Heroes");
        System.err.println("\t" + scex.getMessage());
      } catch (IOException | ClassNotFoundException ex) {
        System.err.println("Could not read back the HeroRegistry");
        System.err.println("\t" + ex.getMessage());
      }
    }
  }


  // ===============================================================================
  // MOCK FOR TESTING
  // ===============================================================================

  public class MockHeroRegistry
  {
    public MockHeroRegistry()
    {}

    /** Get the File that points to the output stream */
    public File getRegFile()
    {
      return new File(_dirname);

    }

    /*
     * Clear all heroes from the hero registry, but keep the registry
     */
    public void clear()
    {
      _list.clear();
    }



  } // end of inner mock class


} // end of HeroRegistry class

