import java.util.*;
import java.net.*;
import java.io.*;
import java.util.concurrent.atomic.AtomicInteger;
import org.json.*;

public class HttpClient{
	private BufferedReader in;
	private PrintWriter out;
	private Socket socket;
	private String address;
	private int port;
	private int token;

	public HttpClient(String address, int port){
		this.address = address;
		this.port = port;
	}

	public void start(){
		try{
			while(true){
				this.socket = new Socket(this.address, this.port);
				this.in  = new BufferedReader(new InputStreamReader(this.socket.getInputStream(), "UTF-8"));
				this.out = new PrintWriter(new OutputStreamWriter(this.socket.getOutputStream(), "UTF-8"), true);
				Scanner sc = new Scanner(System.in);
				System.out.print("Enter your username: ");
				String name = sc.nextLine();

				String response = "{\"username\": \"" + name + "\"}";

				this.out.println("POST /login HTTP/1.1\nHOST: localhost:3001\ncontent-type: application/json\ncontent-length: " + (response.length() + 1) + "\n\n" + response);
				String[] header = this.getHeader();
				if(header[0].split(" ")[1].equals("401")){
					System.out.println("Take another name!");
					this.close();
					continue;
				}
				String content = this.getContent(new Integer((header[header.length - 1].split(":"))[1].trim()));
				this.token = new JSONObject(content).getInt("token");
				// System.out.println(content);
				this.close();
				break;
			}

			System.out.println("Connected!");
			boolean online = true;

			this.socket = new Socket(this.address, this.port);
			this.in  = new BufferedReader(new InputStreamReader(this.socket.getInputStream(), "UTF-8"));
			this.out = new PrintWriter(new OutputStreamWriter(this.socket.getOutputStream(), "UTF-8"), true);

			this.out.println("GET /messages HTTP/1.1\nHOST: localhost:3001\nauthorization: " + this.token + "\n");
			String[] header = this.getHeader();
			if(header[0].split(" ")[1].equals("403")){
				System.out.println("Bad token!");
				this.close();
				online = false;
				return;
			}
			String content = this.getContent(new Integer((header[header.length - 1].split(":"))[1].trim()));
			JSONObject obj = new JSONObject(content);
			JSONArray arr = obj.getJSONArray("messages");
			for(int i = 0; i < arr.length(); i++){
				JSONObject o = arr.getJSONObject(i);
				System.out.println(">>> " + o.getString("message"));
			}
			// System.out.println(content);
			this.close();

			Thread msgGetter = new Thread(new Runnable(){
				@Override
				public void run(){
					long time = System.currentTimeMillis();
					while(true){
						try{
							if(System.currentTimeMillis() - time > 1000){
								socket = new Socket(address, port);
								in = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
								out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"), true);
								out.println("GET /messages HTTP/1.1\nHOST: localhost:3001\nauthorization: " + token + "\n");
								String[] header = getHeader();
								if(header[0].split(" ")[1].equals("403")){
									System.out.println("Bad token!");
									in.close();
									out.close();
									socket.close();
									// continue;
									System.exit(1);
								}
								String content = getContent(new Integer((header[header.length - 1].split(":"))[1].trim()));
								JSONObject obj = new JSONObject(content);
								JSONArray arr = obj.getJSONArray("messages");
								for(int i = 0; i < arr.length(); i++){
									JSONObject o = arr.getJSONObject(i);
									System.out.println(">>> " + o.getString("message"));
								}
								// System.out.println(content);
								in.close();
								out.close();
								socket.close();
								time = System.currentTimeMillis();
							}
							else{
								Thread.sleep(1000);
							}
						}
						catch(Exception e){
							e.printStackTrace();
							System.err.println("In getter " + e.getMessage());
						}
					}
				}
			});
			msgGetter.setDaemon(true);
			msgGetter.start();

			Scanner sc = new Scanner(System.in);
			while(online){
				String command = sc.nextLine();
				this.socket = new Socket(this.address, this.port);
				this.in  = new BufferedReader(new InputStreamReader(this.socket.getInputStream(), "UTF-8"));
				this.out = new PrintWriter(new OutputStreamWriter(this.socket.getOutputStream(), "UTF-8"), true);

				switch(command){
					case "/logout":
						this.out.println("GET /logout HTTP/1.1\nHOST: localhost:3001\nauthorization: " + this.token + "\n");
						header = this.getHeader();
						if(header[0].split(" ")[1].equals("403")){
							System.out.println("Bad token!");
							this.close();
							online = false;
							// break;
							return;
						}
						content = this.getContent(new Integer((header[header.length - 1].split(":"))[1].trim()));
						// System.out.println(content);

						online = false;
						this.close();
						break;
					case "/list":
						this.out.println("GET /users HTTP/1.1\nHOST: localhost:3001\nauthorization: " + this.token + "\n");
						header = this.getHeader();
						if(header[0].split(" ")[1].equals("403")){
							System.out.println("Bad token!");
							this.close();
							online = false;
							// break;
							return;
						}
						content = this.getContent(new Integer((header[header.length - 1].split(":"))[1].trim()));
						JSONObject ob = new JSONObject(content);
						JSONArray ar = ob.getJSONArray("users");
						for(int i = 0; i < ar.length(); i++){
							JSONObject o = ar.getJSONObject(i);
							System.out.println("### " + o.getString("username") + " : " + o.getString("status"));
						}
						// System.out.println(content);
						break;
					default:
						String response = "{\"message\": \"" + command + "\"}";
						this.out.println("POST /messages HTTP/1.1\nHOST: localhost:3001\nauthorization: " + this.token + "\ncontent-type: application/json\ncontent-length: " + (response.length() + 1) + "\n\n" + response);
						header = this.getHeader();
						if(header[0].split(" ")[1].equals("403")){
							System.out.println("Bad token!");
							this.close();
							online = false;
							// break;
							return;
						}
						content = this.getContent(new Integer((header[header.length - 1].split(":"))[1].trim()));
						// System.out.println(content);
						break;
				}
			}
		}
		catch(Exception e){
			e.printStackTrace();
			System.err.println("In start " + e.getMessage());
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
			// for(int i = 0; i < header.length; i++){
			// 	System.out.println(header[i]);
			// }
		}
		catch(Exception e){
			System.err.println(e.getMessage());
		}

		return header;
	}

	private String getContent(int contentLength){
		int sum = 0;
		StringBuilder request = new StringBuilder();
		try{
			while(true){
				char[] buffer = new char[64];
				int r = in.read(buffer, 0, 64);
				// System.out.println(r + " " + contentLength);
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

	public static void main(String[] args) throws Exception {
		HttpClient c = new HttpClient("localhost", 3001);
		c.start();
	}

}
