package shutdownServer;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class ToClient {
	static Socket socket;
	static PrintWriter printWriter;
	static String message = "";
	Boolean bool = true;
public ToClient(String host, int port, String message)
{
	try {
		socket = new Socket(host, port);
		printWriter = new PrintWriter(socket.getOutputStream());
		printWriter.write(message);
		printWriter.flush();
		printWriter.close();
		socket.close();
	} catch (UnknownHostException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
}
//	public static void main(String[] args) {
//		Scanner sc = new Scanner(System.in);
//		while(true)
//		{
//			System.out.println("Send to Client>>");
//			message = sc.nextLine();
//			new ToClient(message);
			
			
			
			
			
			
			
			
			
			
//			try {
//				socket = new Socket("192.168.1.103", 16058);
//				printWriter = new PrintWriter(socket.getOutputStream());
//				System.out.println("Send to Client>>");
//				message = sc.nextLine();
//				printWriter.write(message);
//				printWriter.flush();
//				printWriter.close();
//				socket.close();
//			} catch (UnknownHostException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
			
//		}
		
		
//	}
}
