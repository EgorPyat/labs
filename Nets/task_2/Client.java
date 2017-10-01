import java.net.*;
import java.io.*;
import java.util.*;

public class Client{
	private Socket socket;
	private File file;

  public Client(String fileName){
    try{
			this.file = new File(fileName);
			if(!file.exists()){
	      System.out.println("No such file: " + fileName + "!");
	      return;
	    }
      this.socket = new Socket("localhost", 3000);
			this.send(fileName);
    }
    catch(IOException e){
      System.err.println(e.getMessage());
      // e.printStackTrace();
    }
  }
  private void send(String fileName){
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

				req = in.readLine();
				if(req.equals("1")) System.out.println("Upload success!");
				else System.out.println("Upload error!");
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
