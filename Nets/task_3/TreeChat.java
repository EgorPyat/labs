import java.net.*;
import java.io.*;
import java.util.*;
import java.time.*;

public class TreeChat{
  private String nodeName;
  private int lossPercent;
  private int port;
  private String parentIP;
  private InetAddress parentAddr;
  private int parentPort;
  private boolean root;
  private DatagramSocket sendSock;
  private DatagramSocket reciSock;
  private Set<InetSocketAddress> addresses;
  private Map<UUID, Message> notConfMsg;
  private final String NEW = "0";
  private final String MSG = "1";
  private final String ACC = "2";
  private final String FIN = "3";
  private boolean connect = false;
  private Random random = new Random(LocalTime.now().hashCode());

  public TreeChat(String[] opts){
    try{
      this.nodeName = opts[0];
      this.lossPercent = new Integer(opts[1]);
      this.port = new Integer(opts[2]);
      if(opts.length == 5){
        this.parentIP = opts[3];
        this.parentPort = new Integer(opts[4]);
        this.root = false;
      }
      else{
        this.parentIP = null;
        this.parentPort = -1;
        this.root = true;
      }
      if(this.lossPercent < 0 || this.lossPercent > 100){
        System.out.println("Invalid option value!");
        TreeChat.printHelp();
      }
    }
    catch(NumberFormatException e){
      System.out.println("Error: " + e.getMessage());
      TreeChat.printHelp();
    }
    catch(Exception e){
      System.out.println("Error: " + e.getMessage());
    }
  }

