package pod;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class Servidor extends Thread{
	
	private static String mensagem, split[];
	private static Map<String, DataOutputStream> usuarios;
	private static ServerSocket serverSocket;
	private Socket socket;
	
	public static void main(String[] args) throws IOException{
		serverSocket = new ServerSocket(5121);
		usuarios = new HashMap<String, DataOutputStream>();
		while(true){//espera conexão de alguem
			Socket socket = serverSocket.accept();
			System.out.println("Conexão estabelecida!");
			
			Thread thread = new Servidor(socket);
			thread.start();
		}
	}
	
	public Servidor(Socket socket) {
		this.socket = socket;
	}
	
	@Override
	public void run() {
		try {
			DataInputStream dataInput = new DataInputStream(socket.getInputStream());
			DataOutputStream dataOutput = new DataOutputStream(socket.getOutputStream());
			
			dataOutput.writeUTF("Informe seu nome: ");
			String nome = dataInput.readUTF();
			
			usuarios.put(nome, dataOutput);
			while(true){
				if(socket.isClosed())
					break;
				//System.out.println("Servidor aguardando mensagem...");
				String mensagem = dataInput.readUTF();
				interpretar(mensagem, nome);
			}
			System.out.println("Conexão encerrada.");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	private void interpretar(String mensagem, String remetente)throws IOException{
		split = mensagem.split(" ");
		
		switch(split[0]){
		case "send":
			if(split[1].equals("-all")){
				send(mensagem, remetente, true);
			}
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
	
	private void sair() throws IOException{
		socket.close();
	}
	
	private void send(String mensagem, String remetente, boolean todos) throws IOException{
		for(String key : usuarios.keySet()){
			if(usuarios.get(key) != usuarios.get(remetente))
				usuarios.get(key).writeUTF(mensagem + "  pra tudiiiin");			
		}
	}
	
	private void send() throws IOException{
		for(String key : usuarios.keySet()){
			if(usuarios.get(key) != this.socket.getOutputStream())
				usuarios.get(key).writeUTF(mensagem + "  pra tudiiiin");			
		}
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
