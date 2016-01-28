package chronos.pdc.character;

public class Trait
{

  /** Indices into the Hero's prime traits */
  public enum PrimeTraits {
    STR, INT, WIS, DEX, CON, CHR
  };


  private final PrimeTraits _type;
  private final int _value;
  
  public Trait(PrimeTraits type, int value)
  {
    _type = type;
    _value = value;
  }

  public final int getValue()
  {
    return _value;
  }
  
  public final PrimeTraits getType()
  {
    return _type;
  }
}
