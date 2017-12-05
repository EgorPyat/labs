import java.net.*;

public class Main{
  public static void main(String[] args){
    try{
      MyServerSocket s = new MyServerSocket(3000);
      new Thread(new Runnable(){
        public void run(){
          try{
            Thread.sleep(500);
            MyClientSocket s = new MyClientSocket();
            s.connect(InetAddress.getByName("localhost"), 3000);
            s.send("hello".getBytes());
            s.recieve(new byte[1024]);
            s.close();
          }
          catch(Exception e){
            System.err.println(e.getMessage());
          }
        }
      }).start();
      s.listen(0);
      MyClientSocket c = s.accept();
      c.recieve(new byte[1024]);
      c.send("hi".getBytes());
      c.close();
    }
    catch(Exception e){
      System.err.println(e.getMessage());
      // e.printStackTrace();
    }
  }
}
