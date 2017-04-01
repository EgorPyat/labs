package ru.nsu.ccfit.pyataev.logo.auxunits;

import java.io.*;
import java.util.*;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ClassHunterTest{
  @Test(expected = NullPointerException.class)
  public void findTest() throws IOException{
    ClassHunter agent = new ClassHunter();
    String name = "INIT";
    String path = agent.find(name);
    assertEquals(path, "ru.nsu.ccfit.pyataev.logo.commandpost.commands.Init");
    name = "INit";
    agent.find(name);
  }
}
