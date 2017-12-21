import java.net.*;
import java.io.*;
import java.util.*;
import java.util.concurrent.*;

public class MyClientSocket{
  private final int DATALENGTH = 1020;
  private final int BUFFERSIZE = 1024;
  private DatagramSocket socket;
  private InetSocketAddress connectedSocket;
  private boolean independent;
  private boolean work;
  private BlockingQueue<DatagramPacket> inBuffer;
  private BlockingQueue<DatagramPacket> outBuffer;
  private Map<Integer, DatagramPacket> unconfPackets;
  private Map<Integer, Long> unconfTime;

  public MyClientSocket(){
    try{
      this.socket = new DatagramSocket();
      this.independent = true;
      this.inBuffer = new ArrayBlockingQueue<DatagramPacket>(32);
      this.outBuffer = new ArrayBlockingQueue<DatagramPacket>(32);
      this.unconfPackets = new HashMap<Integer, DatagramPacket>();
      this.unconfTime = new HashMap<Integer, Long>();
      this.work = true;
      Thread reciever = new Thread(new Runnable(){
        @Override
        public void run(){
          try{
            while(work){
              DatagramPacket packet = new DatagramPacket(new byte[BUFFERSIZE], BUFFERSIZE);
              socket.receive(packet);
              inBuffer.put(packet);
            }
          }
          catch(Exception e){
            // System.err.println(e.getMessage());
            // e.printStackTrace();
          }
        }
      }, "Reciever");
      reciever.start();
      Thread resender = new Thread(new Runnable(){
        @Override
        public void run(){
          try{
            for(int num : unconfPackets.keySet()){
              if(System.currentTimeMillis() - unconfTime.get(num) > 1000){
                System.out.println("resend");
                unconfTime.put(num, System.currentTimeMillis());
                socket.send(unconfPackets.get(num));
              }
            }
          }
          catch(Exception e){
            // System.err.println("in resender " + e.getMessage());
            e.printStackTrace();
          }
        }
      }, "Resender");
    }
    catch(SocketException e){
      System.err.println(e.getMessage());
      e.printStackTrace();
    }
    catch(Exception e){
      System.err.println(e.getMessage());
      e.printStackTrace();

    }
  }
  public MyClientSocket(DatagramSocket socket, InetSocketAddress address){
    this.socket = socket;
    this.connectedSocket = address;
    this.independent = false;
    this.inBuffer = new ArrayBlockingQueue<DatagramPacket>(32);
    this.outBuffer = new ArrayBlockingQueue<DatagramPacket>(32);
    this.unconfPackets = new HashMap<Integer, DatagramPacket>();
    this.unconfTime = new HashMap<Integer, Long>();
  }
  public void connect(InetAddress address, int port){
    try{
      DatagramPacket ack = new DatagramPacket("1:0:".getBytes(), "1:0:".getBytes().length, address, port);
      this.socket.send(ack);
      long time = System.currentTimeMillis();
      while(true){
        DatagramPacket packet = this.inBuffer.poll(1000, TimeUnit.MILLISECONDS);
        if(packet == null){
          this.socket.send(ack);
        }
        else{
          String[] data = new String(packet.getData(), packet.getOffset(), packet.getLength()).split(":", 3);
          if(data[1].equals("1") && new InetSocketAddress(address, port).equals(new InetSocketAddress(packet.getAddress(), packet.getPort()))){
            this.connectedSocket = new InetSocketAddress(address, port);
            ack = new DatagramPacket("1:1:1".getBytes(), "1:1:1".getBytes().length, packet.getAddress(), packet.getPort());
            this.socket.send(ack);
            break;
          }
        }
      }
    }
    catch(Exception e){
      System.err.println(e.getMessage());
    }
    System.out.println("Client connected!");
  }
  public int recieve(byte[] buffer){
    int sum = 0;
    try{
      while(true){
        DatagramPacket packet = this.inBuffer.take();
        // System.out.println("recv " + new String(packet.getData(), packet.getOffset(), packet.getLength()));
        String[] data = new String(packet.getData(), packet.getOffset(), packet.getLength()).split(":", 3);

        if(data[1].equals("2") && this.connectedSocket.equals(new InetSocketAddress(packet.getAddress(), packet.getPort()))){
          System.out.println(data[0] + " " + data[1] + " " + data[2].length());
          packet = new DatagramPacket(("1:1:" + data[0]).getBytes(), ("1:1:" + data[0]).getBytes().length, connectedSocket.getAddress(), connectedSocket.getPort());
          System.arraycopy(data[2].getBytes(), 0, buffer, DATALENGTH * (new Integer(data[0]) - 1), data[2].getBytes().length);
          sum += data[2].getBytes().length;
          this.socket.send(packet);
        }
        else if(data[1].equals("3") && this.connectedSocket.equals(new InetSocketAddress(packet.getAddress(), packet.getPort()))){
          // System.out.println(data[0] + " " + data[1]);
          packet = new DatagramPacket(("1:3:" + data[0]).getBytes(), ("1:3:" + data[0]).getBytes().length, connectedSocket.getAddress(), connectedSocket.getPort());
          this.socket.send(packet);
          break;
        }
      }
      System.out.println("sum " + sum);
    }
    catch(Exception e){
      System.out.println("in recieve " + e.getMessage());
      e.printStackTrace();
    }
    return sum;
  }
  public void send(byte[] buffer){
    int dataLength = buffer.length;
    int segNum = dataLength / DATALENGTH;
    int rest = dataLength % DATALENGTH;
    byte[][] segments = new byte[segNum + 1][];

    System.out.println(dataLength + " " + segNum + " " + rest);
    for(int i = 0; i < segNum; i++){
      byte[] header = ((i + 1) + ":2:").getBytes();
      segments[i] = new byte[header.length + DATALENGTH];
      System.arraycopy(header, 0, segments[i], 0, header.length);
      System.arraycopy(buffer, DATALENGTH * i, segments[i], header.length, DATALENGTH);
    }
    if(rest > 0){
      byte[] header = ((segNum + 1) + ":2:").getBytes();
      segments[segNum] = new byte[header.length + rest];
      System.arraycopy(header, 0, segments[segNum], 0, header.length);
      System.arraycopy(buffer, DATALENGTH * segNum, segments[segNum], header.length, rest);
    }
    else{
      segments[segNum] = null;
    }
    // for(int i = 0; i < segNum; i++){
    //   System.out.println((i + 1) + " " + new String(segments[i]) + " " + segments[i].length);
    // }
    // if(rest > 0){
    //   System.out.println((segNum + 1) + " " + new String(segments[segNum]) + " " + segments[segNum].length);
    // }
    try{
      for(int i = 0; i < segNum; i++){
        DatagramPacket msg = new DatagramPacket(segments[i], segments[i].length, this.connectedSocket.getAddress(), this.connectedSocket.getPort());
        this.socket.send(msg);
        this.unconfPackets.put(i + 1, msg);
        this.unconfTime.put(i + 1, System.currentTimeMillis());
      }
      if(rest > 0){
        DatagramPacket msg = new DatagramPacket(segments[segNum], segments[segNum].length, this.connectedSocket.getAddress(), this.connectedSocket.getPort());
        this.socket.send(msg);
        this.unconfPackets.put(segNum + 1, msg);
        this.unconfTime.put(segNum + 1, System.currentTimeMillis());
      }
      while(this.unconfPackets.size() > 0){
        DatagramPacket packet = this.inBuffer.take();
        String[] data = new String(packet.getData(), packet.getOffset(), packet.getLength()).split(":", 3);
        if(data[1].equals("1") && this.connectedSocket.equals(new InetSocketAddress(packet.getAddress(), packet.getPort()))){
          this.unconfPackets.remove(new Integer(data[2]));
          this.unconfTime.remove(new Integer(data[2]));
        }
      }
      DatagramPacket msg = new DatagramPacket(((segNum + 2) + ":3:").getBytes(), ((segNum + 2) + ":3:").getBytes().length, this.connectedSocket.getAddress(), this.connectedSocket.getPort());
      this.socket.send(msg);
      this.unconfPackets.put(segNum + 2, msg);
      this.unconfTime.put(segNum + 2, System.currentTimeMillis());

      while(this.unconfPackets.size() > 0){
        DatagramPacket packet = this.inBuffer.take();
        String[] data = new String(packet.getData(), packet.getOffset(), packet.getLength()).split(":", 3);
        if(data[1].equals("3") && this.connectedSocket.equals(new InetSocketAddress(packet.getAddress(), packet.getPort()))){
          this.unconfPackets.remove(new Integer(data[2]));
          this.unconfTime.remove(new Integer(data[2]));
        }
      }
    }
    catch(Exception e){
      System.err.println("in send " + e.getMessage());
      // e.printStackTrace();
    }
  }
  public BlockingQueue<DatagramPacket> getInBuffer(){
    return this.inBuffer;
  }
  public void close(){
    this.work = false;
    this.socket.close();
  }
}
