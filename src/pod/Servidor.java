package pod;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Servidor extends Thread{
	
	private static String mensagem, split[];
	private static Map<String, Usuario> usuarios;
	private static ServerSocket serverSocket;
	private Socket socket;
	
	public static void main(String[] args) throws IOException{
		
		serverSocket = new ServerSocket(5121);
		usuarios = new HashMap<String, Usuario>();
		System.out.println("Servidor rodando!");
		
		while(true){
			Socket socket = serverSocket.accept();
			
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
			
			DateFormat df = new SimpleDateFormat(" HH:mm dd/MM/yyyy");
			String strData = df.format(new Date(System.currentTimeMillis()));
			
			String nome = "";
			
			while(true){
				
				dataOutput.writeUTF("Informe seu nome: > ");
				nome = dataInput.readUTF().toUpperCase();
				
				if (nome.equals("")){
					dataOutput.writeUTF("\nO nome informado é inválido.\n");
					continue;
				}
				
				if (usuarios.get(nome) != null){
					dataOutput.writeUTF("\nO nome informado já existe!\n");
					continue;
				}
				dataOutput.writeUTF("\nVocê foi cadastrado com sucesso!\n");
				break;
			}
			
			Usuario user = new Usuario(socket,dataOutput,dataInput);
			usuarios.put(nome, user);
			informaEntrada(nome, strData);
			
			while(true){
				if(socket.isClosed())
					break;
				String mensagem = dataInput.readUTF();
				nome = interpretar(mensagem, nome);
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	private String interpretar(String mensagem, String remetente)throws IOException{
		
		DateFormat df = new SimpleDateFormat(" HH:mm dd/MM/yyyy");
		String strData = df.format(new Date(System.currentTimeMillis()));
		
		try{
			split = mensagem.split(" ");
			
			switch(split[0]){
			
				case "send":
					if(split.length < 2) throw new Exception("\nComando Inválido!");
					String modo = split[1];
					if(modo.equals("-all")){
						if(split.length < 3) throw new Exception("\nComando Inválido!");
						mensagem = mensagem.substring(10, mensagem.length());
						send(mensagem, remetente, strData);
					}else if(modo.equals("-user")){
						if(split.length < 4) throw new Exception("\nComando Inválido!");
						String usuario = split[2];
						mensagem = mensagem.substring(12+usuario.length(), mensagem.length());
						sendTo(mensagem, usuario.toUpperCase(), remetente, strData);
					}else throw new Exception("\nCommando Inválido!");
					break;
				case "list":
					list(remetente);
					break;
				case "rename":
					if(split.length < 2) throw new Exception("\nComando Inválido!");
					String novoNome = split[1];
					rename(remetente, novoNome.toUpperCase(), strData);
					remetente = novoNome.toUpperCase();
					break;
				case "bye":
					sair(remetente, strData);
					break;
				default:
					throw new Exception("\nComando Inválido!");
			}
		}catch(Exception ex){
			usuarios.get(remetente).getDataOutput().writeUTF(ex.getMessage() + strData);		
		}
		
		return remetente;
	}
	
	private void sair(String usuario, String strData) throws IOException{
		usuarios.get(usuario).getDataOutput().writeUTF("bye");
		informaSaida(usuario, strData);
		this.socket.close();
		usuarios.remove(usuario);
	}
	
	private void send(String mensagem, String remetente, String strData) throws IOException{
		for(String key : usuarios.keySet()){
			if(usuarios.get(key) != usuarios.get(remetente))
				usuarios.get(key).getDataOutput().writeUTF(getChatName(remetente) + ": " + mensagem+strData);			
		}
	}
	
	private void informaEntrada(String remetente, String strData) throws IOException{
		for(String key : usuarios.keySet()){
			if(usuarios.get(key) != usuarios.get(remetente))
				usuarios.get(key).getDataOutput().writeUTF("\n"+getChatName(remetente) + " entrou no chat."+strData);			
		}
	}
	
	private void informaSaida(String remetente, String strData) throws IOException{
		for(String key : usuarios.keySet()){
			if(usuarios.get(key) != usuarios.get(remetente))
				usuarios.get(key).getDataOutput().writeUTF("\n"+getChatName(remetente) + " saiu do chat."+strData);			
		}
	}
	
	private void sendTo(String mensagem, String destinatario, String remetente, String strData) throws Exception{
		if(usuarios.get(destinatario) == null) throw new Exception("\nUsuário inexistente!");
			
		if(!remetente.equals(destinatario))
			usuarios.get(destinatario).getDataOutput().writeUTF("\n"+getChatName(remetente) + " (direct): " + mensagem+strData);
		else throw new Exception("\nUsuário inexistente!");
	}
	
	private static void rename(String user, String novoNome, String strData) throws Exception{
		if(usuarios.get(novoNome) != null) throw new Exception("\nUsuário já existente!");
		
		Usuario usuario = usuarios.get(user);
		usuarios.remove(user);
		usuarios.put(novoNome, usuario);
		
		usuarios.get(novoNome).getDataOutput().writeUTF("\nNome alterado com sucesso!"+strData);
	}
	
	private static void list(String remetente) throws IOException{
		StringBuilder str = new StringBuilder();
		str.append("Usuários online:\n");
		for(String key : usuarios.keySet()){
			str.append(key + "\n");
		}
		usuarios.get(remetente).getDataOutput().writeUTF(str.toString());
	}
	
	private static String getChatName(String user){
		Usuario usuario = usuarios.get(user);
		StringBuilder str = new StringBuilder();
		str.append(usuario.getSocket().getRemoteSocketAddress());
		str.append("/~");
		str.append(user);
		
		return str.toString().substring(1, str.toString().length());
	}

}
