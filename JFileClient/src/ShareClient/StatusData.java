/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ShareClient;

import java.io.File;
import javax.swing.JProgressBar;

/**
 *
 * @author revant
 */
public class StatusData {
    String FileName;
    JProgressBar Progress;
    String status;
    public StatusData(File file, String status){
        this.FileName = file.getName();
        this.status = status;
        Progress = new JProgressBar();
    }
    public StatusData(String file, String status){
        this.FileName = file;
        this.status = status;
        Progress = new JProgressBar();
    }
    
    public String getStatus(){
        return this.status;
    }
    public void setStatus(String status){
        this.status = status;
    }
    public JProgressBar getProgress(){
        return Progress;
    }
    public void setProgress(int value){
        this.Progress.setValue(value);
    }
    public String getFileName(){
        return this.FileName;
    }
    public void setFileName(String name){
        this.FileName = name;
    }
}
