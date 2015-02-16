/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ShareServer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 *
 * @author Revant
 */
public class BroadcastData implements Runnable{
    InputStream in;
    OutputStream out;
    String Filename;
    ClientThread client;
    public BroadcastData(InputStream in, String Filename, ClientThread client){
        this.in = in;
        this.Filename = Filename;
        this.client = client;
    }
    public BroadcastData(File data, ClientThread client) throws FileNotFoundException{
        this.in = new FileInputStream(data);
        this.Filename = data.getName();
        this.client = client;
    }

    @Override
    public void run() {
        try {
            Socket dataPort = client.IOhandling(Filename, true);
            out = dataPort.getOutputStream();
            byte[] data = new byte[512];
            int dataLen;
            while( (dataLen = in.read(data)) > -1){
                out.write(data, 0, dataLen);
            }
        } catch (IOException ex) {
            System.out.println("Redirect to " + client.getClientName() + " has failed");
        } finally{
            try {
                out.close();
            } catch (IOException ex) {
                System.out.println("Failed to close connection to " + client.getClientName());
            }
        }
        
    }
    
}
