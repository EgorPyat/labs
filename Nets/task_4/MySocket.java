import java.net.*;
import java.io.*;
import java.util.*;

public class MySocket{
  private boolean isListening = false;
  private BlockingQueue connections = null;
  private int port;
  private DatagramSocket socket;
  private InetSocketAddress connectedSocketAddress;
  public MySocket(int port){
    this.port = port;
    this.socket = new DatagramSocket(this.port);
  }
  public void listen(){
    this.isListening = true;
  }
  public MySocket accept(){
    if(this.isListening == false){
      System.err.println("Can't accept connections! I'm not a server socket!");
      return null;
    }
    else{
      while(true){
        byte[] buffer = new byte[8];
        this.recv(buffer, buffer.length);
      }
    }
    return null;
  }
  public void connect(InetAddress address, int port){
    if(this.isListening == true){
      System.err.println("Can't listen connections! I'm a server socket!");
    }
    else{
      this.connectedSocketAddress = new InetSocketAddress(address, port);
    }
  }
  public void send(byte[] buffer, int length){
    this.socket.send(new DatagramPacket(buffer, length, this.connectedSocketAddress.getAddress(), connectedSocketAddress.getPort()));
  }
  public void recv(byte[] buffer, int length){
    DatagramPacket packet = new DatagramPacket(buffer, length);
    socket.receive(packet);
  }
  public void close(){
    this.socket.close();
  }
}
