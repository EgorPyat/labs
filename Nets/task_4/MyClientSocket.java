import java.net.*;
import java.io.*;
import java.util.*;
import java.util.concurrent.*;

public class MyClientSocket{
  private DatagramSocket socket;
  private InetSocketAddress connectedSocket;
  private boolean independent;
  private BlockingQueue<DatagramPacket> inBuffer;
  private BlockingQueue<DatagramPacket> outBuffer;
  public MyClientSocket(){
    try{
      this.socket = new DatagramSocket();
      this.independent = true;
      this.inBuffer = new ArrayBlockingQueue<DatagramPacket>(32);
      this.outBuffer = new ArrayBlockingQueue<DatagramPacket>(32);
      Thread reciever = new Thread(new Runnable(){
        @Override
        public void run(){
          try{
            while(true){
              DatagramPacket packet = new DatagramPacket(new byte[1024], 1024);
              socket.receive(packet);
              inBuffer.put(packet);
            }
          }
          catch(Exception e){
            System.err.println(e.getMessage());
          }
        }
      }, "Reciever");
      reciever.start();
    }
    catch(SocketException e){
      System.err.println(e.getMessage());
    }
    catch(Exception e){
      System.err.println(e.getMessage());
    }
  }
  public MyClientSocket(DatagramSocket socket, InetSocketAddress address){
    this.socket = socket;
    this.connectedSocket = address;
    this.independent = false;
    this.inBuffer = new ArrayBlockingQueue<DatagramPacket>(32);
    this.outBuffer = new ArrayBlockingQueue<DatagramPacket>(32);
  }
  public void connect(InetAddress address, int port){
    try{
      DatagramPacket syn = new DatagramPacket("1:0:".getBytes(), "1:0:".getBytes().length, address, port);
      this.socket.send(syn);
      long time = System.currentTimeMillis();
      while(true){
        DatagramPacket packet = this.inBuffer.take();
        String[] data = new String(packet.getData(), packet.getOffset(), packet.getLength()).split(":");
        System.out.println(data[1] + " got");

        if(data[1].equals("1") && new InetSocketAddress(address, port).equals(new InetSocketAddress(packet.getAddress(), packet.getPort()))){
          this.connectedSocket = new InetSocketAddress(address, port);
          System.out.println("Client connected!");
          syn = new DatagramPacket("1:1:".getBytes(), "1:1:".getBytes().length, packet.getAddress(), packet.getPort());
          this.socket.send(syn);
          break;
        }
        if(System.currentTimeMillis() - time > 5000){
          this.socket.send(syn);
        }
      }
    }
    catch(Exception e){
      System.err.println(e.getMessage());
    }
  }
  public void recieve(byte[] buffer){

  }
  public void send(byte[] buffer){
    try{
      if(this.connectedSocket == null){
        System.err.println("Can't send! Don't connect!");
      }
      byte[] header = "1:2:".getBytes();
      byte[] mass = new byte[header.length + buffer.length];
      System.arraycopy(header, 0, mass, 0, buffer.length);
      System.arraycopy(buffer, 0, mass, header.length, buffer.length);

      DatagramPacket msg = new DatagramPacket(mass, mass.length, this.connectedSocket.getAddress(), this.connectedSocket.getPort());
      this.socket.send(msg);
      long time = System.currentTimeMillis();
      while(true){
        DatagramPacket packet = this.inBuffer.take();
        String[] data = new String(packet.getData(), packet.getOffset(), packet.getLength()).split(":");
        if(data[1].equals("1") && this.connectedSocket.equals(new InetSocketAddress(packet.getAddress(), packet.getPort()))){
          break;
        }
        else{
          this.inBuffer.put(packet);
        }
        if(System.currentTimeMillis() - time > 5000){
          this.socket.send(msg);
        }
      }
    }
    catch(Exception e){
      System.err.println(e.getMessage());
    }
  }
  public BlockingQueue<DatagramPacket> getInBuffer(){
    return this.inBuffer;
  }
  public void close(){
    this.socket.close();
  }
}
