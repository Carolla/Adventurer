
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
    if (range <= 30) {
      result = _average - _md.roll(_lowDice);
    } else if (range >= 70) {
      result = _average + _md.roll(_highDice);
    } else {
      result = _average;
    }
    return result;
  }

  /**
   * Calculate the weight of the Hero based on deviation from average. For example,
   * {@code calcValue(10, "2d6")} will provide a 2-12 number added to 10 to get 12-22, but centered
   * on the average of 17
   * 
   * @param lowValue the lowest value in the range
   * @param wtVariance the multiple-die distribution to be added to lowValue
   */
  public int calcValue(int lowValue, String wtVariance)
  {
    int result = 0;
    result = lowValue + _md.roll(wtVariance);
    return result;
  }

}
