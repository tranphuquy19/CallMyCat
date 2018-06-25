package client;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

import shutdownServer.Command;

public class ClientControl {
	private String serverHost = "192.168.1.104";
	private int serverPort = 16057;

	public void action(Command user) {
		try {
			Socket mySocket = new Socket(serverHost, serverPort);
			System.out.println("Client>> Đã tạo kết nối đến server");
			ObjectOutputStream oos = new ObjectOutputStream(mySocket.getOutputStream());
			oos.writeObject(user);
			System.out.println("Da gui user");
			ObjectInputStream ois = new ObjectInputStream(mySocket.getInputStream());
			Object o = ois.readObject();
			if (o instanceof String) {
				String rely = (String) o;
				System.out.println("Server>> " + rely);
			}
			mySocket.close();
			System.out.println("Đã gửi xong gói");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		System.out.println("Nhập cmd: ");
		String id = sc.nextLine();
		System.out.println("Nhập mt: ");
		String passwd = sc.nextLine();
		Command user = new Command();
		ClientControl client = new ClientControl();
		user.setCmd(id);
		user.setMoTa(passwd);
		client.action(user);
		sc.close();

	}

}
