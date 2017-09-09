import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.HashSet;

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
          catch(Exception ex){
            System.err.println(ex.getMessage());
          }
        }
      }
    });

    HashSet<InetAddress> set = new HashSet<InetAddress>();

    try{
      mcSocket.send(new DatagramPacket("Hello".getBytes(), 5, mcIPAddress, mcPort));

      while(true){
        synchronized(monitor){
          DatagramPacket packet = new DatagramPacket(new byte[16], 16);
          mcSocket.receive(packet);
          String msg = new String(packet.getData(), packet.getOffset(), packet.getLength());

          if(msg.equals("Hello")){
            set.add(packet.getAddress());
            mcSocket.send(new DatagramPacket("Hello".getBytes(), 5, mcIPAddress, mcPort));
          }
          else if(msg.equals("Bye")){
            set.remove(packet.getAddress());
          }
          else if(!msg.equals("Bye") || !msg.equals("Hello")){
            System.out.println("Wrong msg");
          }

          System.out.println("Hosts number: " + set.size());
        }
      }
    }
    catch(Exception ex){
      System.err.println(ex.getMessage() + "!");
    }
  }
}
