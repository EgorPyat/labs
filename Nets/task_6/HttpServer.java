import java.util.*;
import java.net.*;
import java.io.*;
import org.json.*;

public class HttpServer{
  private ServerSocket socket;
  private int port;
  private List<Connection> connections;

  public HttpServer(int port){
    try{
      this.port = port;
      this.socket = new ServerSocket(this.port);
      this.connections = Collections.synchronizedList(new ArrayList<Connection>());
    }
    catch(Exception e){
      System.err.println(e.getMessage());
    }
  }

  public void start(){
    try{
      while(true){
        Socket client = this.socket.accept();
        Connection con = new Connection(client);
        this.connections.add(con);
        con.start();
      }
    }
    catch(Exception e){
      System.err.println(e.getMessage());
    }
  }

  private class Connection extends Thread{
    private BufferedReader in;
		private PrintWriter out;
		private Socket socket;

    public Connection(Socket socket){
			this.socket = socket;

			try{
        this.in  = new BufferedReader(new InputStreamReader(this.socket.getInputStream(), "UTF-8"));
        this.out = new PrintWriter(new OutputStreamWriter(this.socket.getOutputStream(), "UTF-8"), true);
			}
      catch(IOException e){
				e.printStackTrace();
				this.close();
			}
		}

    private String[] getHeader(){
      String req;
      StringBuilder request = new StringBuilder();
      String[] header = null;
      try{
        while(true){
          req = in.readLine();
          request.append(req);
          if(req.isEmpty()) break;
          request.append("_");
        }
        req = request.toString();
        header = req.split("_");
      }
      catch(Exception e){
        System.err.println(e.getMessage());
      }

      return header;
    }

    private String getContent(int contentLength){
      int sum = 0;
      StringBuilder request = new StringBuilder();
      char[] buffer = new char[64];
      try{
        while(true){
          int r = in.read(buffer, 0, 64);
          request.append(buffer);
          sum += r;
          if(sum == contentLength) break;
        }
      }
      catch(Exception e){
        System.err.println(e.getMessage());
      }

      return request.toString();
    }

    private String getQuery(String[] header){
      return header[0].split(" ")[0];
    }

    private String[] getMethod(String[] header){
      return header[0].split(" ")[1].replaceFirst("/", "").split("/");
    }

    @Override
		public void run(){
			try{
        String[] header = this.getHeader();
        String content = this.getContent(new Integer((header[3].split(":"))[1].trim()));
        String query = getQuery(header);
        String[] method = getMethod(header);
        String methodType = method[0];
        String methodArg = method[1] == null ? null : method[1];

        switch(query){
          case "GET":
            switch(methodType){
              case "logout":
                break;
              case "users":
                break;
              case "messages":
                break;
            }
            break;
          case "POST":
            switch(methodType){
              case "login":
                break;
              case "messages":
                break;
            }
            break;
        }

        this.out.println("HTTP/1.1 200 OK\n");
        this.out.println("Content-Type:application/json\nContent-Length:8\n");
        this.out.println("{\"id\":1}");
      }
      catch(Exception e){
        System.err.println(e.getMessage());
        e.printStackTrace();
      }
    }

    public void close(){
      try{
        this.in.close();
        this.out.close();
        this.socket.close();
      }
      catch(Exception e){
        System.err.println(e.getMessage());
      }
    }
  }

  public static void main(String[] args){
    try{
      HttpServer s = new HttpServer(3000);
      s.start();
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
