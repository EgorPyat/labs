import java.net.*;
import java.io.*;

public class Main{
  public static void main(String[] args){
    // StringBuilder b = new StringBuilder();
    // for(int i = 0; i < 2000; i++){
    //   b.append("a");
    // }
    // String r = b.toString();
    // System.out.println(r + " " + r.length());
    try{
      MyServerSocket s = new MyServerSocket(3000);
      new Thread(new Runnable(){
        public void run(){
          try{
            Thread.sleep(500);
            MyClientSocket s = new MyClientSocket();
            s.connect(InetAddress.getByName("localhost"), 3000);
            File file = new File("MyClientSocket.java");
            s.send(file.getName().getBytes());
            FileInputStream fileIn = new FileInputStream(file);
            System.out.println("file " + file.length());
            byte[] buffer = new byte[(int)file.length()];
            int t = fileIn.read(buffer);
            System.out.println(t);
            s.send(buffer);
            s.recieve(new byte[2024]);
            s.close();
          }
          catch(Exception e){
            // System.err.println(e.getMessage());
            e.printStackTrace();
          }
        }
      }).start();
      s.listen(0);
      File folder = new File("downloads");

      if(!folder.exists()){
        folder.mkdir();
      }
      byte[] buffer = new byte[100];
      MyClientSocket c = s.accept();
      int dataLen = c.recieve(buffer);
      String fileName = new String(buffer, 0, dataLen);

      File file = new File("./downloads/" + fileName);
      buffer = new byte[16384];
      dataLen = c.recieve(buffer);
      FileOutputStream filOut = new FileOutputStream(file);
      filOut.write(buffer, 0, dataLen);

      c.send("hi".getBytes());
      c.close();
    }
    catch(Exception e){
      // System.err.println(e.getMessage());
      e.printStackTrace();
    }
  }
}
