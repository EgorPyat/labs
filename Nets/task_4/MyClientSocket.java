import java.net.*;
import java.io.*;
import java.util.*;
import java.util.concurrent.*;

public class MyClientSocket{
  private DatagramSocket socket;
  private InetSocketAddress connectedSocket;
  private boolean independent;
  private BlockingQueue inBuffer;
  private BlockingQueue outBuffer;
  public MyClientSocket(){
    try{
      this.socket = new DatagramSocket();
      this.independent = true;
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
  public MyClientSocket(DatagramSocket socket, InetSocketAddress address){
    this.socket = socket;
    this.connectedSocket = address;
    this.independent = false;
  }
  public void connect(InetAddress address, int port){

  }
  public int recieve(byte[] buffer){
    return -1;
  }
  public int send(byte[] buffer){
    return -1;
  }
  public void close(){
    this.socket.close();
  }
}
