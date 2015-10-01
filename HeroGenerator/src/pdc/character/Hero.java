/**
 * Person.java Copyright (c) 2009, Carolla Development, Inc. All Rights Reserved
 *
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists, requires
 * prior specific permission and/or a fee. Request permission to use from Carolla Development, Inc.
 * by email: acline@carolla.com
 */


package pdc.character;


import java.io.Serializable;

import mylib.pdc.MetaDie;
import pdc.Klass;


/**
 * @author Alan Cline
 * @version Sept 4 2015 // rewrite per revised generation rules <br>
 */
public class Hero implements Serializable // IRegistryElement
{
  // Statics and transients are not saved with the serialized Person object
  /** Recommended serialization constant */
  static final long serialVersionUID = 1007L;


  /* INTERNAL OBJECT COMPONENTS */
  /** One of the four canonical Hero klasses: Fighter, Cleric, Wizard, or Thief */
  private Klass _klass;
  /** The Race object for this Person (Input), and contains the Hunger and Age objects */
  // private Race _race = null;

  /** Input Data for Hero */
  private String _name = null;
  /** Male or female Person */
  private String _gender = null;;
  /** Person's hair color, used in building their physical appearance */
  protected String _hairColor = null;
  /** Name of the race to convert to a Race component */
  private String _racename;
  /** Name of the klass to convert to a Klass component */
  private String _klassname;

  /** What we see when we look at the Person */
  private String _description = null;

  /** Contains the indexes into the prime traits */
  private final int NBR_TRAITS = 6;

  public enum PrimeTraits {
    STR, INT, WIS, CON, DEX, CHR;
  };

  /** Each Person has six prime traits, adjusted by gender, race, and klass */
  private int[] _traits = new int[NBR_TRAITS];




  // ====================================================
  // CONSTRUCTOR(S) AND RELATED METHODS
  // ====================================================

  /** Default Hero object used for testing */
  public Hero()
  {}

  /**
   * Create the Person from the basic non-klass attributes.
   * 
   * @param name of the Person, default filename under which it is saved
   * @param gender male or female Person
   * @param hairColor selected hair color for this Person
   * @param raceName the concrete subclass object of Race, e.g., Human or Hobbit
   * @param klassName the concrete subclass object of Klass, e.g. Fighter or Thief
   * 
   * @throws InstantiationException if any of the input parms are null
   */
  public Hero(String name, String gender, String hairColor, String raceName, String klassName)
      throws InstantiationException
  {
    // Guards
    if ((name == null) || (gender == null) || (hairColor == null)) {
      throw new InstantiationException("Missing input data passed to Person constructor");
    }
    if ((raceName == null) || (klassName == null)) {
      throw new InstantiationException("Missing Race or Klass passed to Person constructor");
    }

    // 1. INPUT DATA (racename and klassname are used later)
    _name = name;
    _gender = gender;
    _hairColor = hairColor;

    // 2. SET PRIME TRAITS for Peasant (base is same for all Klasses and Races)
    MetaDie md = new MetaDie();
    for (int k = 0; k < NBR_TRAITS; k++) {
      _traits[k] = md.rollTrait();
    }

    // 3. REARRANGE THE PRIME TRAIT FOR THE KLASS
    // Create the Klass object
    _klass = Klass.createKlass(klassName);
    // Verify that good objects were created
    if (_klass == null) {
      throw new InstantiationException(String.format("Could not create klass ", klassName));
    }



  } // end of Hero constructor



  // ====================================================
  // INNER CLASS MockHero
  // ====================================================

  /** Accesses and tests the private methods of the Person object. */
  public class MockHero
  {
    /** Default constructor */
    public MockHero()
    {}

    /** for test */
    public int[] getTraits()
    {
      return Hero.this._traits;
    }


  } // end of MockHero inner class


} // end of Hero class
