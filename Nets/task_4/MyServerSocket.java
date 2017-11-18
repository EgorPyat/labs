import java.net.*;
import java.io.*;
import java.util.*;

public class MyServerSocket{
  private DatagramSocket socket;
  private int port;
  private boolean listen;
  private int backlog;
  private BlockingQueue inBuffer;
  private BlockingQueue outBuffer;
  public MyServerSocket(int port){
    try{
      this.port = port;
      this.socket = new DatagramSocket(this.port);
      this.listen = false;
      this.backlog = -1;
      this.inBuffer = new ArrayBlockingQueue<byte[]>(32);
      this.outBuffer = new ArrayBlockingQueue<byte[]>(32);
    }
    catch(SocketException e){
      System.err.println(e.getMessage());
    }
    catch(Exception e){
      System.err.println(e.getMessage());
    }
  }
  public void listen(int backlog){
    if(this.listen == true){
      System.err.println("Already listening!");
      return;
    }
    this.listen = true;
    this.backlog = backlog;
    Thread reciever = new Thread(new Runnable(){
      @Override
      public void run(){
        DatagramPacket packet = new DatagramPacket(new byte[1024], 1024);
        this.socket.recieve(packet);
        String[] data = new String(packet.getData(), packet.getOffset(), packet.getLength()).split(':');
      }
    }, "Reciever");
    Thread sender = new Thread(new Runnable(){
      @Override
      public void run(){

      }
    }, "Sender");
  }
  public MyClientSocket accept(){
    if(this.listen == false){
      System.err.println("Can't accept! Don't listen!");
      return null;
    }

    return new MyClientSocket(this.socket, new InetSocketAddress(packet.getAddress(), packet.getPort()));
  }
  public void close(){
    this.socket.close();
  }
}
