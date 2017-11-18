import java.net.*;

public class Main{
  public static void main(String[] args){
    try{
      MyServerSocket s = new MyServerSocket(3000);
      new Thread(new Runnable(){
        public void run(){
          try{
            Thread.sleep(5000);
            MyClientSocket s = new MyClientSocket();
            System.out.println(1);
            s.connect(InetAddress.getByName("localhost"), 3000);
          }
          catch(Exception e){
            System.err.println(e.getMessage());
          }
        }
      }).start();
      s.listen(0);
      System.out.println(2);
      MyClientSocket c = s.accept();
    }
    catch(Exception e){
      System.err.println(e.getMessage());
      // e.printStackTrace();
    }
  }
}
