/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ShareServer;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author revant
 */
public class ClientThread extends Thread{
    Socket Client;
    Socket ClientData;
    Socket CommandPort;
    String Name;
    PrintWriter sender;
    BufferedReader input;
    Map ClientList;
    PrintWriter CommandSend;
    BufferedReader CommandResponse;
    String RootFolder;
    public ClientThread(Socket client) throws IOException{
        this.Client = client;
        Name = null;
        sender = new PrintWriter(Client.getOutputStream(), true);
        input = new BufferedReader( new InputStreamReader(Client.getInputStream()));
    }
    
    public void setRoot(String root){
        RootFolder = root;
    }
    
    @Override
    public void run(){
        sender.printf("200 Welcome to our server\r\n"
                + "200 Please enjoy your stay\r\n");
        String message;
        boolean quitted = false;
        int cmdSize;
        while(!Client.isClosed()){
            try{
                message = input.readLine();
                if(message == null)break;
//                    System.out.println(message);
                String[] Command = message.split(" ");
                cmdSize = Command.length;
                Command[0] = Command[0].toLowerCase();
                switch(Command[0]){
                    case "user" : {
                        if(cmdSize < 3){
                            SetUserName(Command[1]);
                        }
                        else{
                            sender.printf("401 Command have too many value\r\n");
                        }
                        break;
                    }
                    case "quit" : {
                        if(cmdSize == 1){
                            sender.printf("200 bye\r\n");
                            quitted = true;
                        }
                        else{
                            sender.printf("401 Command have too many value\r\n");
                        }
                        break;
                    }
                    case "list" : {
                        if(cmdSize > 1){
                            sender.printf("401 Command have too many value\r\n");
                        }
                        else{
                            sendUserList();
                        }
                        break;
                    }
                    case "port" : {
                        if(cmdSize > 2){
                            sender.printf("401 Command have too many value\r\n");
                        }
                        else{
                            connectDataPort(Integer.parseInt(Command[1]));
                        }
                        break;
                    }
                    case "command" : {
                        if(cmdSize > 2){
                            sender.printf("401 Command have too many value\r\n");
                        }
                        else{
                            connectCommandPort(Integer.parseInt(Command[1]));
                        }
                        break;
                    }
                    case "stor" : {
                        System.out.println(message);
                        String files = Command[2];
                        for(int i = 3; i < cmdSize; i++){
                            files = files.concat(" " + Command[i]);
                        }
                        if(cmdSize < 2){
                            sender.printf("401 Command have too few value\r\n");
                        }
                        else{
                            System.out.println("Send " + files);
                            SendData(Command[1], files, true);
                        }
                        break;
                    }
                    case "retr" : {
                        if(cmdSize > 3){
                            sender.printf("401 Command have too many value\r\n");
                        }
                        else{
                            SendData(Command[1], Command[2], false);
                        }
                        break;
                    }
                    case "bcast" : {
                        String filename = input.readLine();
                        List<ClientThread> clients = new ArrayList<>();
                        for(int i = 1; i < cmdSize; i++){
                            if(ClientList.containsKey(Command[i])){
                                clients.add( (ClientThread) ClientList.get(Command[i]));
                            }
                        }
                        try{
                            Broadcast(clients, filename);
                        } catch(IOException ex){
                            sender.printf("500 %s\r\n", ex.getLocalizedMessage());
                        }
                        break;
                    }
                    default: {
                        sender.printf("503 Command not found\r\n");
                        break;
                    }
                }
            }catch(IOException e){
                System.out.println("Cannot read input stream");
                sender.printf("504 Read command error\r\n");
                break;
            }
            if(quitted)break;
        }
        if(Name != null){
            try {
                removeClient();
                System.out.println(Name + " Has quitted");
                CommandPort.close();
                Client.close();
                ClientData.close();
            } catch (IOException ex) {
                System.out.println("Cannot close the socket");
            }
        }
    }
    
    private void Broadcast(List targets, String fileName) throws IOException{
        File dir = new File(RootFolder, Name);
        dir.mkdirs();
        File tempFile = new File(dir, fileName);
        tempFile.createNewFile();
        if(ClientData.isClosed()){
            throw new IOException("data port has not been specified/invalid");
        }
        else{
            new Thread(new BroadcastSender(ClientData.getInputStream(), targets, tempFile)).start();
            sender.printf("200 You can now send the data\r\n");
        }
    }
    
