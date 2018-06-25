package shutdownServer;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Command implements Serializable{
private String cmd;
private String moTa;
public String getCmd() {
	return cmd;
}
public void setCmd(String cmd) {
	this.cmd = cmd;
}
public String getMoTa() {
	return moTa;
}
public void setMoTa(String moTa) {
	this.moTa = moTa;
}

}
