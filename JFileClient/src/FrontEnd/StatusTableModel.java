/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package FrontEnd;

import ShareClient.StatusData;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JProgressBar;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author revant
 */
public class StatusTableModel extends AbstractTableModel{
    public String[] m_colNames = {"File", "Progress", "Status"};
    public Class[] m_colTypes = {String.class, JProgressBar.class, String.class};
    List m_macDataVector;
    
    public StatusTableModel(){
        super();
        m_macDataVector = new ArrayList();
    }
    public StatusTableModel(List Data){
        super();
        m_macDataVector = Data;
    }
    
    
    @Override
    public int getRowCount() {
        return m_macDataVector.size();
    }

    @Override
    public int getColumnCount() {
        return m_colNames.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        StatusData data = (StatusData)m_macDataVector.get(rowIndex);
        switch(columnIndex){
            case 0 :
                return data.getFileName();
            case 1 :
                return data.getProgress();
            case 2 :
                return data.getStatus();
        }
        return null;
    }
    
    public void addData(StatusData Data){
        m_macDataVector.add(Data);
        fireTableRowsInserted(m_macDataVector.size() -1, m_macDataVector.size() -1);
    }
    
    @Override
    public String getColumnName(int col) {
      return m_colNames[col];
    }

    @Override
    public Class getColumnClass(int col) {
      return m_colTypes[col];
    }
    
}