    private boolean addMember(){
        if(!ClientList.containsKey(Name)){
            ClientList.put(Name, this);
            return true;
        }
        return false;
    }
    
    private void removeClient(){
        if(ClientList.containsKey(Name)){
            ClientList.remove(Name);
        }
    }
    
    private void SetUserName(String user){
        if(Name == null){
            Name = user;
            if(addMember()){
                sender.printf("200 assigned name is %s\r\n", user);
            }
            else{
                Name = null;
                sender.printf("402 Error the name has already used by another user\r\n");
            }
        }
        else{
            sender.printf("402 cannot change the already assigned name\r\n");
        }
    }
    
    private void sendUserList(){
        if(!ClientData.isClosed()){
            sender.printf("200 Here come the list\r\n");
            Set<String> nameList = ClientList.keySet();
            try (PrintWriter resend = new PrintWriter(ClientData.getOutputStream())) {
                nameList.stream().forEach((name) -> {
                    resend.println(name);
                });
                sender.printf("200 The list has been sent\r\n");
            } catch(IOException ex){
                sender.printf("500 %s\r\n", ex.getLocalizedMessage());
            } finally{
                try{
                    ClientData.close();
                } catch(IOException ex){
                    System.out.println("Cannot close client data");
                }
            }
        }
        else{
            sender.printf("402 data port has not been specified/invalid\r\n");
        }
    }
    
    private void connectDataPort(int port){
        try{
            ClientData = new Socket(Client.getInetAddress(), port);
            sender.printf("200 Connection established\r\n");
        } catch(IOException ex){
            sender.printf("500 %s\r\n", ex.getLocalizedMessage());
        }
    }
    
    private void connectCommandPort(int port){
        try{
//            CommandPort.connect(new InetSocketAddress(Client.getRemoteSocketAddress().toString(), port));
            CommandPort = new Socket(Client.getInetAddress(), port);
            sender.printf("200 The command socket has been connectded\r\n");
            CommandSend = new PrintWriter(CommandPort.getOutputStream(), true);
            CommandResponse = new BufferedReader(new InputStreamReader(CommandPort.getInputStream()));
        }catch(IOException ex){
            sender.printf("500 %s\r\n", ex.getLocalizedMessage());
        }
    }
    
    private void SendData(String target, String destination, boolean sending){
        if(ClientList.containsKey(target)){
            if(ClientData.isClosed()){
                sender.printf("402 data port has not been specified/invalid\r\n");
                System.out.println(Name + "try sending to unknown user");
            }
            else{
                ClientThread dest = (ClientThread) ClientList.get(target);
                try{
                    Socket destData = dest.IOhandling(destination, sending);
                    if(sending){
                        new Thread(new GetData(ClientData.getInputStream(), destData.getOutputStream(), true)).start();
                        sender.printf("200 You can now send the data\r\n");
                        System.out.println(Name + " to " + target);
                    }
                    else{
                        new Thread(new GetData(destData.getInputStream(), ClientData.getOutputStream(), true)).start();
                        sender.printf("200 Data is being transferred\r\n");
                    }
                } catch(IOException ex){
                    sender.printf("%s", ex.getLocalizedMessage());
                }
            }
        }
        else{
            sender.printf("404 No user with name %s\r\n", target);
        }
    }
    
    
    public Socket getCommandPort(){
        return CommandPort;
    }
    
    
    public void addMap(Map theMap){
        ClientList = theMap;
    }
    /**
     *
     * @return
     */
    public String getClientName(){
        return Name;
    }
    
    public synchronized Socket IOhandling(String destination, boolean isSend) throws IOException{
        if(isSend){
            CommandSend.printf("STOR " + destination + "\r\n");
            
        }
        else{
            CommandSend.printf("RETR " + destination + "\r\n");
        }
        String message = CommandResponse.readLine();
        String[] Status = message.split(" ");
        int code = Integer.parseInt(Status[0]);
        if(code != 200){
            throw new SocketException(message);
        }
        code = Integer.parseInt(Status[2]);
        System.out.println("Connecting to " + Integer.toString(code));
        Socket newConn = new Socket(Client.getInetAddress(), code);

        message = CommandResponse.readLine();
        Status = message.split(" ");
        code = Integer.parseInt(Status[0]);
        if(code != 200){
            throw new SocketException(message);
        }
        return newConn;
    }
}
