package ru.nsu.ccfit.pyataev.chat.client;

import java.net.*;
import java.io.*;
import java.util.*;

public class Client{
	private BufferedReader in;
	private PrintWriter out;
	private Socket socket;

	public Client(){
		Scanner scan = new Scanner(System.in);

		try{
			this.socket = new Socket("localhost", 3000);
			this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			this.out = new PrintWriter(socket.getOutputStream(), true);

			System.out.println("Enter your name:");
			this.out.println(scan.nextLine());

			Resender resend = new Resender();
			resend.start();

			String str = "";

      while(!str.equals("exit")){
				str = scan.nextLine();
				this.out.println(str);
			}

			resend.setStop();
		}
    catch(Exception e){
			e.printStackTrace();
		}
    finally{
			this.close();
		}
	}

	private void close(){
		try{
			this.in.close();
			this.out.close();
			this.socket.close();
		}
    catch(Exception e){
			System.err.println(e.getMessage());
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
					String str = Client.this.in.readLine();
					System.out.println(str);
				}
			}
      catch(IOException e){
				System.err.println(e.getMessage());
				e.printStackTrace();
			}
		}
	}

  public static void main(String[] args){
    Client client = new Client();
  }
}
