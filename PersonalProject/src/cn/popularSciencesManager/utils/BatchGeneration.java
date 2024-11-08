package cn.popularSciencesManager.utils;

import javax.swing.*;
import java.awt.*;

public class BatchGeneration {

    public JPanel[] createJPanel(int sum){
        JPanel[] jPanel=new JPanel[sum];
        for(int i=0;i<sum;i++){
            jPanel[i]=new JPanel();
        }
        return jPanel;
    }

    public JTabbedPane createJTabbedPane(String[] title,JPanel[] jPanels){
        JTabbedPane jTabbedPane=new JTabbedPane();
        for(int i=0;i< title.length;i++){
            jTabbedPane.addTab(title[i],jPanels[i]);
        }
        return jTabbedPane;
    }

    public JMenuItem[] createJMenuItems(String[] title){
        JMenuItem[] menuItem=new JMenuItem[title.length];
        for(int i=0;i<title.length;i++){
            menuItem[i]=new JMenuItem(title[i]);
        }
        return menuItem;
    }

    public JButton[] createJButton(String[] title){
        JButton[] buttons=new JButton[title.length];
        for(int i=0;i<title.length;i++){
            buttons[i]=new JButton(title[i]);
        }
        return buttons;
    }

    public JLabel[] createJLabel(String[] title){
        JLabel[] labels=new JLabel[title.length];

        for(int i=0;i<title.length;i++){
            labels[i]=new JLabel(title[i]);
        }
        return labels;
    }

    public JTextField[] createJTextField(int num,int colnum){
        JTextField[] textFields=new JTextField[num];

        for(int i=0;i<num;i++){
            textFields[i]=new JTextField(colnum);
        }

        return textFields;
    }


/*    public void jTabbedPaneAddPanel(JTabbedPane jTabbedPane,int index,Component comp, Object constraints){
        ((JPanel)jTabbedPane.getComponentAt(index)).add(comp,constraints);
    }*/



}
