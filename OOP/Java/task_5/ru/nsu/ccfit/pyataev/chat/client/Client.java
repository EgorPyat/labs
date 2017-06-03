package ru.nsu.ccfit.pyataev.chat.client;

import ru.nsu.ccfit.pyataev.chat.message.Message;

import java.net.*;
import java.io.*;
import java.util.*;

public class Client{
	private ObjectInputStream in;
	private ObjectOutputStream out;
	private Socket socket;

	public Client(){
		Scanner scan = new Scanner(System.in);

		try{
			this.socket = new Socket("localhost", 3001);

			this.in = new ObjectInputStream(this.socket.getInputStream());
      this.out = new ObjectOutputStream(this.socket.getOutputStream());

			System.out.println("Enter your name:");
			this.out.writeObject(new Message(scan.nextLine()));
      this.out.flush();

			Resender resend = new Resender();
			resend.start();

			String str = "";

      while(!str.equals("exit")){
				str = scan.nextLine();
				this.out.writeObject(new Message(str));
        this.out.flush();
			}
      synchronized(in){
        resend.setStop();
      }
		}
    catch(IOException e){
      System.err.println("Server not found!");
		}
    finally{
			this.close();
		}
	}

	private void close(){
		try{
			this.socket.close();
			this.in.close();
			this.out.close();
		}
    catch(IOException e){
			System.out.println("Bad closing!");
		}
	}

	private class Resender extends Thread{
		private boolean stopped;

		public void setStop(){
			this.stopped = true;
		}

		@Override
		public void run(){
			try{
				while(!stopped){
          Message str;
          synchronized(in){
						str = (Message)Client.this.in.readObject();
          }
          System.out.println(str);
				}
			}
      catch(ClassNotFoundException | IOException e){
				System.out.println("Connection was lost!");
			}
		}
	}

  public static void main(String[] args){
    try{
      Client client = new Client();
    }
    catch(NullPointerException e){
      System.err.println("Connection error!");
    }
  }
}
