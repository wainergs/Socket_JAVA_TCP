import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;


public class ServidorSocket {
	public static void main(String[] args) {
		 final int porta = 9999;
		//Inicia o servidor
		ServerSocket servidor = null;
		
		try {
			System.out.println("Iniciando o servidor na porta "+porta+" ...");
			
			//Instancia um objeto na classe ServetSocket passando como parametro a porta
			servidor = new ServerSocket(porta);
			System.out.println("Servidor Iniciado...");
			
			//aceitando conexao
			while (true) {
				Socket cliente = servidor.accept();
				new GerenciadorDeClientes(cliente);
			}
			
		} catch (Exception e) {
			//Se cair aqui a porta está ocupada ou fechada
			System.err.println("Porta ocupada ou servidor fechado");
			try {
				//se o servidor nao for nulo fecha a conexão
				if (servidor != null)
					servidor.close();
			} catch (IOException e1) {}
		}
	}

}
