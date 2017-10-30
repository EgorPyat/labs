import java.util.*;
import java.net.*;
import java.io.*;

public class Server{
  private final int MAX_LENGTH = 8;
  private Map<UUID, int[]> clients;
  private List<int[]> tasks;
  private Map<int[], Long> unconfirmedTasks;
  private ServerSocket server;
  private int port;
  private String hash;

  public Server(int port){
    this.port = port;
    this.tasks = Collections.synchronizedList(new ArrayList<int[]>());
    this.unconfirmedTasks = Collections.synchronizedMap(new HashMap<int[], Long>());
    this.clients = Collections.synchronizedMap(new HashMap<UUID, int[]>());
    int sum = 0;
    for(int i = 1; i <= MAX_LENGTH; i++){
      tasks.add(new int[]{sum, sum + (int)Math.pow(4, i) - 1});
      sum += Math.pow(4, i);
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
            System.out.println("resen");
            for(int[] t : unconfirmedTasks.keySet()){
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
          catch(NullPointerException e){

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
          // System.out.println("d");
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
            int[] task;
            // synchronized(tasks){
              if(tasks.size() != 0){ task = tasks.remove(0); }
              else{ task = null; }
            // }
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
            int [] t = clients.get(uuid);
            System.out.println("Client#" + uuid + " fault: " + t[0] + " " + t[1]);
            break;
        }
        this.socket.close();

        // System.out.println(1);
        // if(!clients.containsKey(uuid)){
        //   System.out.println(2);
        //   if(tasks.size() == 0){
        //     System.out.println("No tasks to send!");
        //     out.println("stop");
        //   }
        //   else{
        //     int[] task;
        //     synchronized(tasks){
        //       task = tasks.remove(0);
        //     }
        //     System.out.println("Task " + task[0] + " " + task[1]);
        //     System.out.println("Send task to " + uuid);
        //     out.println("work");
        //     out.println(hash);
        //     out.println(task[0]);
        //     out.println(task[1]);
        //     out.println(MAX_LENGTH);
        //     clients.put(uuid, task);
        //     unconfirmedTasks.put(task, System.currentTimeMillis());
        //   }
        // }
        // else{
        //   System.out.println(3);
        //   int [] t = clients.get(uuid);
        //   unconfirmedTasks.remove(clients.get(uuid));
        //   // tasks.clear();
        //   clients.remove(uuid);
        //   // String state = in.readLine();
        //   System.out.println("serv wait state");
        //   switch(in.readLine()){
        //     case "fault":
        //     System.out.println(4);
        //       System.out.println("Client#" + uuid + " fault: " + t[0] + " " + t[1]);
        //       break;
        //     case "found":
        //       System.out.println(5);
        //
        //       tasks.clear();
        //       String sequence = in.readLine();
        //       System.out.println("Client#" + uuid + " found sequence: " + sequence);
        //       break;
        //   }
        // }
        // System.out.println("close");
      }
      catch (IOException e){
        System.err.println(e.getMessage());
      }
      finally{
      }
    }
  }
  public static void main(String[] args){
    Server server = new Server(new Integer(args[0]));
    server.decrypt(args[1]);
  }
}
