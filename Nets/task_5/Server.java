import java.util.*;
import java.net.*;
import java.io.*;

public class Server{
  private final int MAX_LENGTH = 8;
  private final int STEP = 1024;
  private Map<UUID, long[]> clients;
  private List<long[]> tasks;
  private Map<long[], Long> unconfirmedTasks;
  private ServerSocket server;
  private int port;
  private String hash;

  public Server(int port){
    this.port = port;
    this.tasks = Collections.synchronizedList(new ArrayList<long[]>());
    this.unconfirmedTasks = Collections.synchronizedMap(new HashMap<long[], Long>());
    this.clients = Collections.synchronizedMap(new HashMap<UUID, long[]>());
    long sum = 0;
    for(; sum < Math.pow(4, MAX_LENGTH); sum += STEP){
      tasks.add(new long[]{sum, sum + STEP - 1});
    }
  }

  public void decrypt(String hash){
    this.hash = hash;
    Thread resender = new Thread(new Runnable(){
      @Override
      public void run(){
        while(!(tasks.isEmpty() && unconfirmedTasks.isEmpty())){
          try{
            Thread.sleep(3000);
            for(long[] t : unconfirmedTasks.keySet()){
              if(System.currentTimeMillis() - unconfirmedTasks.get(t) > 10000){
                tasks.add(t);
                unconfirmedTasks.remove(t);
                System.out.println("Task returned!");
              }
            }
          }
          catch(InterruptedException e){
            System.out.println(e.getMessage());
          }
        }
      }
    });
    resender.start();
    try{
      this.server = new ServerSocket(this.port);
      this.server.setSoTimeout(1000);
      while(!(tasks.isEmpty() && unconfirmedTasks.isEmpty())){
        try{
          Socket socket = this.server.accept();
          System.out.println("wait");
          Connection con = new Connection(socket);
          con.start();
        }
        catch(SocketTimeoutException e){
          continue;
        }
        catch(SocketException | EOFException e){
          System.err.println(e.getMessage());
        }
      }
    }
    catch(IOException e){
      System.err.println(e.getMessage());
    }
  }

  private class Connection extends Thread{
    private Socket socket;
    public Connection(Socket socket){
      this.socket = socket;
    }
    @Override
    public void run(){
      try
      (
        BufferedReader in  = new BufferedReader(new InputStreamReader(this.socket.getInputStream(), "UTF-8"));
        PrintWriter out = new PrintWriter(new OutputStreamWriter(this.socket.getOutputStream(), "UTF-8"), true);
      )
      {
        UUID uuid = UUID.fromString(in.readLine());
        String state = in.readLine();
        switch(state){
          case "wait":
            long[] task;
            if(tasks.size() != 0){ task = tasks.remove(0); }
            else{ task = null; }
            if(task != null){
              System.out.println("Task " + task[0] + " " + task[1]);
              System.out.println("Send task to " + uuid);
              clients.put(uuid, task);
              unconfirmedTasks.put(task, System.currentTimeMillis());
              out.println("work");
              out.println(hash);
              out.println(task[0]);
              out.println(task[1]);
              out.println(MAX_LENGTH);
            }
            else{
              System.out.println("No tasks to send!");
              out.println("stop");
            }
            break;
          case "found":
            tasks.clear();
            unconfirmedTasks.remove(clients.get(uuid));
            clients.remove(uuid);
            String sequence = in.readLine();
            System.out.println("Client#" + uuid + " found sequence: " + sequence);
            break;
          case "fault":
            unconfirmedTasks.remove(clients.get(uuid));
            long[] t = clients.get(uuid);
            System.out.println("Client#" + uuid + " fault: " + t[0] + " " + t[1]);
            break;
        }
        this.socket.close();
      }
      catch (IOException e){
        System.err.println(e.getMessage());
      }
    }
  }

  public static void main(String[] args){
    Server server = new Server(new Integer(args[0]));
    server.decrypt(args[1]);
  }
}
