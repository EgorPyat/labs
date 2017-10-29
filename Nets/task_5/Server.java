import java.util.*;
import java.net.*;
import java.io.*;

public class Server{
  private final int MAX_LENGTH = 4;
  private Map<UUID, Long> clients;
  private List<Connection> connections;
  private List<int[]> tasks;
  private ServerSocket server;
  private int port;
  private String hash;

  public Server(int port){
    this.port = port;
    this.tasks = Collections.synchronizedList(new ArrayList<int[]>());
    this.clients = Collections.synchronizedMap(new HashMap<UUID, Long>());
    this.connections = Collections.synchronizedList(new ArrayList<Connection>());
    int sum = 0;
    for(int i = 1; i <= MAX_LENGTH; i++){
      tasks.add(new int[]{sum, sum + (int)Math.pow(4, i) - 1});
      sum += Math.pow(4, i);
    }
  }

  public void decrypt(String hash){
    this.hash = hash;
    try{
      this.server = new ServerSocket(this.port);
      while(true){
        try{
          Socket socket = this.server.accept();
          Connection con = new Connection(socket);
          con.start();
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
      try{
        BufferedReader in  = new BufferedReader(new InputStreamReader(this.socket.getInputStream(), "UTF-8"));
        PrintWriter out = new PrintWriter(new OutputStreamWriter(this.socket.getOutputStream(), "UTF-8"), true);
        UUID uuid = UUID.fromString(in.readLine());
        if(!clients.containsKey(uuid)){
          if(tasks.size() == 0){
            System.out.println("No tasks to send!");
            out.println("stop");
          }
          else{
            clients.put(uuid, System.currentTimeMillis());
            int[] task = tasks.remove(0);
            System.out.println("Task " + task[0] + " " + task[1]);
            System.out.println("Send task to " + uuid);
            out.println("work");
            out.println(hash);
            out.println(task[0]);
            out.println(task[1]);
            out.println(MAX_LENGTH);
          }
        }
        else{
          clients.remove(uuid);
          String state = in.readLine();
          switch(state){
            case "fault":
              System.out.println("Client#" + uuid + " fault.");
              break;
            case "found":
              tasks.clear();
              String sequence = in.readLine();
              System.out.println("Client#" + uuid + " found sequence: " + sequence);
              break;
          }
        }
        in.close();
        out.close();
        socket.close();
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
