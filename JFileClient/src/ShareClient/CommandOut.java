/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ShareClient;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author revant
 */
public class CommandOut{
    private final Socket Cmd;
    BufferedReader Response;
    PrintWriter Command;
    ServerSocket DataSocket;
    public CommandOut(Socket Outbound, int DataPort) throws IOException{
        this.Cmd = Outbound;
        Response = new BufferedReader(new InputStreamReader(Cmd.getInputStream()));
        Command = new PrintWriter(Cmd.getOutputStream(), true);
        DataSocket = new ServerSocket(DataPort);
    }
    
    public CommandOut(Socket Outbound, ServerSocket DataOut) throws IOException{
        Cmd = Outbound;
        Response = new BufferedReader(new InputStreamReader(Cmd.getInputStream()));
        Command = new PrintWriter(Cmd.getOutputStream(), true);
        DataSocket = DataOut;
    }
    
    public void setUsername(String user) throws IOException, IllegalArgumentException{
        String message = Response.readLine();
        message = Response.readLine();
        Command.printf("USER %s\r\n", user);
        message = Response.readLine();
        System.out.println(message);
        if(message == null){
            throw new IOException("Cannot read the response");
        }
        String[] Status = message.split(" ");
        int stat = Integer.parseInt(Status[0]);
        if(stat != 200){
            throw new IllegalArgumentException("The username has been used");
        }
    }
    
    private Socket OpenDataPort() throws IOException{
        Command.printf("PORT %d\r\n", DataSocket.getLocalPort());
        Socket client = DataSocket.accept();
        String message = Response.readLine();
        return client;
    }
    
    public void SendData(String receiver, File data) throws IOException{
        Socket dataPort = OpenDataPort();
        Command.printf("STOR %s %s\r\n", receiver, data.getName());
        String message = Response.readLine();
        String[] Stat = message.split(" ");
        if(Integer.parseInt(Stat[0]) != 200){
            throw new IOException(message);
        }
        GetData stream = new GetData(new FileInputStream(data), dataPort.getOutputStream(), true);
        new Thread(stream).start();
    }
    
    public void SetCommandInPort(int port) throws IOException{    
        Command.printf("COMMAND %d\r\n", port);
        String message = Response.readLine();
        String[] Stat = message.split(" ");
        if(Integer.parseInt(Stat[0]) != 200){
            throw new IOException(message);
        }
    }
    
    public Object[] getUserList() throws IOException{
        Socket dataPort = OpenDataPort();
        Command.printf("LIST\r\n");
        List<String> userList = new ArrayList<>();
        String message;
        String[] usr;
        BufferedReader userRead = new BufferedReader(new InputStreamReader(dataPort.getInputStream()));
        while(true){
            message = userRead.readLine();
            if(message == null) break;
            userList.add(message);
        }
        return userList.toArray();
    }
    
    public void Broadcast(File data, List Receiver) throws IOException{
        Socket dataPort = OpenDataPort();
        String sends = "BCAST";
        for(Object now : Receiver){
            sends = sends.concat(" " + now);
        }
        Command.printf("%s \r\n", sends);
        Command.printf("%s\r\n", data.getName());
        String message = Response.readLine();
        String[] code = message.split(" ");
        if(Integer.parseInt(code[0]) != 200){
            throw new IOException(message);
        }
        new Thread(new GetData(new FileInputStream(data), dataPort.getOutputStream(), true)).start();
    }
}
