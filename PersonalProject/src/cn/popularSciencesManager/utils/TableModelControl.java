package cn.popularSciencesManager.utils;

import cn.popularSciencesManager.model.TableModel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.ArrayList;

/***
 * 该类仅仅实现对导入的result进行加工处理，不涉及筛选等操作
 */
public class TableModelControl extends TableModel {
    private final Connection con;
    private Object[] columnNames; // 创建列名
    private Object[][] rows; // 创建行数据组
    private Object[][] allRows;
    private int currentPage = 1; // 设置页数
    private int pageSize = 10; // 设置每页显示的页数

    //面板控制板块
    private JScrollPane scrollPanepanel;//放置到tablePanels[0]当中，用于显示table面板
    private JPanel[] tablePanels=new JPanel[4];
    //第一个表格面板采用边界布局，中心装表格，南方装在控制元件
    //第二个表格面板采用2*1的网格布局，上面放置信息框下面放置提示框
    //第三个装按钮组和输入框
    //第四个装信息框
    private JButton[] tableButton=new JButton[4];
    //三个按钮，第一个是上一页控制按钮
    //第二个是跳转按钮
    //第三个是设置每页有多少行
    //第四个是下一页按钮
    private JTextField tablePageSetField;
    //用于输入跳转的页面数量
    private JTextField tablePageSizeField;
    private JLabel[] tableInfo=new JLabel[2];
    //第一个提示框是页数信息
    //第二个提示框是当前状态


    public JScrollPane getScrollPanepanel() {
        return scrollPanepanel;
    }

    public JPanel[] getTablePanels() {
        return tablePanels;
    }

    public JButton[] getTableButton() {
        return tableButton;
    }

    public JTextField getTablePageSetField() {
        return tablePageSetField;
    }

    public JLabel[] getTableInfo() {
        return tableInfo;
    }

