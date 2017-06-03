package ru.nsu.ccfit.pyataev.chat.server;

import ru.nsu.ccfit.pyataev.chat.message.Message;

import java.net.*;
import java.io.*;
import java.util.*;

public class Server{
	private List<Connection> connections = Collections.synchronizedList(new ArrayList<Connection>());
	private ServerSocket server;

	public Server(){
		try{
			this.server = new ServerSocket(3001);

			while(true){
				try{
					Socket socket = server.accept();
					Connection con = new Connection(socket);
					this.connections.add(con);
					con.start();
				}
				catch(SocketException | EOFException e){
					System.out.print(".");
				}
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
    catch(IOException e){
			System.err.println(e.getMessage());
      e.printStackTrace();
		}
	}

	private class Connection extends Thread{
		private ObjectInputStream in;
		private ObjectOutputStream out;
		private Socket socket;

		private String name = "";

		public Connection(Socket socket){
			this.socket = socket;

			try{
        this.out = new ObjectOutputStream(socket.getOutputStream());
				this.in = new ObjectInputStream(socket.getInputStream());
			}
      catch(IOException e){
				e.printStackTrace();
				this.close();
			}
		}

		@Override
		public void run(){
			try{
				this.name = ((Message)this.in.readObject()).toString();

				synchronized(connections){
					Iterator<Connection> iter = Server.this.connections.iterator();
          while(iter.hasNext()){
            Connection c = (Connection)iter.next();
						c.out.writeObject(new Message(name + " entered."));
            c.out.flush();
					}
				}

				String str = "";
				while(true){
					str = ((Message)this.in.readObject()).toString();
					if(str.equals("exit")) break;

					synchronized(connections){
						Iterator<Connection> iter = Server.this.connections.iterator();
						while(iter.hasNext()){
              Connection c = (Connection)iter.next();
    					c.out.writeObject(new Message(this.name + ": " + str));
              c.out.flush();
						}
					}
				}

				synchronized(connections){
					Iterator<Connection> iter = Server.this.connections.iterator();
					while(iter.hasNext()){
            Connection c = (Connection)iter.next();
            c.out.writeObject(new Message(this.name + " left."));
            c.out.flush();
					}
				}
			}
      catch(ClassNotFoundException | IOException e){
				System.out.println("Client was lost!");
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
        e.printStackTrace();
			}
		}
	}

  public static void main(String[] args){
    try{
      Server server = new Server();
    }
    catch(NullPointerException e){
      System.err.println("Connection error!");
    }
  }
}
