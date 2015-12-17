package pod;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

public class Usuario {
	
	private Socket socket;
	private DataOutputStream dataOutput;
	private DataInputStream dataInput;
	
	public Usuario(Socket socket, DataOutputStream dataOutput, DataInputStream dataInput) {
		super();
		this.socket = socket;
		this.dataOutput = dataOutput;
		this.dataInput = dataInput;
	}
	public Socket getSocket() {
		return socket;
	}
	public void setSocket(Socket socket) {
		this.socket = socket;
	}
	public DataOutputStream getDataOutput() {
		return dataOutput;
	}
	public void setDataOutput(DataOutputStream dataOutput) {
		this.dataOutput = dataOutput;
	}
	public DataInputStream getDataInput() {
		return dataInput;
	}
	public void setDataInput(DataInputStream dataInput) {
		this.dataInput = dataInput;
	}
	
	
}
