package pod;
import java.io.DataOutputStream;
import java.net.InetAddress;
import java.net.Socket;

import javax.swing.JOptionPane;

public class Cliente {
	public static void main(String[] args){
		String host = "0.0.0.0";
		int port = 5121;

		try{
			InetAddress address = InetAddress.getByName(host);
			Socket serverSocket = new Socket(address, port);

			DataOutputStream dataOutput = new DataOutputStream(serverSocket.getOutputStream());
			//DataInputStream dataInput = new DataInputStream(serverSocket.getInputStream());
			
			while(serverSocket.isConnected()){
				String string = JOptionPane.showInputDialog("Digite a mensagem: ");
				dataOutput.writeUTF(string);
			}
			serverSocket.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
