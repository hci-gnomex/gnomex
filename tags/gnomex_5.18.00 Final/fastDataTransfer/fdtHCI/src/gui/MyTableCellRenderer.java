/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gui;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.table.TableCellRenderer;

/**
 * Table cell renderer used for formatting the row size and row coloring.
 *
 * @author Shobhit
 */
public class MyTableCellRenderer  extends JTextArea implements TableCellRenderer {
    public MyTableCellRenderer() {
          setLineWrap(true);
          setWrapStyleWord(true);
       }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
    {

        setSize(table.getColumnModel().getColumn(column).getWidth(),getPreferredSize().height);
        if (table.getRowHeight(row) != getPreferredSize().height)
        {
            if(getPreferredSize().height ==0)
                table.setRowHeight(row, 32);
            else
                table.setRowHeight(row, getPreferredSize().height);
                
        }
    
        setText(value.toString());
        
        Color color;
        
        if(row % 2 != 0)
            color = new Color(191, 239, 255);
        else
            color = Color.WHITE;
        this.setBackground(color);
        return this;
    }
}
