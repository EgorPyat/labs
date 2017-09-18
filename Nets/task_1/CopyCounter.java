import java.net.*;
import java.util.*;
import java.io.*;

public class CopyCounter{
  private int mcPort;
  private String mcIPStr;
  private MulticastSocket mcSocket;
  private InetAddress mcIPAddress;
  private Object monitor = new Object();

  public CopyCounter(String addr, int port){
    mcPort = port;
    mcIPStr = addr;
    try{
      mcIPAddress = InetAddress.getByName(mcIPStr);
      mcSocket = new MulticastSocket(mcPort);
      mcSocket.joinGroup(mcIPAddress);
    }
    catch(Exception ex){
      System.out.println(ex.getMessage());
    }
  }

  public void startCount(){
    Runtime.getRuntime().addShutdownHook(new Thread(){
      public void run(){
        synchronized(monitor){
          System.out.println("Finish...");
          try{
            mcSocket.send(new DatagramPacket("Bye".getBytes(), 3, mcIPAddress, mcPort));
            mcSocket.leaveGroup(mcIPAddress);
            mcSocket.close();
          }
          catch(IOException ex){
            System.err.println(ex.getMessage());
          }
        }
      }
    });

    Map<SocketAddress, Long> map = Collections.synchronizedMap(new HashMap<SocketAddress, Long>());

    Thread deadChecker = new Thread(new Runnable(){
      @Override
      public void run(){
        while(!Thread.interrupted()){
          for(SocketAddress adr : map.keySet()){
            if(System.currentTimeMillis()/1000 - map.get(adr) > 5){
              map.remove(adr);
            }
          }
        }
      }
    });

    Thread receiver = new Thread(new Runnable(){
      @Override
      public void run(){
        while(!Thread.interrupted()){
          try{
            DatagramPacket packet = new DatagramPacket(new byte[16], 16);
            mcSocket.receive(packet);
            String msg = new String(packet.getData(), packet.getOffset(), packet.getLength());

            if(msg.equals("Hello")){
              map.put(packet.getSocketAddress(), System.currentTimeMillis());
            }
            else if(msg.equals("Bye")){
              map.remove(packet.getAddress());
            }
            else if(!msg.equals("Bye") || !msg.equals("Hello")){
              System.out.println("Wrong msg");
            }
          }
          catch(IOException e){
            System.err.println("IOError: " + e.getMessage());
          }
        }
      }
    });

    Thread sender = new Thread(new Runnable(){
      @Override
      public void run(){
        while(!Thread.interrupted()){
          try{
            mcSocket.send(new DatagramPacket("Hello".getBytes(), 5, mcIPAddress, mcPort));
            Thread.sleep(3000);
          }
          catch(IOException e){
            System.err.println("IOError: " + e.getMessage());
          }
          catch(InterruptedException e){
            System.err.println("SleepError: " + e.getMessage());
          }
        }
      }
    });

    receiver.start();
    deadChecker.start();
    sender.start();

    while(true){
      try{
        Thread.sleep(1000);
      }
      catch(InterruptedException e){
        System.err.println("Error: " + e.getMessage());
      }

      System.out.println(map.size());
    }
  }

}
