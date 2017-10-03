import java.net.*;
import java.io.*;
import java.util.*;

public class TreeChat{
  private String nodeName;
  private int lossPercent;
  private int port;
  private String parentIP;
  private int parentPort;

  public TreeChat(String[] opts){
    try{
      this.nodeName = opts[0];
      this.lossPercent = new Integer(opts[1]);
      this.port = new Integer(opts[2]);
      if(opts.length == 5){
        this.parentIP = opts[3];
        this.parentPort = new Integer(opts[4]);
        System.out.println("NODE");
      }
      else{
        this.parentIP = null;
        this.parentPort = -1;
        System.out.println("ROOT");
      }
    }
    catch(NumberFormatException e){
      System.out.println("Error: " + e.getMessage());
      TreeChat.printHelp();
    }
    catch(Exception e){
      System.out.println("Error: " + e.getMessage());
    }
  }

  public static void printHelp(){
    System.out.println("java TreeChat {nodeName} {lossPercent} {nodePort} [parentIP] [parentPort]");
  }

  public static void main(String[] args){
    if(args.length == 3 || args.length == 5){
      TreeChat chat = new TreeChat(args);
    }
    else TreeChat.printHelp();
  }

}
