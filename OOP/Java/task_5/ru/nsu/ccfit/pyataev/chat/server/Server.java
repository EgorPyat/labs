package ru.nsu.ccfit.pyataev.chat.server;

import java.net.*;
import java.io.*;
import java.util.*;

public class Server{
	private List<Connection> connections = Collections.synchronizedList(new ArrayList<Connection>());
	private ServerSocket server;

	public Server(){
		try{
			this.server = new ServerSocket(3000);

			while(true){
				Socket socket = server.accept();

				Connection con = new Connection(socket);
				this.connections.add(con);

				con.start();
			}
		}
    catch(IOException e){
			e.printStackTrace();
		}
    finally{
			this.closeAll();
		}
	}

	private void closeAll(){
		try{
			this.server.close();

			synchronized(connections){
				Iterator<Connection> iter = connections.iterator();
				while(iter.hasNext()){
					((Connection)iter.next()).close();
				}
			}
		}
    catch (Exception e){
			System.err.println(e.getMessage());
		}
	}

	private class Connection extends Thread{
		private BufferedReader in;
		private PrintWriter out;
		private Socket socket;

		private String name = "";

		public Connection(Socket socket){
			this.socket = socket;

			try{
				this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				this.out = new PrintWriter(socket.getOutputStream(), true);
			}
      catch (IOException e){
				e.printStackTrace();
				close();
			}
		}

		@Override
		public void run(){
			try{
				this.name = this.in.readLine();
				synchronized(connections){
					Iterator<Connection> iter = Server.this.connections.iterator();

          while(iter.hasNext()){
						((Connection)iter.next()).out.println(name + " cames now.");
					}
				}

				String str = "";
				while(true){
					str = this.in.readLine();
					if(str.equals("exit")) break;

					synchronized(connections){
						Iterator<Connection> iter = Server.this.connections.iterator();

						while(iter.hasNext()){
							((Connection)iter.next()).out.println(this.name + ": " + str);
						}
					}
				}

				synchronized(connections){
					Iterator<Connection> iter = Server.this.connections.iterator();
					while(iter.hasNext()){
						((Connection)iter.next()).out.println(name + " has left.");
					}
				}
			}
      catch(IOException e){
				e.printStackTrace();
			}
      finally{
				this.close();
			}
		}

		public void close(){
			try{
				this.in.close();
				this.out.close();
				this.socket.close();

				Server.this.connections.remove(this);

        if(connections.size() == 0){
					Server.this.closeAll();
					System.exit(0);
				}
			}
      catch(Exception e){
				System.err.println(e.getMessage());
			}
		}
	}

  public static void main(String[] args){
    Server server = new Server();
  }
}
