import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class GerenciadorDeClientes extends Thread {

	private Socket cliente;
	private String nomeCliente;
	private BufferedReader leitor;
	private PrintWriter escritor;
	private static final Map<String, GerenciadorDeClientes> clientes = new HashMap<>();

	/*
	 * construtor porque é um gerenciador de cliente para cada cliente
	 */
	public GerenciadorDeClientes(Socket cliente) {
		this.cliente = cliente;
		start();
	}

	/**
	 * 
	 */
	@Override
	public void run() {
		try {
			// getInputStream recebe mensagen
			leitor = new BufferedReader(new InputStreamReader(cliente.getInputStream()));
			// esta mandando getOutputStream
			escritor = new PrintWriter(cliente.getOutputStream(), true);
			escritor.println("Favor inserir seu nome: ");
			String msg = leitor.readLine();

			this.nomeCliente = msg.toLowerCase().replaceAll(",", "");
			escritor.println("Olá " + this.nomeCliente);
			clientes.put(this.nomeCliente, this);
			
			escritor.println("###########"+this.nomeCliente+"###########");

			while (true) {
				msg = leitor.readLine();
				if (msg.equalsIgnoreCase("SAIR")) {
					this.cliente.close();
				} else if (msg.toLowerCase().startsWith("msg")) {
					String nomeDestinatario = (String) msg.substring(2, msg.length());
					System.out.println("Enviando para: " + nomeDestinatario);
					GerenciadorDeClientes destinatario = clientes.get(msg.subSequence(3, msg.length()));
					if (destinatario == null) {
						escritor.println("Cliente informado nao existe");
					} else {
						escritor.println("Digite uma mensagem para " + destinatario.getNomeCliente());
						destinatario.getEscritor().println(this.nomeCliente + " disse a mensagem " + leitor.readLine());
					}
				}
				else if (msg.equalsIgnoreCase("listar")) {
					StringBuffer str = new StringBuffer();
					for (String c : clientes.keySet()) {
						str.append(c);
						str.append(",");
					}
					str.delete(str.length() - 1, str.length() - 1);
					escritor.println(str.toString());
				} else {
					escritor.println("Foi recebido por " + this.nomeCliente + " a seguinte mensagem: " + msg);
				}
			}
		} catch (IOException e) {
			System.out.println("O cliente " + this.nomeCliente + " fechou a conexão");

		}
	}

	public PrintWriter getEscritor() {
		return escritor;
	}

	public BufferedReader getLeitor() {
		return leitor;
	}

	public String getNomeCliente() {
		return nomeCliente;
	}
}
