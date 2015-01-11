/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package swing;

import javax.swing.*; 
import javax.swing.border.Border; 
import javax.swing.border.EmptyBorder; 
import javax.swing.table.TableCellRenderer; 
import javax.swing.text.View; 
import java.awt.*; 
 
 
public class MultilineTableCellRenderer extends JTextPane implements TableCellRenderer 
{ 
    protected static Border noFocusBorder = new EmptyBorder(1, 1, 1, 1); 
 
    public MultilineTableCellRenderer() 
    { 
        //setWrapStyleWord(true); 
        //setLineWrap(true);
        super.setContentType("text/html");
    }
  
    @Override
    public Component getTableCellRendererComponent(JTable table, Object arg, boolean isSelected, boolean hasFocus, int row, int column) 
    { 
        Object[] tab = (Object[])arg;
        String value = tab[0].toString();
        Color user_co = (Color)tab[1];
        if (isSelected) 
        { 
            super.setForeground(table.getSelectionForeground());
            if (user_co != null) 
                { 
                    super.setOpaque(true);
                    super.setBackground(user_co); 
                    
                }
            else
                super.setBackground(table.getSelectionBackground());
        } 
        else 
        { 
            super.setForeground(table.getForeground());
            if (user_co != null) 
                { 
                    super.setOpaque(true);
                    super.setBackground(user_co); 
                    
                }
            else{
                super.setBackground(table.getBackground());
            } 
        } 
 
        setFont(table.getFont()); 
 
        if (hasFocus) 
        { 
            setBorder(UIManager.getBorder("Table.focusCellHighlightBorder")); 
            if (!isSelected && table.isCellEditable(row, column)) 
            { 
                Color col; 
                col = UIManager.getColor("Table.focusCellForeground"); 
                if (col != null) 
                { 
                    super.setForeground(col); 
                } 
                col = UIManager.getColor("Table.focusCellBackground"); 
                
                if(col != null){
                    //super.setOpaque(false);
                    super.setBackground(col);
                } 
            } 
        } 
        else 
        { 
            setBorder(noFocusBorder); 
        } 
 
        setEnabled(table.isEnabled()); 
        setValue(table, row, column, value); 
 
        return this; 
    }
 
 
    protected void setValue(JTable table, int row, int column, Object value) 
    { 
        if (value != null) 
        { 
            setText(value.toString()); 
 
            View view = getUI().getRootView(this); 
            view.setSize((float) table.getColumnModel().getColumn(column).getWidth() - 3, 0); 
            float y = view.getPreferredSpan(View.Y_AXIS); 
            int h = (int) Math.ceil(y + 3); 
            if(h < 30) h = 30;
 
            if (table.getRowHeight(row) != h) 
            { 
                table.setRowHeight(row, h); 
            } 
        } 
        else 
        { 
            setText(""); 
        } 
    } 
}