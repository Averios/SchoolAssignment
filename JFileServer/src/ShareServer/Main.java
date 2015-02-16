/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ShareServer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author revant
 */
public class Main {
    
    public static void main(String[] args) {
        try {
            ServerSocket server = new ServerSocket(2121);
            Map<String, ClientThread> clientList = new ConcurrentHashMap<>();
            while (true) {                
                Socket client = server.accept();
                ClientThread newClient = new ClientThread(client);
                newClient.addMap(clientList);
                newClient.setRoot("/home/revant/Documents/Progjar/");
                newClient.start();
                System.out.println("New connection from " + client.getInetAddress());
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
