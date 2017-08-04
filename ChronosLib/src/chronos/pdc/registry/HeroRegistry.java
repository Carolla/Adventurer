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
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.List;

import chronos.pdc.Chronos;
import chronos.pdc.character.Hero;
import mylib.ApplicationException;
import mylib.pdc.Registry;

/**
 * Contains all Heros in the game. This is the only {@code Registry} subclass that currently saves
 * its data to a file. The other Registries are read-only, in-memory copies that must be initialized
 * each time.
 * 
 * @author Tim Armstrong, Alan Cline
 * @version Mar 13, 2013 // original <br>
 *          Dec 9 2015 // added a few interfacing methods to Registry <br>
 *          Dec 25 2015 // ABC added GetAllHeroes() <br>
 *          Mar 25 2016 // ABC Added getNamplares() <br>
 *          Mar 28 2016 // ABC Extended HeroRegistry to use DbReadWriter as subclass override <br>
 *          Aug 1, 2017 // replaced dbReadWriter with File; and updated per QATool <br>
 */
// public class HeroRegistry extends ConcreteRegistry<Hero>
public class HeroRegistry extends Registry<Hero> implements Serializable
{
  // Required for serialization
  static final long serialVersionUID = 1040L;

  /** Filename of where the Heroes are stored */
  private String _filename;

  // /** HeroRegistry is serialized to an object stream between sessions. */
  // private ObjectOutputStream _outStream = null;

  /** This file is needed to allow calling objects to have access to file stats */
  private File _regFile = null;


  /**
   * Create a Registry to contain all the Heroes 
   */
  public HeroRegistry()
  {
    super(Chronos.HeroRegPath);
    _filename = Chronos.HeroRegPath;
    // initialize(_filename);
  }


  // ========================================================
  // PUBLIC METHODS
  // ========================================================

  /**
   * Compare two registries, although only one should be instantiated. Required for serialization.
   * The only field to compare is filename where the registry is stored.
   * 
   * @param obj some other possible HeroRegistry to compare with
   */
  @Override
  public boolean equals(Object obj)
  {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass()) {
      return false;
    }
    HeroRegistry other = (HeroRegistry) obj;
    if (!_filename.equals(other._filename))
      return false;
    return true;
  }


  /** Required for serialization */
  @Override
  public int hashCode()
  {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((_filename == null) ? 0 : _filename.hashCode());
    return result;
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


  @Override
  public List<Hero> getAll()
  {
    return super.getAll();
  }


  /** This version does nothing; see {@code initialize(String filename)}. */
  @Override
  public void initialize()
  {}


  // /*
  // * There are no Heroes in the heroRegistry when first initialized. Heroes are kept in a file and
  // * read in as requested, one at a time, during play
  // *
  // * @see mylib.pdc.Registry#initialize()
  // */
  // public void initialize(String filename)
  // {
  // _filename = filename;
  // ObjectOutputStream oos = null;
  // try {
  // oos = new ObjectOutputStream(new FileOutputStream(_filename));
  // } catch (FileNotFoundException ex) {
  // ex.printStackTrace();
  // }
  // try {
  // oostStream = new ObjectOutputStream(fileOut);
  // } catch (IOException ex) {
  // ex.printStackTrace();
  // }
  // // Create a File to point to the output stream for later file stats since streams have none
  // _regFile = new File(_filename);
  // }


  /*
   * Serialize the entire registry to file
   */
  public void save()
  {
    FileOutputStream fileOut = null;
    ObjectOutputStream oos = null;
    try {
      fileOut = new FileOutputStream(_filename);
    } catch (FileNotFoundException ex) {
      ex.printStackTrace();
    }
    try {
      oos = new ObjectOutputStream(fileOut);
      oos.writeObject(this);
      oos.flush();
      oos.close();
    } catch (IOException ex) {
      System.err.println("Could not create or write to serialize the HeroRegistry");
      System.err.println("\t" + ex.getMessage());
    }
    // Create a File to point to the output stream for later file stats since streams have none
    _regFile = new File(_filename);
  }


  /*
   * Instantiate the serialized HeroRegistry from its file
   */
  public HeroRegistry load()
  {
    FileInputStream fileOut = null;
    ObjectInputStream oos = null;
    HeroRegistry hreg = null;
    try {
      fileOut = new FileInputStream(_filename);
    } catch (FileNotFoundException ex) {
      ex.printStackTrace();
    }
    try {
      oos = new ObjectInputStream(fileOut);
      hreg = (HeroRegistry) oos.readObject();
      oos.close();
    } catch (IOException | ClassNotFoundException ex) {
      System.err.println("Could not read back the HeroRegistry");
      System.err.println("\t" + ex.getMessage());
    }
    // Create a File to point to the output stream for later file stats since streams have none
    _regFile = new File(_filename);
    return hreg;
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
      return _regFile;
    }

    /**
     * Delete the dormitory (OutputStream cannot be deleted directly). WARNING: Should only be used
     * for testing.
     */
    public void delete()
    {
      File saveFile = new File(_filename);
      saveFile.delete();
    }


  } // end of inner mock class



} // end of HeroRegistry class