    public TableModelControl(isCellEditable cellEditable, Connection con) {
        super(cellEditable);
        this.con = con;
        setTableModel(rows, columnNames);
        //初始化表格面板


        scrollPanepanel = new JScrollPane(getTable());
        tablePanels[0]=new JPanel(new BorderLayout());
        tablePanels[0].add(scrollPanepanel,BorderLayout.CENTER);

        tablePanels[1]=new JPanel(new GridLayout(2,1));
        tablePanels[0].add(tablePanels[1],BorderLayout.SOUTH);

        tablePanels[2]=new JPanel(new FlowLayout());
        tablePanels[2].add(tableButton[0]=new JButton("上一页"));
        tablePanels[2].add(tablePageSetField=new JTextField(3));
        tablePanels[2].add(tableButton[1]=new JButton("跳转到页"));
        tablePanels[2].add(tableInfo[0]=new JLabel());
        tablePanels[2].add(tablePageSizeField=new JTextField(3));
        tablePanels[2].add(tableButton[2]=new JButton("设置行数"));
        tablePanels[2].add(tableButton[3]=new JButton("下一页"));
        tablePanels[1].add(tablePanels[2]);


        tablePanels[3]=new JPanel();
        tablePanels[3].add(tableInfo[1]=new JLabel());
        tablePanels[1].add(tablePanels[3]);


        //上一页
        tableButton[0].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(allRows==null){
                    tableInfo[1].setText("表格为空");
                    return;
                }
                setCurrentPage(getCurrentPage()-1);
            }
        });

        //跳转
        tableButton[1].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try{
                    if(allRows==null){
                        tableInfo[1].setText("表格为空");
                        tablePageSetField.setText(null);
                        return;
                    }
                    String text=tablePageSetField.getText();
                    if(text==null){
                        tableInfo[1].setText("请输入整数");
                        tablePageSetField.setText(null);
                        return;
                    }
                    int current=Integer.parseInt(text);
                    if(current<0){
                        tableInfo[1].setText("请输入大于0的整数");
                        tablePageSetField.setText(null);
                        return;
                    }
                    setCurrentPage(current);
                    tablePageSetField.setText(null);
                    tableInfo[1].setText(null);
                }catch (NumberFormatException ex){
                    tableInfo[1].setText("请输入整数");
                    tablePageSetField.setText(null);
                }

            }
        });
        //设置页数
        tableButton[2].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try{
                    if(allRows==null){
                        tableInfo[1].setText("表格为空");
                        tablePageSizeField.setText(null);
                        return;
                    }
                    String text=tablePageSizeField.getText();
                    if(text==null){
                        tableInfo[1].setText("请输入整数");
                        tablePageSizeField.setText(null);
                        return;
                    }
                    int current=Integer.parseInt(text);
                    if(current<0){
                        tableInfo[1].setText("请输入大于0的整数");
                        tablePageSizeField.setText(null);
                        return;
                    }
                    setPageSize(current);
                    tablePageSizeField.setText(null);
                    tableInfo[1].setText(null);
                }catch (NumberFormatException ex){
                    tableInfo[1].setText("请输入整数");
                    tablePageSizeField.setText(null);
                }
            }
        });

        //下一页
        tableButton[3].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(allRows==null){
                    tableInfo[1].setText("表格为空");
                    return;
                }
                setCurrentPage(getCurrentPage()+1);
            }
        });


    }











    public JPanel getTablePanel() {
        return tablePanels[0];
    }


    //sql语句执行部分


    // 创建输入SQL语句创建表格的函数，并附带分页控制
    public void loadTableData(String sql) {
        try (Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if(rs==null){
                return;
            }
            // 获取列名
            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();
            columnNames = new Object[columnCount];
            for (int i = 1; i <= columnCount; i++) {
                columnNames[i - 1] = metaData.getColumnLabel(i);
            }

            // 获取所有行数据
            ArrayList<Object[]> rowList = new ArrayList<>();
            while (rs.next()) {
                Object[] row = new Object[columnCount];
                for (int i = 1; i <= columnCount; i++) {
                    row[i - 1] = rs.getObject(i);
                }
                rowList.add(row);
            }

            // 转换为二维数组
//            Object [][] allRows = new Object[rowList.size()][];
            allRows = new Object[rowList.size()][];
            rowList.toArray(allRows);

            // 进行分页处理

//            updateTableModelForPage(allRows);
            updateTableModelForPage();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    //从resultSet创建列表
    public void loadTableData(ResultSet rs){
        try{
            if(rs==null){
                return;
            }
            // 获取列名
            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();
            columnNames = new Object[columnCount];
            for (int i = 1; i <= columnCount; i++) {
                columnNames[i - 1] = metaData.getColumnLabel(i);
            }

            // 获取所有行数据
            ArrayList<Object[]> rowList = new ArrayList<>();
            while (rs.next()) {
                Object[] row = new Object[columnCount];
                for (int i = 1; i <= columnCount; i++) {
                    row[i - 1] = rs.getObject(i);
                }
                rowList.add(row);
            }

            // 转换为二维数组
            allRows = new Object[rowList.size()][];
            rowList.toArray(allRows);

            // 进行分页处理

//            updateTableModelForPage(allRows);
            updateTableModelForPage();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //从Object创建表格
    public void loadTableData(Object[][] rows,Object[] columnNames){
        allRows=rows;
        this.columnNames=columnNames;
        updateTableModelForPage();
    }




    // 根据当前页和每页大小更新表格模型
    private void updateTableModelForPage() {
        int start = (currentPage - 1) * pageSize;
        int end;
        if(start >= allRows.length&&start > 0){
            rows = new Object[0][];
            currentPage--;
            start = (currentPage - 1) * pageSize;
        } else if(start<0){
            start=0;
            currentPage++;
            tableInfo[1].setText("页数已达到最小");
        }
        else if(start==0){
            tableInfo[0].setText(String.format("当前为第%d 页，共%d页",0,0));
        }
        end = Math.min(start + pageSize, allRows.length);
        rows = new Object[end - start][];

        System.arraycopy(allRows, start, rows, 0, end - start);

        setTableModel(rows, columnNames);
        this.getTable().repaint();
        tableInfo[0].setText(String.format("当前为第%d 页，共%d页",currentPage,getMaxPage()));
    }

    // 获取当前页
    public int getCurrentPage() {
        tableInfo[1].setText(null);
        return currentPage;
    }

    // 设置当前页
    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
        updateTableModelForPage();
    }

    // 获取每页显示的行数
    public int getPageSize() {
        return pageSize;
    }

    // 设置每页显示的行数
    public void setPageSize(int pageSize) {
        int pageSized=this.pageSize;
        this.pageSize = pageSize;
        setCurrentPage(((currentPage*pageSized) / pageSize + (currentPage*pageSized) % pageSize != 0 ? 1 : 0));
    }

    public int getMaxPage(){
        return (allRows.length / pageSize+(allRows.length % pageSize != 0 ? 1 : 0));
    }

    public void setColumnNames(Object[] columnNames) {
        this.columnNames = columnNames;
    }

    public void setAllRows(Object[][] allRows) {
        this.allRows = allRows;
    }

    public Object[] getColumnNames() {
        return columnNames;
    }

    public Object[][] getRows() {
        return rows;
    }

    @Override
    public JTable getTable() {
        return super.getTable();
    }

    @Override
    public DefaultTableModel getTableModel() {
        return super.getTableModel();
    }

    public Object[][] getAllRows() {
        return allRows;
    }

    public void clear(){
        allRows=null;
        rows=null;
        setTableModel(allRows,columnNames);
    }
}
