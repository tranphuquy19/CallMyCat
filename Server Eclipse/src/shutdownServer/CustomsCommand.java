package shutdownServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class CustomsCommand extends Thread{
	private String result = "";
	String cmd="", host="";
	int port;
	@Override
	public void run() {
		// TODO Auto-generated method stub
		threadCustomsCommand();
	}
	public CustomsCommand(String cmd, String host, int port)
	{
		this.host = host;
		this.port = port;
		this.cmd = cmd;
	}
	private void threadCustomsCommand()
	{
		Process p;
		try {
			p = Runtime.getRuntime().exec(cmd);
			String s;
			System.out.println(p.getOutputStream());
			BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
			while((s = stdInput.readLine()) != null)
			{
				System.out.println(s);
				if("".equals(s)) continue;
				new ToClient(host, port, s);
				try {
					sleep(50);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			new ToClient(host, port, "Error! Cannot Execute...");
		}
		
	}
	public String getResult() {
		return result;
	}

}
