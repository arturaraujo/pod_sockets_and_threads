package pod;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Servidor extends Thread{
	
	private static String mensagem, split[];
	private static List<Cliente> usuarios = new ArrayList<Cliente>();
	private static ServerSocket serverSocket;
	
	public static void main(String[] args) throws IOException{
		serverSocket = new ServerSocket(5121, 5);
		Socket socket = serverSocket.accept();
		
		DataInputStream dataInput = new DataInputStream(socket.getInputStream());
		mensagem = dataInput.readUTF();
		interpretar(mensagem);
		serverSocket.close();
	}
	
	private static void interpretar(String comando){
		split = mensagem.split(" ");
		
		switch(split[0]){
		case "send":
			send();
			break;
		case "list":
			System.out.println(list());
			break;
		case "rename":
			rename(null);
			break;
		case "bye":
			sair();
			break;
		default:
			System.out.println("O comando informado é invalido.");
		}
	}
	
	private static void sair(){
		
	}
	
	private static void send(){
		
	}
	
	private static void rename(String novoNome){
		if(novoNome == null){
			System.out.println("Novo nome informado inválido.");
		}
	}
	
	private static String list(){
		String retorno = new String();
		retorno = "Listar os clientes da lista e enviar pra quem requisitou.";
		return retorno;
	}

}
