import java.net.*;
import java.io.*;
import java.util.*;

public class Client{
	private Socket socket;

  public Client(String fileName){
    try{
      this.socket = new Socket("localhost", 3000);
    }
    catch(IOException e){
      System.err.println(e.getMessage());
      // e.printStackTrace();
    }
    this.send(fileName);
  }
  private void send(String fileName){
    File file = new File(fileName);
    if(!file.exists()){
      System.out.println("No such file: " + fileName + "!");
      return;
    }
    try{
      BufferedReader in  = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
      PrintWriter out = new PrintWriter(this.socket.getOutputStream(), true);

      out.println(file.getName());
      out.println(file.length());
      String req = in.readLine();
      if(req.equals("1")){
        FileInputStream fileIn = new FileInputStream(file);
        OutputStream outp = this.socket.getOutputStream();
        byte[] buffer = new byte[256];

        while(fileIn.available() > 0){
          int r = fileIn.read(buffer);
          outp.write(buffer, 0, r);
        }
      }
      else{
        System.out.println("Server is not available to download file: " + fileName + "!");
      }
      this.socket.close();
    }
    catch(IOException e){
      System.err.println(e.getMessage());
      // e.printStackTrace();
    }
  }

  public static void main(String[] args){
    if(args.length == 0) return;
    try{
      Client client = new Client(args[0]);
    }
    catch(NullPointerException e){
      System.err.println(e.getMessage());
    }
  }
}
