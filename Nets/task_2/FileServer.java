import java.net.*;
import java.lang.*;
import java.io.*;
import java.util.*;

public class FileServer{
  private List<Connection> connections = Collections.synchronizedList(new ArrayList<Connection>());
	private ServerSocket server;

  public FileServer(){
    File folder = new File("downloads");

    if (!folder.exists()) {
      folder.mkdir();
    }

    try{
			this.server = new ServerSocket(3000);

			while(true){
				try{
					Socket socket = this.server.accept();
					Connection con = new Connection(socket);
					this.connections.add(con);
					con.start();
				}
				catch(SocketException | EOFException e){
          System.err.println(e.getMessage());
          // e.printStackTrace();
				}
			}
		}
    catch(IOException e){
      System.err.println(e.getMessage());
			// e.printStackTrace();
		}
    finally{
			this.closeAll();
		}
	}

	private void closeAll(){
		try{
			synchronized(connections){
				Iterator<Connection> iter = connections.iterator();
				while(iter.hasNext()){
					((Connection)iter.next()).close();
				}
			}

      this.server.close();
		}
    catch(IOException e){
      System.err.println(e.getMessage());
      // e.printStackTrace();
		}
  }

  private class Connection extends Thread{
    private Socket socket;

    public Connection(Socket socket){
      this.socket = socket;
    }

    @Override
    public void run(){
      String fileName;
      String fileSize;
      File file;

      try{
        BufferedReader in  = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
        PrintWriter out = new PrintWriter(this.socket.getOutputStream(), true);

        fileName = in.readLine();
        fileSize = in.readLine();
        System.out.println("File: " + fileName + " | " + "Size: " + fileSize + " bytes");

        file = new File("./downloads/" + fileName);

        if(file.exists()){
          System.out.println("File: " + fileName + " exists!");
          out.println("0");
        }
        else{
          out.println("1");
          InputStream inp = this.socket.getInputStream();
          FileOutputStream filOut = new FileOutputStream(file);
          byte[] buffer = new byte[2];
          int r = 0;
          double speed = 0.0;
          long time = System.currentTimeMillis();

          for(int i = 0; i < new Integer(fileSize);){
            r = inp.read(buffer);
            if(r == -1){
              System.out.println("Recieving Error! Download cancelled!");
              file.delete();
              break;
            }
            i += r;
            speed +=r;

            filOut.write(buffer, 0, r);

            if(System.currentTimeMillis() - time > 3000){
              time = System.currentTimeMillis();
              System.out.println("File: " + fileName + " | Speed, kB/s: " + String.format("%.2f", speed / 1024 / 3) + " | Now(bytes): " + r);
              speed = 0;
            }
          }
          if(r != -1){
            System.out.println("File: " + fileName + " - " + "Download complete!");
            out.println("1");
          }
        }
      }
      catch(IOException e){
        System.err.println(e.getMessage());
        // e.printStackTrace();
      }

      this.close();
    }

    public void close(){
      try{
				this.socket.close();

				FileServer.this.connections.remove(this);
			}
      catch(Exception e){
				System.err.println(e.getMessage());
        // e.printStackTrace();
			}
		}

  }

  public static void main(String[] args){
    FileServer fs = new FileServer();
  }

}
