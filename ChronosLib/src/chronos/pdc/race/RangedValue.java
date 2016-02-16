package chronos.pdc.race;

import mylib.pdc.MetaDie;

public class RangedValue
{
  private static MetaDie _md = new MetaDie();
  private String _highDice;
  private String _lowDice;
  private int _average;

  public RangedValue(int average, String baseDie)
  {
    this(average, baseDie, baseDie);
  }

  public RangedValue(int average, String lowDice, String highDice)
  {
    _average = average;
    _lowDice = lowDice;
    _highDice = highDice;
  }

  /** Calculate the weight of the Hero based on deviation from average */
  public int calcValue()
  {
    int result = 0;
    int range = _md.rollPercent();
    if (range < 31) {
      result = _average - _md.roll(_lowDice);
    } else if (range > 69) {
      result = _average + _md.roll(_highDice);
    } else {
      result = _average;
    }
    return result;
  }

}
