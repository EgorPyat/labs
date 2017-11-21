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
  private long task[];
  private String state;

  public Client(){
    this.clientUUID = UUID.randomUUID();
    this.task = new long[2];
  }

  public void setServer(String host, int hostPort){
    this.host = host;
    this.hostPort = hostPort;
  }

  public void doWork(){
    while(true){
      if(this.isTaskAvailable()){
        for(long i = this.task[0]; i <= this.task[1]; i++){
          String sequence = Long.toString(i, 4);
          sequence = sequence.replace('0', DNA[0]);
          sequence = sequence.replace('1', DNA[1]);
          sequence = sequence.replace('2', DNA[2]);
          sequence = sequence.replace('3', DNA[3]);
          StringBuilder seqBuild = new StringBuilder(sequence);
          for(int j = sequence.length(); j <= this.max_length; j++){
            System.out.println(sequence);
            if(this.calcMD5(sequence).equals(hash)){
              System.out.println("Found! Wanted sequence: " + sequence);
              this.sendResults("found", sequence);
              return;
            }
            seqBuild.reverse();
            seqBuild.append('A');
            seqBuild.reverse();
            sequence = seqBuild.toString();
          }
        }
        this.sendResults("fault", null);
      }
      else{ return; }
    }
  }

  private void sendResults(String state, String sequence){
    for(int i = 0; i < 5; i++){
      try{
        this.socket = new Socket(host, hostPort);
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
        this.socket = new Socket(host, hostPort);
        BufferedReader in  = new BufferedReader(new InputStreamReader(this.socket.getInputStream(), "UTF-8"));
        PrintWriter out = new PrintWriter(new OutputStreamWriter(this.socket.getOutputStream(), "UTF-8"), true);
        out.println(this.clientUUID.toString());
        out.println("wait");
        switch(in.readLine()){
          case "stop":
            System.out.println("No tasks to do!");
            in.close();
            out.close();
            this.socket.close();
            return false;
          case "work":
            this.hash = in.readLine();
            System.out.println("Get hash: " + this.hash);
            this.task[0] = new Long(in.readLine());
            this.task[1] = new Long(in.readLine());
            this.max_length = new Integer(in.readLine());
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

  public static void main(String[] args){
    Client client = new Client();
    client.setServer(args[0], new Integer(args[1]));
    client.doWork();
  }
}
