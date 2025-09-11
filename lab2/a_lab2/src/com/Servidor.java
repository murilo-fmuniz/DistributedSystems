package com;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;

public class Servidor {

	private static Socket socket;
	private static ServerSocket server;

	private static DataInputStream entrada;
	private static DataOutputStream saida;
	private int NUM_FORTUNES = 0;

	private int porta = 1025;

	public void iniciar() {

		FortuneFileReader fr = new FortuneFileReader(false); // only if debug == true shows souts in the file reading process
		HashMap hm = new HashMap<Integer, String>();
		try {
			NUM_FORTUNES = fr.countFortunes();
			fr.parser(hm);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		System.out.println("Servidor iniciado na porta: " + porta);
		while (true) {
			try {
				// Criar porta de recepcao
				server = new ServerSocket(porta);
				socket = server.accept();  //Processo fica bloqueado, ah espera de conexoes

				// Criar os fluxos de entrada e saida
				entrada = new DataInputStream(socket.getInputStream());
				saida = new DataOutputStream(socket.getOutputStream());

				// Recebimento do valor inteiro
				String valor = entrada.readUTF();
				System.out.println(valor);

				// Processamento do valor
				String resultado = "";
				if (valor.equals("read")){
					System.out.println("Read received...\n\tloading fortune from database\n");
					resultado = fr.read(hm);
				}else if(valor.equals("write")) {
					System.out.println("Write received...\n");
					valor = entrada.readUTF();
					System.out.println("Fortune received: " + valor);

					fr.write(hm, new StringBuilder(valor));
					resultado = "Fortune written successfully";
					saida.writeUTF(resultado);
				}else if(valor.equals("exit")) {
					System.out.println("Exit received...\n\tclosing connection\n");
					resultado = "Connection closed";
					saida.writeUTF(resultado);
					socket.close();
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
	}

	public static void main(String[] args) {

		new Servidor().iniciar();

	}

}
