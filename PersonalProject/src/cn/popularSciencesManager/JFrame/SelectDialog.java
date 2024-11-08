package cn.popularSciencesManager.JFrame;

import cn.popularSciencesManager.model.*;
import cn.popularSciencesManager.utils.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SelectDialog extends JDialog {
    JDialogPromptWindow jdpw=new JDialogPromptWindow();
    BatchGeneration batchGeneration=new BatchGeneration();
    AllModel module;
    TableModelControl table;
    Map<String, String[]> databaseStruct;
    String sql;


    SelectDialog(AllModel module, TableModelControl table, Map<String, String[]> databaseStruct, String sql) {
        setTitle("查找信息");
        setLayout(new FlowLayout());
        this.module=module;
        this.table=table;
        this.databaseStruct=databaseStruct;
        this.sql=sql;
    }



    public String init(){
        JPanel panel=new JPanel(new BorderLayout());
        this.add(panel);
        //顶部区域编写
        JPanel[] topPanel=batchGeneration.createJPanel(3);
        topPanel[0].setLayout(new GridLayout(1,2));
        topPanel[1].setLayout(new FlowLayout(FlowLayout.LEFT));
        JLabel topLabel=new JLabel("选择要查找的内容");
        topPanel[1].add(topLabel);
        topPanel[0].add(topPanel[1]);
        topPanel[2].setLayout(new FlowLayout(FlowLayout.RIGHT));
        JButton topButton=new JButton("增加列");
        topPanel[2].add(topButton);
        topPanel[0].add(topPanel[2]);





        panel.add(topPanel[0],BorderLayout.PAGE_START);



        //设置中心主显示页面
        //设置垂直布局的盒子
        Box centerBox=Box.createVerticalBox();

        String tableName=module.getDatabaseName();
        String[] colNames=null;
        for(Map.Entry<String,String[]> x:databaseStruct.entrySet()){//将数据库信息导出
            if(x.getKey().equals(tableName)){
                colNames=x.getValue();
                break;
            }
        }

        //提示面板创建
        JPanel cueColumn =new JPanel(new GridLayout(1,4));
        JLabel[] cueColumnLabel=batchGeneration.createJLabel(new String[]{"选择字段","选择操作符","输入值","关系"});

        for(JLabel label:cueColumnLabel){
            cueColumn.add(label);
        }
        centerBox.add(cueColumn);


        List<SelectComboBox> selectBox=new ArrayList<>();
        selectBox.add(new SelectComboBox(colNames,module.getDatabaseName()));
        centerBox.add(selectBox.get(0).getSelectPanel());



        //底部区域编写
        JPanel bottomArea=new JPanel(new FlowLayout());
        JButton[] southButton=batchGeneration.createJButton(new String[]{"确认","取消"});
        bottomArea.add(southButton[0]);
        bottomArea.add(southButton[1]);


        String[] finalColNames = colNames;

        //新增列
        topButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(selectBox.get(selectBox.size()-1).selectField.getText().isEmpty()){

                    return;
                }
                selectBox.get(selectBox.size()-1).getRelationControl().setEnabled(true);//在前面一列输入数值后，开放编辑
                selectBox.add(new SelectComboBox(finalColNames,module.getDatabaseName()));
                centerBox.add(selectBox.get(selectBox.size()-1).getSelectPanel());
                SelectDialog.this.validate();
                SelectDialog.this.repaint();
                SelectDialog.this.pack();
            }
        });

        //确认
        southButton[0].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