  public void startChatting(){
    try{
      this.parentAddr = InetAddress.getByName(parentIP);
      this.reciSock = new DatagramSocket(this.port);
      this.sendSock = new DatagramSocket(this.port + 1);
      this.addresses = Collections.synchronizedSet(new HashSet<InetSocketAddress>());
      this.notConfMsg = Collections.synchronizedMap(new HashMap<UUID, Message>());
      Scanner scan = new Scanner(System.in);
      String message;

      Thread sender = new Thread(new Runnable(){
        @Override
        public void run(){
          try{
            while(true){
              String msg = scan.nextLine();
              for(InetSocketAddress addr : addresses){
                UUID uuid = UUID.randomUUID();
                String message = uuid + ":" + MSG + ":" + msg;
                sendSock.send(new DatagramPacket(message.getBytes(), message.length(), addr.getAddress(), addr.getPort()));
                notConfMsg.put(uuid, new Message(addr, System.currentTimeMillis(), message));
              }
            }
          }
          catch(IOException e){
            System.out.println(e.getMessage());
            // e.printStackTrace();
          }
        }
      }, "Sender");

      Thread receiver = new Thread(new Runnable(){
        @Override
        public void run(){
          try{
            while(true){
              DatagramPacket packet = new DatagramPacket(new byte[256], 256);
              reciSock.receive(packet);
              if(lossPercent > random.nextInt(101)) continue;
              addresses.add(new InetSocketAddress(packet.getAddress(), packet.getPort() - 1));
              String data = new String(packet.getData(), packet.getOffset(), packet.getLength());
              String[] dataArray = data.split(":");

              if(dataArray[1].equals(NEW)){
                UUID uuid = UUID.randomUUID();
                String message = uuid + ":" + ACC + ":" + dataArray[0];
                sendSock.send(new DatagramPacket(message.getBytes(), message.length(), packet.getAddress(), packet.getPort() - 1));
                System.out.println("New user# " + dataArray[2] + "!");
                for(InetSocketAddress addr : addresses){
                  if(!addr.equals(new InetSocketAddress(packet.getAddress(), packet.getPort() - 1))){
                    uuid = UUID.randomUUID();
                    message = uuid + ":" + MSG + ":" + "New user# " + dataArray[2] + "!";
                    sendSock.send(new DatagramPacket(message.getBytes(), message.length(), addr.getAddress(), addr.getPort()));
                    notConfMsg.put(uuid, new Message(addr, System.currentTimeMillis(), message));
                  }
                }
              }
              else if(dataArray[1].equals(MSG)){
                System.out.println(dataArray[2]);
                UUID uuid = UUID.randomUUID();
                String message = uuid + ":" + ACC + ":" + dataArray[0];
                sendSock.send(new DatagramPacket(message.getBytes(), message.length(), packet.getAddress(), packet.getPort() - 1));

                for(InetSocketAddress addr : addresses){
                  if(!addr.equals(new InetSocketAddress(packet.getAddress(), packet.getPort() - 1))){
                    uuid = UUID.randomUUID();
                    message = uuid + ":" + MSG + ":" + dataArray[2];
                    sendSock.send(new DatagramPacket(message.getBytes(), message.length(), addr.getAddress(), addr.getPort()));
                    notConfMsg.put(uuid, new Message(addr, System.currentTimeMillis(), message));
                  }
                }
              }
              else if(dataArray[1].equals(FIN)){

              }
              else if(dataArray[1].equals(ACC)){
                notConfMsg.remove(UUID.fromString(dataArray[2]));
              }

            }
          }
          catch(IOException e){
            System.out.println(e.getMessage());
            // e.printStackTrace();
          }
        }
      }, "Reciever");

      Thread resender = new Thread(new Runnable(){
        @Override
        public void run(){
          try{
            while(true){
              Thread.sleep(3000);
              for(UUID u : notConfMsg.keySet()){
                if(System.currentTimeMillis() - notConfMsg.get(u).getTime() > 3000){
                  System.out.println("Resend to " + notConfMsg.get(u).getAddress());
                  InetSocketAddress s = notConfMsg.get(u).getAddress();
                  String m = notConfMsg.get(u).getMessage();
                  sendSock.send(new DatagramPacket(m.getBytes(), m.length(), s.getAddress(), s.getPort()));
                  notConfMsg.put(u, new Message(s, System.currentTimeMillis(), m));
                }
              }
            }
          }
          catch(InterruptedException e){
            System.out.println(e.getMessage());
            // e.printStackTrace();
          }
          catch(IOException e){
            System.out.println(e.getMessage());
            // e.printStackTrace();
          }
        }
      }, "Resender");

      resender.start();

      if(this.root == false){
        addresses.add(new InetSocketAddress(parentAddr, parentPort));

        UUID uuid = UUID.randomUUID();
        message = uuid + ":" + this.NEW + ":" + this.nodeName;
        this.sendSock.send(new DatagramPacket(message.getBytes(), message.length(), parentAddr, parentPort));
        notConfMsg.put(uuid, new Message(new InetSocketAddress(parentAddr, parentPort), System.currentTimeMillis(), message));
        while(true){
          DatagramPacket packet = new DatagramPacket(new byte[256], 256);
          this.reciSock.receive(packet);
          String data = new String(packet.getData(), packet.getOffset(), packet.getLength());
          String[] dataArray = data.split(":");
          if(dataArray[1].equals(ACC) && uuid.toString().equals(dataArray[2])){
            notConfMsg.remove(uuid);
            System.out.println("Connected!");
            break;
          }
        }
      }

      receiver.start();
      sender.start();

      while(true){}
    }
    catch(SocketException e){
      System.out.println(e.getMessage());
      // e.printStackTrace();
    }
    catch(UnknownHostException e){
      System.out.println(e.getMessage());
      // e.printStackTrace();
    }
    catch(IOException e){
      System.out.println(e.getMessage());
      // e.printStackTrace();
    }
  }

  private class Message{
    private InetSocketAddress addr;
    private long time;
    private String msg;

    public Message(InetSocketAddress addr, long time, String msg){
      this.addr = addr;
      this.time = time;
      this.msg = msg;
    }

    public long getTime(){
      return this.time;
    }

    public String getMessage(){
      return this.msg;
    }

    public InetSocketAddress getAddress(){
      return this.addr;
    }
  }

  public static void printHelp(){
    System.out.println("java TreeChat {nodeName} {lossPercent} {nodePort} [parentIP] [parentPort]");
  }

  public static void main(String[] args){
    if(args.length == 3 || args.length == 5){
      TreeChat chat = new TreeChat(args);
      chat.startChatting();
    }
    else{
      System.out.println("Wrong number of options");
      TreeChat.printHelp();
    }
  }

}
