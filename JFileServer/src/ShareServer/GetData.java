/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ShareServer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 *
 * @author revant
 */
public class GetData implements Runnable{
    private final InputStream in;
    private final OutputStream out;
    private int sent;
    private final boolean closeOut;
    public GetData(InputStream in, OutputStream out){
        this.in = in;
        this.out = out;
        closeOut = false;
    }
    public GetData(InputStream in, OutputStream out, boolean CloseOutput){
        this.in = in;
        this.out = out;
        closeOut = CloseOutput;
    }
    @Override
    public void run() {
        int read;
        sent = 0;
        byte[] data = new byte[512];
        try {
            while((read = in.read(data)) > -1){
                sent += read;
                out.write(data, 0, read);
            }
        } catch (IOException ex) {
            System.out.println("Failed to stream data");
        } finally{
            try{
                in.close();
                if(closeOut){
                    out.close();
                }
            } catch(IOException e){
                System.out.println("Failed to close I/O stream");
            }
        }
    }
    
    public int getSent(){
        return sent;
    }
    
}
