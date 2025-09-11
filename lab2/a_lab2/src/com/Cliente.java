
/**
 * Laboratorio 1 de Sistemas Distribuidos
 * 
 * Autor: Murilo Fontana Muniz, Vinicius Lopes Silvino
 */

package com;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Scanner;

public class Cliente {
    
    private static Socket socket;
    private static DataInputStream entrada;
    private static DataOutputStream saida;
    
    private int porta=1025;
    
    public void iniciar(){
    	System.out.println("Cliente iniciado na porta: "+porta);
    	while (true) {
                try {
                
                socket = new Socket("127.0.0.1", porta);
                
                entrada = new DataInputStream(socket.getInputStream());
                saida = new DataOutputStream(socket.getOutputStream());
                
                //Recebe do usuario algum valor
                BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
                System.out.println("Options| \"read\" a fortune\t\"write\" a fortune\t\"exit\": \n\tType your option: ");
                String valor = br.readLine();

                if (valor.equals("read")) {
                    saida.writeUTF(valor);
                } else if (valor.equals("write")) {
                    Scanner scanner = new Scanner(System.in);
                    System.out.println("Enter a new fortune (finish input with an empty line):");
                    StringBuilder newFortune = new StringBuilder();
                    String line;
                    while (true) {
                        line = scanner.nextLine();
                        if (line.isEmpty()) break;
                        newFortune.append(line).append("\n");
                    }
                    saida.writeUTF(newFortune.toString());
                } else if (valor.equals("exit")) {
                    System.out.println("Exiting...");
                    socket.close();
                    break;
                } else {
                    System.out.println("Invalid option. Please type \"read\", \"write\" or \"exit\".");
                    continue;
                }
                
                //Recebe-se o resultado do servidor
                String resultado = entrada.readUTF();
                
                //Mostra o resultado na tela
                System.out.println(resultado);

                //socket.close();

            } catch(Exception e) {
                e.printStackTrace();
            }
        }
    	
    }
    
    public static void main(String[] args) {
        new Cliente().iniciar();
    }
    
}
