import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

// Thread é uma classe que possibilita
// que a classe execute a mesma funcao ao mesmo
// tempo, pois ela pode estar enviando e 
// recebendo mensagens ao mesmo tempo
	
public class ClienteSocket {
	public static void main(String[] args) {
		final int porta = 9999;
		//conectando ao servidor
		try {
			final Socket cliente = new Socket("127.0.0.1",porta);
			
			//lendo mesnsagens do servidor
			new Thread() {
				public void run() {
					try {
						BufferedReader leitor = new BufferedReader(new InputStreamReader(cliente.getInputStream()));
						
						while (true) {
							String mensagem = leitor.readLine();
							System.out.println(mensagem);
						}
						
					} catch (IOException e) {
						System.err.println("Impossivel ler a mensagem do servidor");
						e.printStackTrace();
					}
				}
			}.start();
			
			//escrevendo mensagem para o servidor
			PrintWriter escritor = new PrintWriter(cliente.getOutputStream(),true);
			BufferedReader leitorTerminal = new BufferedReader(new InputStreamReader(System.in));
			String mensagemTerminal = "";
			while((mensagemTerminal = leitorTerminal.readLine()) != null || leitorTerminal.readLine().length()>0) {
				escritor.println(mensagemTerminal);
				if(mensagemTerminal.equalsIgnoreCase("SAIR"))
					System.exit(0);
			}
			
		} catch (UnknownHostException e) {
			System.out.println("Endereço inválido");
			System.exit(0);
		} catch (IOException e) {
			System.out.println("Servidor fora do ar");
			System.exit(0);
			
		}
	}
}
