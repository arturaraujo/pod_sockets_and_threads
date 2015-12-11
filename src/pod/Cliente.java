package pod;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

public class Cliente extends Thread{
	static Socket serverSocket;
	static Scanner keyboard = new Scanner(System.in);
	
	public static void main(String[] args){
		String host = "0.0.0.0";
		int port = 5121;

		try{
			InetAddress address = InetAddress.getByName(host);
			serverSocket = new Socket(address, port);
			
			Thread thread = new Cliente();
			thread.start();

			DataOutputStream dataOutput = new DataOutputStream(serverSocket.getOutputStream());
			
			while(serverSocket.isConnected()){
				String string = keyboard.nextLine();
				dataOutput.writeUTF(string);
			}
			serverSocket.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	@Override
	public void run() {
		DataInputStream dataInput;
		try {
			dataInput = new DataInputStream(serverSocket.getInputStream());
			while(true){
				System.out.println(dataInput.readUTF());
			}
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
	}
}
