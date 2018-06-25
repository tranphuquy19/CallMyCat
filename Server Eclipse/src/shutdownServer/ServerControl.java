package shutdownServer;

import java.awt.AWTException;
import java.awt.SystemTray;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JFrame;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class ServerControl {
	private static ServerSocket myServer;
	private static Socket clientSocket;
	private static InputStreamReader inputStreamReader;
	private static BufferedReader bufferedReader;

	private String message = "";

	private int serverPort = 16057;
	private int clientPort = 16058;
	private String name = "";
	private String ip = "localhost";
	private String clientIP = "localhost";
	private NotifTray td = new NotifTray();
	private panel p;

	public ServerControl() {

		InetAddress IP;
		try {
			IP = InetAddress.getLocalHost();
			ip = IP.getHostAddress();
			System.out.println(ip);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		JFrame jf = new JFrame();
		p = new panel(ip, serverPort);
		jf.add(p);
		jf.setBounds(300, 300, 452, 232);
		jf.setVisible(true);
		openServer(serverPort);

		while (true) {
			getNameCute();
			listening();
		}
	}

	private void getNameCute() {
		File f = new File("C:/Users/"+System.getProperty("user.name").toString()+"/Documents/Call My Cat/name.txt");
		if (f.exists()) {
			try {
				BufferedReader br = new BufferedReader(new FileReader("C:/Users/"+System.getProperty("user.name").toString()+"/Documents/Call My Cat/name.txt"));
				StringBuilder sb = new StringBuilder();
				String line = br.readLine();
				while (line != null) {
					sb.append(line);
					sb.append(System.lineSeparator());
					line = br.readLine();
				}
				this.name = sb.toString();
				System.out.println("Name is" + this.name);
				br.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		} else {
			this.name = p.getName();
			System.out.println("Name is" + this.name);
		}
	}

	private void openServer(int portServer) {
		try {
			myServer = new ServerSocket(portServer);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void listening() {
		try {
			System.out.println("Server running");
			clientSocket = myServer.accept();
			inputStreamReader = new InputStreamReader(clientSocket.getInputStream(), "utf8");
			bufferedReader = new BufferedReader(inputStreamReader);
			
			/*
			 * Cần xử lí Null chỗ này phía Client [chưa xử lí] 24/6/18
			 */
			
			this.message = bufferedReader.readLine();
			JSONParser parser = new JSONParser();
			JSONObject jsonObject = (JSONObject) parser.parse(message);

			/*
			 * Đoạn cần xử lý, chuyển sang JSON || 23/6/18
			 */

			// if (o instanceof Command) {
			// Command cmd = (Command) o;
			// System.out.print("Receive mess>>_" + cmd.getMoTa());
			// System.out.println();
			// commmandComputer(cmd);
			// }
			/*
			 * ********************************** Đã fix ngày 24/6/18
			 */
			
			
			String Mota = jsonObject.get("Mo Ta").toString();
			clientIP = jsonObject.get("IP").toString();
			System.out.println("IP Client is>>"+clientIP);
			new ToClient(clientIP, clientPort, "Server>>_OK "+clientIP);
			System.out.println("Recevice message>>_" + Mota);
			System.out.println();
			commmandComputer(jsonObject);
			

		} catch (Exception e) {
			try {
				td.displayTray("Error! Server is running, don't run again!", this.name);
			} catch (MalformedURLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (AWTException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			System.exit(0);
		}
	}

	private void commmandComputer(JSONObject cmd) {
		if (SystemTray.isSupported()) {
			try {
				td.displayTray(cmd.get("Mo Ta").toString(), this.name);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if ("Custom Command".equals(cmd.get("Mo Ta"))) {
			CustomsCommand customsCommand = new CustomsCommand(cmd.get("Command").toString(), clientIP, clientPort);
			customsCommand.start();
			//new ToClient(clientIP, clientPort, customsCommand.getResult());
		}
		cmd(cmd.get("Command").toString());
	}

	private void cmd(String cmd) {
		try {
			@SuppressWarnings("unused")
			Process p = Runtime.getRuntime().exec(cmd);
		} catch (IOException e) {
			e.printStackTrace();
			new ToClient(clientIP, clientPort, "Error! Cannot Execute...");
		}
	}

	public static void main(String[] args) {
		new ServerControl();

	}

}
