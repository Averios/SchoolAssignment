/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ShareClient;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author revant
 */
public class CommandIn implements Runnable{
    private Socket Cmd;
    private BufferedReader Command;
    private PrintWriter Response;
    private final ServerSocket Server;
    private String rootDir;
    private final int dataPort;
    private final ServerSocket Conn;
    public CommandIn(ServerSocket Connection, int DataOutPort) throws IOException{
        Conn = Connection;
        Server = new ServerSocket(DataOutPort);
        rootDir = "/";
        dataPort = DataOutPort;
    }
    @Override
    public void run() {
        try{
            this.Cmd = Conn.accept();
            System.out.println("Command connected");
            Command = new BufferedReader(new InputStreamReader(Cmd.getInputStream()));
            Response = new PrintWriter(Cmd.getOutputStream(), true);
            String message;
            int CmdSize;
            String[] Request;
            while(!Conn.isClosed()){
                try {
                    message = Command.readLine();
                    if(message == null) break;
                    Request = message.split(" ");
                    Request[0] = Request[0].toLowerCase();
                    CmdSize = Request.length;
                    switch(Request[0]){
                        case "retr" : {
                            sendData(Request[1]);
                            break;
                        }
                        case "stor" : {
                            String files = Request[1];
                            for(int i = 2; i < CmdSize; i++){
                                files = files.concat(" " + Request[i]);
                            }
                            try{
                                System.out.println("Download " + files);
                                saveData(files);
                            } catch(IOException ex){
                                Response.printf("500 %s\r\n", ex.getLocalizedMessage());
                                System.out.println(ex.getLocalizedMessage());
                            }
                            break;
                        }
                        case "dir"  : {
                            break;
                        }
                        case "mkd"  : {
                            CreateFolder(Request[1]);
                            break;
                        }
                        case "rm"   : {
                            DeleteFile(Request[1]);
                            break;
                        }
                        case "rmd"  : {
                            DeleteFolder(Request[1]);
                            break;
                        }
                        default: {
                            Response.printf("503 Command not found\r\n");
                            break;
                        }
                    }
                } catch (IOException ex) {
                    System.out.println("Cannot read input stream");
                    Response.printf("504 Read command error\r\n");
                    break;
                }
            }
            try{
                Server.close();
            } catch(IOException e){
                System.out.println(e.getLocalizedMessage());
            }
        } catch(IOException ex){
            System.out.println(ex.getLocalizedMessage());
        }
    }
    
    public void setRootDir(String dir){
        rootDir = dir;
    }
    
    private void saveData(String path) throws IOException{
        File download = new File(rootDir+ "/" + path);
        download.createNewFile();
        download.setWritable(true);
        download.setReadable(true);
        Response.printf("200 PORT " + Integer.toString(dataPort) + "\r\n");
        System.out.println("Opening data port");
        try {
            Socket newConn = Server.accept();
            System.out.println("New connection accepted");
            Response.printf("200 Please send the data\r\n");
            //get the outputstream for file
            GetData stream = new GetData(newConn.getInputStream(), new FileOutputStream(download), true);
            new Thread(stream).start();
        } catch (IOException ex) {
            Response.printf("500 " + ex.getLocalizedMessage() + "\r\n");
        }   
    }
    
    private void sendData(String path){
        try {
            InputStream data = new FileInputStream(rootDir + path);
            Response.printf("200 PORT " + Integer.toString(dataPort) + "\r\n");
            try{
                Socket newConn = Server.accept();
                Response.printf("200 Sending data\r\n");
                GetData stream = new GetData(data, newConn.getOutputStream());
                new Thread(stream).start();
            } catch (IOException ex){
                Response.printf("500 " + ex.getLocalizedMessage() + "\r\n");
            }
        } catch (FileNotFoundException ex) {
            Response.printf("404 File not found\r\n");
        }
    }
    
    private void CreateFolder(String path){
        boolean success = (new File(rootDir + path).mkdirs());
        if(success){
            Response.printf("200 Directory has been created\r\n");
        }
        else{
            Response.printf("500 Directory creation failed\r\n");
        }
    }
    
    private void DeleteFile(String path){
        File deleted = new File(rootDir + path);
        if(deleted.isFile()){
            if(deleted.delete()){
                Response.printf("200 File deleted successfully\r\n");
            }
            else{
                Response.printf("403 Cannot delete file\r\n");
            }
        }
        else{
            Response.printf("404 File not found\r\n");
        }
    }
    
    private void DeleteFolder(String path){
        File deleted = new File(rootDir + path);
        if(deleted.isDirectory()){
            if(deleted.delete()){
                Response.printf("200 File deleted successfully\r\n");
            }
            else{
                Response.printf("403 Cannot delete directory\r\n");
            }
        }
        else{
            Response.printf("404 File not found\r\n");
        }
    }
}
