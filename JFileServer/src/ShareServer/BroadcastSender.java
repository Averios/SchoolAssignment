/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ShareServer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Revant
 */
public class BroadcastSender implements Runnable{
    InputStream in;
    List<ClientThread> outs;
    File temp;
    FileOutputStream tempOut;
    FileInputStream tempIn;
    public BroadcastSender(InputStream in, List<ClientThread> outs, File temp) throws IOException{
        this.in = in;
        this.outs = outs;
        this.temp = temp;
        tempOut = new FileOutputStream(temp);
        tempIn = new FileInputStream(temp);
    }
    @Override
    public void run() {
        Thread down = new Thread(new GetData(in, tempOut, true));
        down.start();
        try {
            down.join();
        } catch (InterruptedException ex) {
            System.out.println("Failed to fetch data");
        }
        List<Thread> transferList = new ArrayList<>();
        try{
            for(ClientThread client : outs){
                Thread t = new Thread(new BroadcastData(temp, client));
                transferList.add(t);
                t.start();
            }
        } catch(FileNotFoundException ex){
            System.out.println("Someone deleted the file");
        }
        for(Thread t : transferList){
            try {
                t.join();
            } catch (InterruptedException ex) {
                System.out.println("The thread has been interrupted");
            }
        }
        temp.delete();
    }
    
}
