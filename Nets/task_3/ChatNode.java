import java.net.*;
import java.io.*;
import java.util.*;
import java.time.*;

public class ChatNode{
  private String nodeName;
  private int nodePort;
  private int lossPercent;
  private boolean root;
  private InetSocketAddress parentAddress;
  private String parentIP;
  private Map<InetSocketAddress, Long> addresses;
  private Map<UUID, Message> unconfirmedMessages;
  private List<UUID> recievedMessages;
  private DatagramSocket sendSocket;
  private DatagramSocket recieveSocket;
  private Random random = new Random(LocalTime.now().hashCode());
  private static enum MessageType{
    NUR, TXT, ACC, FIN, NRT
  }
  private class Message{
    private InetSocketAddress addr;
    private long time;
    private String msg;
    private int tryNum;
    public Message(InetSocketAddress addr, long time, String msg, int tryNum){
      this.addr = addr;
      this.time = time;
      this.msg = msg;
      this.tryNum = tryNum;
    }
    public long getTime(){
      return this.time;
    }
    public int getTryNum(){
      return this.tryNum;
    }
    public String getMessage(){
      return this.msg;
    }
    public InetSocketAddress getAddress(){
      return this.addr;
    }
  }
  public ChatNode(String[] opts){
    try{
      this.nodeName = opts[0];
      this.lossPercent = new Integer(opts[1]);
      this.nodePort = new Integer(opts[2]);
      if(opts.length == 5){
        this.parentIP = opts[3];
        this.parentAddress = new InetSocketAddress(InetAddress.getByName(opts[3]), new Integer(opts[4]));
        this.root = false;
      }
      else{
        this.parentAddress = null;
        this.root = true;
      }
      if(this.lossPercent < 0 || this.lossPercent > 100){
        System.out.println("Invalid option value!");
        ChatNode.printHelp();
      }
    }
    catch(Exception e){
      System.out.println("Constructor: " + e.getMessage());
      ChatNode.printHelp();
    }
  }
  public void startChatting(){
    try{
      Thread resender = this.getResender();
      Thread reciever = this.getReciever();
      Thread sender = this.getSender();
      this.openConnection();
      boolean connected = true;
      if(this.isNode()){
        connected = this.connect(parentAddress);
      }
      if(connected){
        this.addInterruptionHandler();
        resender.start();
        reciever.start();
        sender.start();
        resender.join();
        reciever.join();
        sender.join();
      }
      else{ this.closeConnection(); }
    }
    catch(Exception e){
      System.out.println("Chatting " + e.getMessage());
      e.printStackTrace();
    }
  }
  private void addInterruptionHandler(){
    Runtime.getRuntime().addShutdownHook(new Thread(){
      @Override
      public void run(){
        try{
          disconnect();
          closeConnection();
        }
        catch(Exception e){
          System.out.println("Handler " + e.getMessage());
        }
      }
    });
  }
  private void openConnection() throws SocketException{
    this.recievedMessages = Collections.synchronizedList(new ArrayList<UUID>());
    this.unconfirmedMessages = Collections.synchronizedMap(new HashMap<UUID, Message>());
    this.addresses = Collections.synchronizedMap(new HashMap<InetSocketAddress, Long>());
    this.recieveSocket = new DatagramSocket(this.nodePort);
    this.sendSocket = new DatagramSocket(this.nodePort + 1);
  }
  private void closeConnection(){
    this.sendSocket.close();
    this.recieveSocket.close();
  }
  private boolean connect(InetSocketAddress address) throws IOException{
    sendMessage(ChatNode.MessageType.NUR, nodeName, parentAddress);
    addresses.put(address, System.currentTimeMillis());
    String[] dataArray = new String[3]; dataArray[0] = ""; dataArray[1] = "";
    int tries = 0;
    this.recieveSocket.setSoTimeout(2000);
    do{
      try{
        if(tries == 5) break;
        ++tries;
        DatagramPacket packet = new DatagramPacket(new byte[256], 256);
        this.recieveSocket.receive(packet);
        if(lossPercent > random.nextInt(101)) continue;
        String data = new String(packet.getData(), packet.getOffset(), packet.getLength());
        dataArray = data.split(":");
      }
      catch(SocketTimeoutException e){

      }
    }
    while(!dataArray[1].equals(ChatNode.MessageType.ACC.toString()));
    this.recieveSocket.setSoTimeout(0);
    if(tries < 5){
      unconfirmedMessages.remove(UUID.fromString(dataArray[2]));
      System.out.println("Connected to new parent node!");
    }
    else{ System.out.println("Connecting failed!"); return false;}
    return true;
  }
  private void disconnect() throws IOException{
    System.out.println("\n" + nodeName + " finished!");
    if(root == false){
      for(InetSocketAddress addr : addresses.keySet()){
        if(!addr.equals(parentAddress)){
          sendMessage(ChatNode.MessageType.FIN, nodeName + " finished!" + parentIP + "!" + parentAddress.getPort(), addr);
        }
        else{
          sendMessage(ChatNode.MessageType.FIN, nodeName + " finished!", addr);
        }
      }
    }
    else if(addresses.size() > 0){
      boolean flag = true ;
      InetSocketAddress a = null;
      for(InetSocketAddress addr : addresses.keySet()){
        if(flag){
          a = addr;
          flag = false;
        }
        UUID uuid = UUID.randomUUID();
        String message;
        if(!addr.equals(a)){
          sendMessage(ChatNode.MessageType.FIN, nodeName + " finished!" + a.getAddress().getHostName() + "!" + a.getPort(), addr);
        }
        else{
          sendMessage(ChatNode.MessageType.NRT, "", addr);
        }
      }
    }
    while(unconfirmedMessages.size() != 0){}
  }
  private void sendMessage(MessageType type, String text, InetSocketAddress address) throws IOException{
    UUID uuid = UUID.randomUUID();
    String message = uuid + ":" + type + ":" + text;
    this.sendSocket.send(new DatagramPacket(message.getBytes(), message.length(), address.getAddress(), address.getPort()));
    if(!type.equals(ChatNode.MessageType.ACC)){ unconfirmedMessages.put(uuid, new Message(address, System.currentTimeMillis(), message, 0)); }
  }
  private boolean isRecieved(UUID messageUUID){
    return recievedMessages.contains(messageUUID);
  }
  private boolean isNode(){ return !root; }
  private boolean isRoot(){ return  root; }
  private Thread getResender(){
    return new Thread(new Runnable(){
      @Override
      public void run(){
        try{
          while(true){
            Thread.sleep(3000);
            for(UUID u : unconfirmedMessages.keySet()){
              Message msg = unconfirmedMessages.get(u);
              if(System.currentTimeMillis() - msg.getTime() > 3000){
                if(msg.getTryNum() >= 5){
                  unconfirmedMessages.remove(u);
                  InetSocketAddress addr = msg.getAddress();
                  if(System.currentTimeMillis() - addresses.get(addr) > 16000){
                    addresses.remove(addr);
                    System.out.println("Stop sending to " + addr);
                    if(parentAddress.equals(addr)){
                      root = true;
                      parentAddress = null;
                      parentIP = null;
                      System.out.println("Im new root");
                    }
                  continue;
                  }
                }
                InetSocketAddress s = msg.getAddress();
                String m = msg.getMessage();
                System.out.println("Resend to " + s + " " + m);
                sendSocket.send(new DatagramPacket(m.getBytes(), m.length(), s.getAddress(), s.getPort()));
                unconfirmedMessages.put(u, new Message(s, System.currentTimeMillis(), m, msg.getTryNum() + 1));
              }
            }
          }
        }
        catch(Exception e){
          System.out.println("Resender " + e.getMessage());
          e.printStackTrace();
        }
      }
    });
  }
  private Thread getReciever(){
    return new Thread(new Runnable(){
      @Override
      public void run(){
        try{
          while(true){
            DatagramPacket packet = new DatagramPacket(new byte[256], 256);
            recieveSocket.receive(packet);
            if(lossPercent > random.nextInt(101)) continue;
            InetSocketAddress newPacket = new InetSocketAddress(packet.getAddress(), packet.getPort() - 1);
            addresses.put(newPacket, System.currentTimeMillis());
            String data = new String(packet.getData(), packet.getOffset(), packet.getLength());
            String[] dataArray = data.split(":");

            if(!dataArray[1].equals(ChatNode.MessageType.ACC.toString())){
              sendMessage(ChatNode.MessageType.ACC, dataArray[0], newPacket);
            }

            if(dataArray[1].equals(ChatNode.MessageType.TXT.toString())){
              if(!isRecieved(UUID.fromString(dataArray[0]))){
                if(recievedMessages.size() > 100){
                  recievedMessages.remove(0);
                }
                recievedMessages.add(UUID.fromString(dataArray[0]));
                System.out.println(dataArray[2]);
                for(InetSocketAddress addr : addresses.keySet()){
                  if(!addr.equals(newPacket)){
                    sendMessage(ChatNode.MessageType.TXT, dataArray[2], addr);
                  }
                }
              }
            }
            else if(dataArray[1].equals(ChatNode.MessageType.ACC.toString())){
              unconfirmedMessages.remove(UUID.fromString(dataArray[2]));
            }
            else if(dataArray[1].equals(ChatNode.MessageType.NUR.toString())){
              if(!isRecieved(UUID.fromString(dataArray[0]))){
                if(recievedMessages.size() > 100){
                  recievedMessages.remove(0);
                }
                recievedMessages.add(UUID.fromString(dataArray[0]));
                System.out.println(dataArray[2] + " connected!");
                for(InetSocketAddress addr : addresses.keySet()){
                  if(!addr.equals(newPacket)){
                    sendMessage(ChatNode.MessageType.TXT, dataArray[2] + " connected!", addr);
                  }
                }
              }
            }
            else if(dataArray[1].equals(ChatNode.MessageType.FIN.toString())){
              String[] newParent = dataArray[2].split("!");
              addresses.remove(newPacket);
              if(!isRecieved(UUID.fromString(dataArray[0]))){
                if(recievedMessages.size() > 100){
                  recievedMessages.remove(0);
                }
                recievedMessages.add(UUID.fromString(dataArray[0]));
                System.out.println(newParent[0]);
                if(newParent.length > 1){
                  parentIP = newParent[1];
                  parentAddress = new InetSocketAddress(InetAddress.getByName(newParent[1]), new Integer(newParent[2]));
                  connect(parentAddress);
                }
              }
            }
            else if(dataArray[1].equals(ChatNode.MessageType.NRT.toString())){
              addresses.remove(newPacket);
              if(!isRecieved(UUID.fromString(dataArray[0]))){
                if(recievedMessages.size() > 100){
                  recievedMessages.remove(0);
                }
                recievedMessages.add(UUID.fromString(dataArray[0]));
                parentAddress = null;
                parentIP = null;
                root = true;
                System.out.println("i'm new root");
              }
            }
          }
        }
        catch(Exception e){
          System.out.println("Reciever " + e.getMessage());
        }
      }
    });
  }
  private Thread getSender(){
    return new Thread(new Runnable(){
      @Override
      public void run(){
        try{
          Scanner scan = new Scanner(System.in);
          while(true){
            String text = scan.nextLine();
            for(InetSocketAddress address : addresses.keySet()){
              sendMessage(ChatNode.MessageType.TXT, nodeName + ">>" + text, address);
            }
          }
        }
        catch(IOException e){
          System.out.println("Sender " + e.getMessage());
        }
      }
    });
  }
  public static void printHelp(){
    System.out.println("java TreeChat {nodeName} {lossPercent} {nodePort} [parentIP] [parentPort]");
  }
  public static void main(String[] args){
    ChatNode chat = new ChatNode(args);
    chat.startChatting();
  }
}
/*
 *
 * Добавить попытки на ожидание ACC
 * Новая обработка завершения
 *
 */
