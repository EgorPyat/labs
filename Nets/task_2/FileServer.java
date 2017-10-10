import java.net.*;
import java.lang.*;
import java.io.*;
import java.util.*;

public class FileServer{

  public FileServer(){}

  private List<Connection> connections = new ArrayList<Connection>();
	private ServerSocket server;

  public void startDownload(){
    File folder = new File("downloads");

    if(!folder.exists()){
      folder.mkdir();
    }

    try{
      this.server = new ServerSocket(3000);

      Runtime.getRuntime().addShutdownHook(new Thread(){
        public void run(){
          System.out.println("\nServer work was interrupted!");
          closeAll();
        }
      });

			while(true){
				try{
					Socket socket = this.server.accept();
					Connection con = new Connection(socket);
					this.connections.add(con);
					con.start();
				}
				catch(SocketException | EOFException e){
          // System.err.println(e.getMessage());
          // e.printStackTrace();
				}
			}
		}
    catch(IOException e){
      // System.err.println(e.getMessage());
			// e.printStackTrace();
		}

	}

	private void closeAll(){
		try{
			Iterator<Connection> iter = connections.iterator();
			while(iter.hasNext()){
				iter.next().close(iter);
			}

      this.server.close();
		}
    catch(IOException e){
      // System.err.println(e.getMessage());
      // e.printStackTrace();
		}
  }

  private class Connection extends Thread{
    private Socket socket;
    File file;

    public Connection(Socket socket){
      this.socket = socket;
    }

    @Override
    public void run(){
      String fileName;
      String fileSize;

      try{
        BufferedReader in  = new BufferedReader(new InputStreamReader(this.socket.getInputStream(), "UTF-8"));
        PrintWriter out = new PrintWriter(new OutputStreamWriter(this.socket.getOutputStream(), "UTF-8"), true);

        fileName = in.readLine();
        fileSize = in.readLine();
        int index;

        if(fileName.charAt(fileName.length() - 1) == '/'){
          fileName = fileName.substring(0, fileName.length() - 2);
        }

        if((index = fileName.lastIndexOf('/')) != -1){
          fileName = fileName.substring(index);
        }

        System.out.println("File: " + fileName + " | " + "Size: " + fileSize + " bytes");

        file = new File("./downloads/" + fileName);

        if(file.exists() || new File(".").getFreeSpace() < new Integer(fileSize)){
          System.out.println("File: " + fileName + " can't be created!");
          out.println("0");
        }
        else{
          out.println("1");
          InputStream inp = this.socket.getInputStream();
          FileOutputStream filOut = new FileOutputStream(file);
          byte[] buffer = new byte[2];
          int r = 0;
          int iters = 1;
          double speed = 0.0;
          double midSpeed = 0.0;
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
              midSpeed += speed;
              time = System.currentTimeMillis();
              System.out.println("File: " + fileName + " | Speed, kB/s: " + String.format("%.2f", speed / 1024 / 3) + " | MidSpeed, kB/s: " + String.format("%.2f", midSpeed / 1024 / 3 / iters));
              speed = 0;
              ++iters;
            }
          }
          if(r != -1){
            System.out.println("File: " + fileName + " - " + "Download complete!");
            out.println("1");
          }
        }
      }
      catch(IOException e){
        // System.err.println(e.getMessage());
        // e.printStackTrace();
      }

      this.close();
    }

    public void close(Iterator<Connection> iter){
      try{
				this.socket.close();
        file.delete();
				iter.remove();
			}
      catch(Exception e){
				// System.err.println(e.getMessage());
        // e.printStackTrace();
			}
		}

    public void close(){
      try{
				this.socket.close();
				connections.remove(this);
			}
      catch(Exception e){
				// System.err.println(e.getMessage());
        // e.printStackTrace();
			}
    }
  }

  public static void main(String[] args){
    FileServer fs = new FileServer();
    fs.startDownload();
  }

}
