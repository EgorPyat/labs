import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.net.Socket;
import java.util.UUID;
import java.util.List;
import java.util.ArrayList;

public class Client{
  private UUID clientUUID;
  private String hash;
  private int task[];
  private Socket socket;
  private String host;
  private int hostPort;
  private char[] DNA = {'A', 'C', 'G', 'T'};
  private int max_length;
  private List<Task> tasks;

  public Client(){
    this.clientUUID = UUID.randomUUID();
    this.tasks = new ArrayList<Task>();
  }

  public void setServer(String host, int hostPort){
    this.host = host;
    this.hostPort = hostPort;
  }

  public void doWork(){

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
          sum += Math.pow(4, i);
          startSequenceNum = sum;
        }
      }
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
  }
  public static void main(String[] args){
    System.out.println(new Client().calcMD5(args[0]));
    System.out.println(new Client().getSequence(2, 4));
  }
}
