package pod;

public class Servidor {
	
	static String mensagem = "send 1 to 2"; //Ler mensagem vinda do cliente
	static String[] variavel = mensagem.split(" ");
	
	public static void main(String[] args){
		interpretar(mensagem);
	}
	
	private static void interpretar(String comando){
		switch(variavel[0]){
		case "send":
			send();
			break;
		case "list":
			list();
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
	
	private static void list(){
		
	}

}