//                StringBuilder sql=new StringBuilder(SelectPanelExample.this.sql+" ");
                if(selectBox.get(0).selectField.getText().isEmpty()){
                    SelectDialog.this.dispose();
                }
                StringBuilder sql=new StringBuilder(SelectDialog.this.sql+" AND((");
                String flag="(";
                String relation;
                for(SelectComboBox box:selectBox){
                    relation=box.getRelation();
                    if(relation.equals(" OR ")&&flag.equals("(")){
                        sql.append(box.getSQL()).append(")").append(relation);
                        flag=")";
                    }
                    else if(relation.equals(" AND ")&&flag.equals(")")){
                        sql.append("(").append(box.getSQL()).append(relation);
                        flag="(";
                    }
                    else {
                        sql.append(box.getSQL()).append(relation);
                    }
                }
                sql.delete(sql.length()-5,sql.length()-1);
                if(flag.equals("("))
                    sql.append(")");
                sql.append(")");
                System.out.println(sql);
                SelectDialog.this.sql=sql.toString();
                SelectDialog.this.dispose();
            }
        });

        //取消
        southButton[1].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SelectDialog.this.dispose();
            }
        });


        panel.add(centerBox,BorderLayout.CENTER);
        panel.add(bottomArea,BorderLayout.SOUTH);
        pack();
        setModal(true);
        setLocationRelativeTo(null);
        setVisible(true);
        return sql.toString();
    }



    class SelectComboBox{
        JPanel selectPanel;
        String databaseName;//数据库名，锁定数据库
        String[] colNames;//列名数组，控制列名
        String[] searchControl={" = "," > "," >= "," < "," <= "," != "," like "," NOT LIKE "};//搜索控制
        String[] relation={" AND "," OR "};
        JComboBox<String> colNameComboBox;//选择列
        JComboBox<String> colSearchControl=new JComboBox<>(searchControl);//选择列的匹配模式
        JComboBox<String> relationControl=new JComboBox<>(relation);
        JTextField selectField;

        public SelectComboBox(String[] colNames,String databaseName) {
            this.colNames = colNames;
            this.databaseName=databaseName;
            colNameComboBox=new JComboBox<>(colNames);
            selectPanel=new JPanel(new GridLayout(1,4,10,0));
            selectField=new JTextField(10);
            selectPanel.add(colNameComboBox);//添加列选择框
            selectPanel.add(colSearchControl);//匹配符号
            selectPanel.add(selectField);//添加输入文本框
            selectPanel.add(relationControl);//添加与前面的关系框
            relationControl.setEnabled(false);//默认不可编辑
            selectField.addKeyListener(new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent e) {
                    if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                        // 按下回车键时，使文本框失去焦点并取消显示编辑框
                        selectField.setFocusable(false);
                        selectField.setFocusable(true); // 重新设置为可获取焦点，以便下次可以重新编辑
                    }
                }
            });
        }

        public JPanel getSelectPanel() {
            return selectPanel;
        }

        public JComboBox<String> getColNameComboBox() {
            return colNameComboBox;
        }

        public JTextField getSelectField() {
            return selectField;
        }

        public JComboBox<String> getRelationControl() {
            return relationControl;
        }

        public String getSelectCol(){
            return (String) colNameComboBox.getSelectedItem();
        }

        public String getSelectControl(){
            return (String) colSearchControl.getSelectedItem();
        }

        public String getTypeIn(){
            return selectField.getText();
        }

        public String getRelation(){
            return (String) relationControl.getSelectedItem();
        }

        public String getSQL(){
            if(selectField.getText().isEmpty()){
                return "";
            }
            return databaseName+"."+colNameComboBox.getSelectedItem()+colSearchControl.getSelectedItem()+"\""+selectField.getText()+"\" ";
        }
    }




    public static void main(String[] args) {

/*        String URL = "127.0.0.1:3306";
        String DataBaseName = "sciences";
        String url = "jdbc:mysql://" + URL + "/" + DataBaseName + "?serverTimezone=GMT%2B8&useSSL=true&characterEncoding=utf8";
        String username = "root";
        String password = "12345600O";
        try (Connection conn = DriverManager.getConnection(url, username, password)) {
            DatabaseMetaData metaData = conn.getMetaData();
            ResultSet tables = metaData.getTables(DataBaseName, null, "%", new String[]{"TABLE"});
            while (tables.next()) {
                String tableName = tables.getString("TABLE_NAME");
                System.out.println("Table name: " + tableName);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }*/
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("TextField Example");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            // 创建一个文本框
            JTextField textField = new JTextField(20);

            // 添加键盘事件监听器
            textField.addKeyListener(new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent e) {
                    if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                        // 按下回车键时，使文本框失去焦点并取消显示编辑框
                        textField.setFocusable(false);
                        textField.setFocusable(true); // 重新设置为可获取焦点，以便下次可以重新编辑
                    }
                }
            });

            frame.getContentPane().add(textField);
            frame.pack();
            frame.setVisible(true);
        });
    }





}

