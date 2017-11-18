import java.net.*;
import java.io.*;
import java.util.*;
import java.util.concurrent.*;

public class MyServerSocket{
  private DatagramSocket socket;
  private int port;
  private boolean listen;
  private int backlog;
  private BlockingQueue<DatagramPacket> inBuffer;
  private BlockingQueue<DatagramPacket> outBuffer;
  private Map<InetSocketAddress, MyClientSocket> connections;

  public MyServerSocket(int port){
    try{
      this.port = port;
      this.socket = new DatagramSocket(this.port);
      this.listen = false;
      this.backlog = -1;
      this.inBuffer = new ArrayBlockingQueue<DatagramPacket>(32);
      this.outBuffer = new ArrayBlockingQueue<DatagramPacket>(32);
      this.connections = Collections.synchronizedMap(new HashMap<InetSocketAddress, MyClientSocket>());
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
        try{
          while(true){
            DatagramPacket packet = new DatagramPacket(new byte[1024], 1024);
            socket.receive(packet);
            if(!connections.containsKey(new InetSocketAddress(packet.getAddress(), packet.getPort()))){
              inBuffer.put(packet);
            }
            else{
              connections.get(new InetSocketAddress(packet.getAddress(), packet.getPort())).getInBuffer().put(packet);
            }
          }
        }
        catch(Exception e){
          System.err.println(e.getMessage());
        }
      }
    }, "Reciever");
    reciever.start();
    System.out.println("Start listen!");
  }
  public MyClientSocket accept(){
    if(this.listen == false){
      System.err.println("Can't accept! Don't listen!");
      return null;
    }
    DatagramPacket packet;
    InetSocketAddress address = null;
    String[] data;
    DatagramPacket ack;
    long time;
    try{
      while(true){
        packet = this.inBuffer.take();
        address = new InetSocketAddress(packet.getAddress(), packet.getPort());
        data = new String(packet.getData(), packet.getOffset(), packet.getLength()).split(":");
        ack = new DatagramPacket("1:1:".getBytes(), "1:1:".getBytes().length, packet.getAddress(), packet.getPort());
        if(data[1].equals("0")){
          this.socket.send(ack);
          time = System.currentTimeMillis();
          break;
        }
      }
      while(true){
        packet = this.inBuffer.take();
        data = new String(packet.getData(), packet.getOffset(), packet.getLength()).split(":");
        if(data[1].equals("1") && address.equals(new InetSocketAddress(packet.getAddress(), packet.getPort()))){
          break;
        }
        else{
          this.inBuffer.put(packet);
        }
        if(System.currentTimeMillis() - time > 5000){
          this.socket.send(ack);
        }
      }
    }
    catch(Exception e){
      System.err.println(e.getMessage());
    }
    MyClientSocket socket = new MyClientSocket(this.socket, address);
    this.connections.put(address, socket);
    System.out.println(address + " connected!");
    return socket;
  }

  public void close(){
    this.socket.close();
  }
}
