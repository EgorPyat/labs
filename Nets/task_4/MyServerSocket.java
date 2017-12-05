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
  private Map<InetSocketAddress, MyClientSocket> connections;

  public MyServerSocket(int port){
    try{
      this.port = port;
      this.socket = new DatagramSocket(this.port);
      this.listen = false;
      this.backlog = -1;
      this.inBuffer = new ArrayBlockingQueue<DatagramPacket>(32);
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
          while(listen){
            DatagramPacket packet = new DatagramPacket(new byte[1024], 1024);
            socket.receive(packet);
            InetSocketAddress address = new InetSocketAddress(packet.getAddress(), packet.getPort());
            if(!connections.containsKey(address)){
              inBuffer.put(packet);
            }
            else{
              connections.get(address).getInBuffer().put(packet);
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
    MyClientSocket client = null;
    try{
      while(true){
        packet = this.inBuffer.take();
        address = new InetSocketAddress(packet.getAddress(), packet.getPort());
        data = new String(packet.getData(), packet.getOffset(), packet.getLength()).split(":");
        if(data[1].equals("0")){
          client = new MyClientSocket(this.socket, address);
          this.connections.put(address, client);
          ack = new DatagramPacket("1:1:0".getBytes(), "1:1:0".getBytes().length, packet.getAddress(), packet.getPort());
          this.socket.send(ack);
          time = System.currentTimeMillis();
          break;
        }
      }
      while(true){
        // packet = client.getInBuffer().take();
        if((packet = client.getInBuffer().poll(1000, TimeUnit.MILLISECONDS)) == null){
          this.socket.send(ack);
        }
        else{
          data = new String(packet.getData(), packet.getOffset(), packet.getLength()).split(":");

          if(data[1].equals("1") && address.equals(new InetSocketAddress(packet.getAddress(), packet.getPort()))){
            break;
          }
        }
        // else{
          // client.getInBuffer().put(packet);
        // }
        // if(System.currentTimeMillis() - time > 500){
        //   this.socket.send(ack);
        // }
      }
    }
    catch(Exception e){
      System.err.println(e.getMessage());
    }
    System.out.println("accepted!");
    return client;
  }

  public void close(){
    this.listen = false;
    this.socket.close();
  }
}
