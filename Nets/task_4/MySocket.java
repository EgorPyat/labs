import java.net.*;
import java.io.*;
import java.util.*;
import java.util.concurrent.*;

public class MySocket{
  private int port;
  private DatagramSocket socket;
  private boolean isListening = false;
  private BlockingQueue messages = null;
  private InetSocketAddress connectedSocketAddress = null;
  private int segmentLength = 256;

  private class Message{
    private InetSocketAddress address;
    private String[] data;
    public Message(InetSocketAddress address, String[] data){
      this.address = address;
      this.data = data;
    }
    public byte[] getData(){
      return this.data;
    }
    public String getType(){
      return this.data[1];
    }
    public InetSocketAddress getAddress(){
      return this.address;
    }
  }

  public MySocket(int port){
    this.port = port;
    this.socket = new DatagramSocket(this.port);
  }

  private MySocket(DatagramSocket socket, BlockingQueue messages, InetSocketAddress address){
    this.isListening = true;
    this.socket = socket;
    this.port = socket.getPort();
    this.connectedSocketAddress = address;
  }

  public void listen(){
    this.isListening = true;
    this.connections = new ArrayBlockingQueue<Message>(1024);
  }

  public MySocket accept(){
    if(this.isListening == false){
      System.err.println("Can't accept connections! I'm not a server socket!");
      return Null;
    }
    else{
      DatagramPacket packet = new DatagramPacket(buffer, length);
      socket.receive(packet);
      String data = new String(packet.getData(), packet.getOffset(), packet.getLength());
      String[] dataArray = data.split(":");
      if(dataArray[1]).equals("0")){
        byte[] data = "1/1:0:".getBytes();
        this.socket.send(new DatagramPacket(data, data.length, packet.getAddress(), packet.getPort()));
        return new MySocket(this.socket, this.messages, new InetSocketAddress(packet.getAddress(), packet.getPort()));
      }
    }
  }

  public void connect(InetAddress address, int port){
    if(this.isListening == true){
      System.err.println("Can't connect! I'm a server socket!");
    }
    else if(this.connectedSocketAddress != Null){
      System.err.println("Can't connect! I'm already conneted!");
    }
    else{
      byte[] data = "1/1:0".getBytes();
      this.socket.send(new DatagramPacket(data, data.length, address, port));
      data = new byte[1024];
      DatagramPacket packet = new DatagramPacket(data, data.length);
      socket.receive(packet);
      String message = new String(packet.getData(), packet.getOffset(), packet.getLength());
      if(message.split(':')[1].equals("hi")){
        System.out.println("Connected to server use port " + this.port + "!");
        this.connectedSocketAddress = new InetSocketAddress(address, port);
      }
    }
  }

  public void send(String message){
    if(this.connectedSocketAddress == null){
      System.err.println("Can't send message! No connection to server!");
    }
    else{
      byte[] data = ("1/1:1:" + message).getBytes();
      this.socket.send(new DatagramPacket(data, data.length, this.connectedSocketAddress.getAddress(), connectedSocketAddress.getPort()));
      DatagramPacket packet = new DatagramPacket(new byte[256], 256);
      socket.receive(packet);
      String message = new String(packet.getData(), packet.getOffset(), packet.getLength());
    }
  }

  public void recieve(){
    if(this.connectedSocketAddress == null){
      System.err.println("Can't recieve message! No connection to server!");
    }
    else if(this.isListening == false){
      DatagramPacket packet = new DatagramPacket(new byte[256], 256);
      socket.receive(packet);
      String[] dataArray = new String(packet.getData() , packet.getOffset(), packet.getLength()).split(':');
      byte[] data = ("1/1:1:").getBytes();
      this.socket.send(new DatagramPacket(data, data.length, this.connectedSocketAddress.getAddress(), connectedSocketAddress.getPort()));
      byte[] buffer = packet.getData();
    }
    else if(this.isListening == true){
      while(true){
        if(this.messages.take().getType().equals("")
      }
    }
  }

  public void close(){
    this.socket.close();
  }
}
// private class Message{
//   private InetSocketAddress addr;
//   private byte[] data;
//   private long time;
//   private int tryNum;
//   public Message(InetSocketAddress addr, byte[] data, long time, int tryNum){
//     this.addr = addr;
//     this.data = data;
//     this.time = time;
//     this.tryNum = tryNum;
//   }
//   public long getTime(){
//     return this.time;
//   }
//   public int getTryNum(){
//     return this.tryNum;
//   }
//   public byte[] getData(){
//     return this.data;
//   }
//   public String getType()
//   public InetSocketAddress getAddress(){
//     return this.addr;
//   }
// }
