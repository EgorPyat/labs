import java.net.*;
import java.io.*;
import java.util.*;
import java.util.concurrent.*;

public class MySocket{
  private boolean isServer;
  private DatagramSocket socket;
  private int port;
  private int backlog;
  private InetSocketAddress connectedSocket;
  private BlockingQueue<DatagramPacket> inBuffer;
  private Map<InetSocketAddress, MyClientSocket> connections;

  public MySocket(int port){
    this.socket = new DatagramSocket(port)
  }

  private MySocket(DatagramSocket socket, InetSocketAddress address){

  }
}
