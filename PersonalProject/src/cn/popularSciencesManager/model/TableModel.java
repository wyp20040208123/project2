package cn.popularSciencesManager.model;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

/***
 * 该类包装了表格模型、表格和一个装表格的滚动面板
 */
public class TableModel extends DefaultTableModel {
    private JTable table;

    public enum isCellEditable {
        CellEditable,
        UNCellEditable;
    }

    public TableModel() {
    }

    protected TableModel(isCellEditable cellEditable) {
        switch (cellEditable) {
            case CellEditable:
                this.table = new JTable(this) {
                    @Override
                    public boolean isCellEditable(int row, int column) {
                        return true;
                    }
                };
                break;
            case UNCellEditable:
                this.table = new JTable(this) {
                    @Override
                    public boolean isCellEditable(int row, int column) {
                        return false;
                    }
                };
                break;
        }

    }

    protected DefaultTableModel getTableModel() {
        return this;
    }

    protected void setTableModel(Object[][] data, Object[] columnNames) {
        this.setDataVector(data, columnNames);
    }

    protected JTable getTable() {
        return table;
    }

    protected void setTable(JTable table) {
        this.table = table;
    }



}
