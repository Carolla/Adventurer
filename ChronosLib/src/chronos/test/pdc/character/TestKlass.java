package chronos.test.pdc.character;

import static org.junit.Assert.*;

import org.junit.Test;

import chronos.pdc.character.Klass;

public class TestKlass
{

  @Test
  public void klassesAreConstructedByName()
  {
    for (String klassName : Klass.KLASS_LIST) {
      assertEquals(klassName, Klass.createKlass(klassName, null).className());
    }
  }

  
}
