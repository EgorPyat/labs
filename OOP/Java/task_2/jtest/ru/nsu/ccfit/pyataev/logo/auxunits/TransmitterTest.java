package ru.nsu.ccfit.pyataev.logo.auxunits;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TransmitterTest{
  @Test
  public void parseGetSetTest(){
    String command = "MOVE U 10";
    Transmitter tr = new Transmitter(command);
    assertEquals(tr.getName(), "MOVE");
    assertEquals((tr.getArgs())[0], "U");
    assertEquals((tr.getArgs())[1], "10");
  }
}
