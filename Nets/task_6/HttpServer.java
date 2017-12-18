import java.util.*;
import java.net.*;
import java.io.*;
import java.util.concurrent.atomic.AtomicInteger;
import org.json.*;

public class HttpServer{
  private ServerSocket socket;
  private int port;
  private List<Connection> connections;
  private AtomicInteger userID = new AtomicInteger(-1);
  private AtomicInteger msgID = new AtomicInteger(-1);
  private Map<Integer, Integer> usersIDs;
  private Map<Integer, String> usersNames;
  private Map<String, Long> usersOnline;
  private Map<Integer, Integer> usersMessages;
  private List<String> messages;

  public HttpServer(int port){
    try{
      this.port = port;
      this.socket = new ServerSocket(this.port);
      this.connections = Collections.synchronizedList(new ArrayList<Connection>());
      this.usersIDs = Collections.synchronizedMap(new HashMap<Integer, Integer>());
      this.usersNames = Collections.synchronizedMap(new HashMap<Integer, String>());
      this.usersMessages = Collections.synchronizedMap(new HashMap<Integer, Integer>());
      this.usersOnline = Collections.synchronizedMap(new HashMap<String, Long>());
      this.messages = Collections.synchronizedList(new LinkedList<String>());

      new Thread(new Runnable(){
        @Override
        public void run(){
          int tok = 0;
          while(true){
            // System.out.println("12");
            for(String name : usersOnline.keySet()){
              long time = usersOnline.get(name);
              // System.out.println(name + " " + time);
              if(time > 0){
                if(System.currentTimeMillis() - time > 10000){
                  for(int t : usersNames.keySet()){
                    if(usersNames.get(t).equals(name)){
                      tok = t;
                      break;
                    }
                  }
                  HttpServer.this.usersNames.remove(tok);
                  HttpServer.this.usersOnline.remove(HttpServer.this.usersNames.get(tok));
                  HttpServer.this.usersNames.put(tok, name + ":");
                  HttpServer.this.usersOnline.put(name + ":", -1L);
                  // System.out.println("Hard logout! " + name + " " + tok);
                }
              }
              // else{
              //   System.out.println("minus");
              // }
            }
          }
        }
      }, "Logouter").start();
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
        for(int i = 0; i < header.length; i++){
          System.out.println(header[i]);
        }
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
        String content = header.length == 3 ? null : this.getContent(new Integer((header[header.length - 1].split(":"))[1].trim()));
        String query = getQuery(header);
        String[] method = getMethod(header);
        String methodType = method[0];
        String methodArg = method.length == 1 ? null : method[1];

        switch(query){
          case "GET":
            switch(methodType){
              case "logout":
                System.out.println("logOut");
                String token = header[2].split(":")[1].trim();

                if(token.isEmpty()){
                  this.out.println("HTTP/1.1 401 Unauthorized\n");
                  this.out.println("WWW-Authenticate:Token realm='Token in the request is absent'\n");
                }
                else{
                  int tok = new Integer(token);
                  if(HttpServer.this.usersIDs.containsKey(tok)){
                    String name = HttpServer.this.usersNames.get(tok);
                    if(HttpServer.this.usersOnline.get(name) > 0){
                      String cont = new JSONStringer().object().key("message").value("bye").endObject().toString();
                      this.out.println("HTTP/1.1 200 OK");
                      this.out.println("Content-Type:application/json\nContent-Length:" + (cont.length() + 1) + "\n");
                      this.out.println(cont);
                      HttpServer.this.usersNames.remove(tok);
                      HttpServer.this.usersOnline.remove(HttpServer.this.usersNames.get(tok));
                      HttpServer.this.usersNames.put(tok, name + ":");
                      HttpServer.this.usersOnline.put(name + ":", 0L);
                      System.out.println(name + " logout!");
                    }
                    else{
                      this.out.println("HTTP/1.1 403 Forbidden\n");
                      this.out.println("WWW-Authenticate:Token realm='Token is expired'\n");
                    }
                  }
                  else{
                    this.out.println("HTTP/1.1 403 Forbidden\n");
                    this.out.println("WWW-Authenticate:Token realm='Token is unknown'\n");
                  }
                }
                break;
              case "users":
                String tokeh = header[2].split(":")[1].trim();

                if(tokeh.isEmpty()){
                  this.out.println("HTTP/1.1 401 Unauthorized\n");
                  this.out.println("WWW-Authenticate:Token realm='Token in the request is absent'\n");
                }
                else{
                  int tok = new Integer(tokeh);

                  if(HttpServer.this.usersIDs.containsKey(tok)){
                    if(HttpServer.this.usersOnline.get(HttpServer.this.usersNames.get(tok)) > 0){
                      String start = "{\"users\":[";
                      String end = "]}";

                      for(int t : HttpServer.this.usersIDs.keySet()){
                        String name = HttpServer.this.usersNames.get(t);
                        String status;
                        if(HttpServer.this.usersOnline.get(name) > 0){
                          status = "true";
                        }
                        else if(HttpServer.this.usersOnline.get(name) == 0){
                          status = "false";
                        }
                        else{
                          status = "null";
                        }
                        start += "{\"id\":" + HttpServer.this.usersIDs.get(t) + ",\"username\":\"" + name.split(":")[0] + "\",\"status\":\"" + status + "\"},";
                      }
                      start += end;
                      start = start.replace(",]", "]");

                      HttpServer.this.usersOnline.put(HttpServer.this.usersNames.get(tok), System.currentTimeMillis());

                      this.out.println("HTTP/1.1 200 OK");
                      this.out.println("Content-Type: application/json\ncontent-length: " + (start.length() + 1) + "\n");
                      this.out.println(start);
                    }
                    else{
                      this.out.println("HTTP/1.1 403 Forbidden\n");
                      this.out.println("WWW-Authenticate:Token realm='Token is expired'\n");
                    }
                  }
                  else{
                    this.out.println("HTTP/1.1 403 Forbidden\n");
                    this.out.println("WWW-Authenticate:Token realm='Token is unknown'\n");
                  }
                }
                break;
              case "messages":
                String toke = header[2].split(":")[1].trim();

                if(toke.isEmpty()){
                  this.out.println("HTTP/1.1 401 Unauthorized\n");
                  this.out.println("WWW-Authenticate:Token realm='Token in the request is absent'\n");
                }
                else{
                  int tok = new Integer(toke);

                  if(HttpServer.this.usersIDs.containsKey(tok)){
                    if(HttpServer.this.usersOnline.get(HttpServer.this.usersNames.get(tok)) > 0){
                      String start = "{\"messages\":[";
                      String end = "]}";
                      int last = HttpServer.this.messages.size();
                      for(int i = HttpServer.this.usersMessages.get(tok); i < last; i++){
                        String msg = HttpServer.this.messages.get(i);
                        start += msg + ",";
                      }
                      start += end;
                      start = start.replace(",]", "]");
                      HttpServer.this.usersMessages.put(tok, last);

                      HttpServer.this.usersOnline.put(HttpServer.this.usersNames.get(tok), System.currentTimeMillis());

                      this.out.println("HTTP/1.1 200 OK");
                      this.out.println("Content-Type:application/json\nContent-Length:" + (start.length() + 1) + "\n");
                      this.out.println(start);
                    }
                    else{
                      this.out.println("HTTP/1.1 403 Forbidden\n");
                      this.out.println("WWW-Authenticate:Token realm='Token is expired'\n");
                    }
                  }
                  else{
                    this.out.println("HTTP/1.1 403 Forbidden\n");
                    this.out.println("WWW-Authenticate:Token realm='Token is unknown'\n");
                  }
                }
                break;
            }
            break;
          case "POST":
            switch(methodType){
              case "login":
                JSONObject obj = new JSONObject(content);
                String username = obj.getString("username");

                if(HttpServer.this.usersNames.containsValue(username)){
                  if(HttpServer.this.usersOnline.get(username) > 0){
                    this.out.println("HTTP/1.1 401 Unauthorized");
                    this.out.println("WWW-Authenticate:Token realm='Username is already in use'\n");
                  }
                  else{
                    int id = HttpServer.this.userID.incrementAndGet();
                    int tok = 0 + (int)(Math.random() * 100000000);
                    HttpServer.this.usersIDs.put(tok, id);
                    HttpServer.this.usersNames.put(tok, username);
                    HttpServer.this.usersOnline.put(username, System.currentTimeMillis());
                    HttpServer.this.usersMessages.put(tok, 0);
                    String cont = new JSONStringer().object().key("id").value(id).key("username").value(username).key("online").value("true").key("token").value(tok).endObject().toString();
                    this.out.println("HTTP/1.1 200 OK");
                    this.out.println("Content-Type:application/json\nContent-Length:" + cont.length() + "\n");
                    this.out.println(cont);
                    System.out.println("New user: " + username + "!");
                  }
                }
                else{
                  int id = HttpServer.this.userID.incrementAndGet();
                  int tok = 0 + (int)(Math.random() * 100000000);
                  HttpServer.this.usersIDs.put(tok, id);
                  HttpServer.this.usersNames.put(tok, username);
                  HttpServer.this.usersOnline.put(username, System.currentTimeMillis());
                  HttpServer.this.usersMessages.put(tok, 0);
                  String cont = new JSONStringer().object().key("id").value(id).key("username").value(username).key("online").value("true").key("token").value(tok).endObject().toString();
                  this.out.println("HTTP/1.1 200 OK");
                  this.out.println("Content-Type:application/json\nContent-Length:" + (cont.length() + 1) + "\n");
                  this.out.println(cont);
                  System.out.println("New user: " + username + "!");
                }
                break;
              case "messages":
                String token = header[2].split(":")[1].trim();

                if(token.isEmpty()){
                  this.out.println("HTTP/1.1 401 Unauthorized");
                  this.out.println("WWW-Authenticate:Token realm='Token in the request is absent'\n");
                }
                else{
                  int tok = new Integer(token);

                  if(HttpServer.this.usersIDs.containsKey(tok)){
                    if(HttpServer.this.usersOnline.get(HttpServer.this.usersNames.get(tok)) > 0){
                      JSONObject msg = new JSONObject(content);
                      String message = msg.getString("message");
                      int id = HttpServer.this.msgID.incrementAndGet();
                      String response = new JSONStringer().object().key("id").value(id).key("message").value(message).endObject().toString();

                      HttpServer.this.usersOnline.put(HttpServer.this.usersNames.get(tok), System.currentTimeMillis());

                      this.out.println("HTTP/1.1 200 OK");
                      this.out.println("Content-Type:application/json\nContent-Length:" + (response.length() + 1) + "\n");
                      this.out.println(response);
                      response = new JSONStringer().object().key("id").value(id).key("message").value(message).key("author").value(HttpServer.this.usersIDs.get(tok)).endObject().toString();
                      HttpServer.this.messages.add(response);
                    }
                    else{
                      this.out.println("HTTP/1.1 403 Forbidden\n");
                      this.out.println("WWW-Authenticate:Token realm='Token is expired'\n");
                    }
                  }
                  else{
                    this.out.println("HTTP/1.1 403 Forbidden\n");
                    this.out.println("WWW-Authenticate:Token realm='Token is unknown'\n");
                  }
                }
                break;
            }
            break;
        }
      }
      catch(Exception e){
        System.err.println(e.getMessage());
        e.printStackTrace();
      }

      this.close();
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
      HttpServer s = new HttpServer(3001);
      s.start();
    }
    catch(Exception e){
      System.err.println(e.getMessage());
    }
  }
}

/*
 * rand tokens              +
 * better messages response +
 * better messages look     +
 * preventive user logout   +
 */
