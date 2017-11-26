import java.util.*;
import java.net.*;
import java.io.*;
import org.json.*;

public class HttpServer{
  public static void main(String[] args){
    try{
      ServerSocket ss = new ServerSocket(3000);
        Socket cs = ss.accept();
        System.out.println("Accept");
        BufferedReader in  = new BufferedReader(new InputStreamReader(cs.getInputStream(), "UTF-8"));
        PrintWriter out = new PrintWriter(new OutputStreamWriter(cs.getOutputStream(), "UTF-8"), true);
        String req;
        StringBuilder request = new StringBuilder();
        while(true){
          req = in.readLine();
          request.append(req);
          if(req.isEmpty()) break;
        }
        System.out.println(request.toString());
        System.out.println("Read header!");
        int sum = 0;
        request = new StringBuilder();
        char[] buffer = new char[64];
        while(true){
          int r = in.read(buffer, 0, 64);
          request.append(buffer);
          sum += r;
          if(sum == 15) break;
        }
        System.out.println(request.toString());

        out.println("HTTP/1.1 200 OK\n");
        out.println("Content-Type:application/json\nContent-Length:8\n");
        out.println("{\"id\":1}");

      in.close();
      out.close();
      cs.close();
      ss.close();
    }
    catch(Exception e){
      System.err.println(e.getMessage());
    }
    // String json = "{\"name\":\"egor\",\"id\":1}";
    // JSONObject obj = new JSONObject(json);
    // String name = obj.getString("name");
    // int id = obj.getInt("id");
    // System.out.println(name + id);
  }
}
