import java.math.BigInteger;
import java.security.*;
import java.net.*;
import java.util.*;
import java.io.*;

public class Client{
  private UUID clientUUID;
  private String hash;
  private Socket socket;
  private String host;
  private int hostPort;
  private char[] DNA = {'A', 'C', 'G', 'T'};
  private int max_length;
  private int task[];
  private List<Task> tasks;
  private String state;

  public Client(){
    this.clientUUID = UUID.randomUUID();
    this.tasks = new ArrayList<Task>();
    this.task = new int[2];
  }

  public void setServer(String host, int hostPort){
    this.host = host;
    this.hostPort = hostPort;
  }

  public void doWork(){
    while(true){
      if(this.isTaskAvailable()){
        this.createTasks(this.task[0], this.task[1]);
        for(Task t : this.tasks){
          System.out.println(t.getStartSequenceNum() + " " + t.getFinalSequenceNum());
          for (int num = t.getStartSequenceNum(), end = t.getFinalSequenceNum(); num <= end; num++){
            String sequence = this.getSequence(t.getLength(), num);
            System.out.println(sequence);
            if(this.calcMD5(sequence).equals(hash)){
              System.out.println("Found! Wanted sequence: " + sequence);
              this.sendResults("found", sequence);
              return;
            }
          }
        }
        this.tasks.clear();
        this.sendResults("fault", null);
      }
      else{ return; }
    }
  }
  private void sendResults(String state, String sequence){
    for(int i = 0; i < 5; i++){
      try{
        this.socket = new Socket();
        this.socket.connect(new InetSocketAddress(host, hostPort));
        PrintWriter out = new PrintWriter(new OutputStreamWriter(this.socket.getOutputStream(), "UTF-8"), true);
        out.println(this.clientUUID.toString());
        out.println(state);
        if(null != sequence){ out.println(sequence); }
        out.close();
        this.socket.close();
        return;
      }
      catch(IOException e) {
        System.out.println(e.getMessage());
      }
      try{
        Thread.sleep(3000);
      }
      catch (InterruptedException e) {
        System.out.println(e.getMessage());
      }
    }
  }
  private boolean isTaskAvailable(){
    for(int i = 0; i < 5; i++){
      try{
        this.socket = new Socket();
        this.socket.connect(new InetSocketAddress(host, hostPort));
        BufferedReader in  = new BufferedReader(new InputStreamReader(this.socket.getInputStream(), "UTF-8"));
        PrintWriter out = new PrintWriter(new OutputStreamWriter(this.socket.getOutputStream(), "UTF-8"), true);
        out.println(this.clientUUID.toString());
        this.state = in.readLine();
        switch(state){
          case "stop":
          System.out.println("No tasks to do!");
          return false;
          case "work":
          this.hash = in.readLine();
          System.out.println("Get hash: " + this.hash);
          this.task[0] = new Integer(in.readLine());
          this.task[1] = new Integer(in.readLine());
          this.max_length = new Integer(in.readLine());
          System.out.println("Get task");
          break;
        }
        in.close();
        out.close();
        this.socket.close();
        return true;
      }
      catch(UnknownHostException e){
        System.out.println(e.getMessage());
        break;
      }
      catch(IOException e){
        System.out.println(e.getMessage());
      }
      try{
        Thread.sleep(3000);
      }
      catch(InterruptedException e){
        System.out.println(e.getMessage());
      }
    }
    return false;
  }
  private void createTasks(int startSequenceNum, int finalSequenceNum){
    int sum = 0;
    for(int i = 1; i <= max_length; i++){
      if(startSequenceNum >= sum && startSequenceNum < sum + Math.pow(4, i)){
        if(finalSequenceNum >= sum && finalSequenceNum < sum + Math.pow(4, i)){
          this.tasks.add(new Task(startSequenceNum - sum, finalSequenceNum - sum, i));
          break;
        }
        else{
          this.tasks.add(new Task(startSequenceNum - sum, (int)Math.pow(4, i) - 1, i));
          startSequenceNum = sum + (int)Math.pow(4, i);
        }
      }
      sum += Math.pow(4, i);
    }
  }
  private String getSequence(int length, int sequenceNumber){
    StringBuilder sequence = new StringBuilder();
    for(int i = 0; i < length; i++){
      sequence.append(DNA[sequenceNumber % 4]);
      sequenceNumber /= 4;
    }
    return sequence.reverse().toString();
  }
  private String calcMD5(String text){
    MessageDigest md5 = null;
    try{
      md5 = MessageDigest.getInstance("MD5");
      md5.update(text.getBytes(), 0, text.length());
    }
    catch(NoSuchAlgorithmException e){
      System.out.println(e.getMessage());
    }
    return new BigInteger(1, md5.digest()).toString(16);
  }
  private class Task{
      int start;
      int finish;
      int length;
      public Task(int start, int finish, int length){
        this.start = start;
        this.finish = finish;
        this.length = length;
      }
      public int getStartSequenceNum(){
        return this.start;
      }
      public int getFinalSequenceNum(){
        return this.finish;
      }
      public int getLength(){
        return this.length;
      }
  }
  public static void main(String[] args){
    Client client = new Client();
    client.setServer(args[0], new Integer(args[1]));
    client.doWork();
  }
}
