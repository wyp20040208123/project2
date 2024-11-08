package cn.popularSciencesManager.utils;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
public class DbUtils {
    static Connection con;

    Map<String, String[]> databaseStruct;
    static String databaseName;

    public DbUtils(Connection con,String databaseName){
        this.con=con;
        this.databaseName=databaseName;
        this.databaseStruct = getDatabaseStructure();
    }

    public static Map<String, String[]> getDatabaseStructure() {
        Map<String, String[]> databaseStruct = new HashMap<>();

        try {
            DatabaseMetaData metaData = con.getMetaData();

            // Get table names
            ResultSet tables = metaData.getTables(databaseName, null, "%", new String[] { "TABLE" });

            while (tables.next()) {
                String tableName = tables.getString("TABLE_NAME");

                // Get columns for each table
                ResultSet columns = metaData.getColumns(databaseName, null, tableName, "%");
                String[] columnNames = extractColumnNames(columns);

                databaseStruct.put(tableName, columnNames);
            }

            tables.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return databaseStruct;
    }

    private static String[] extractColumnNames(ResultSet columns) throws SQLException {
        columns.last(); // 移动到最后一列
        int rowCount = columns.getRow(); // 获取行信息
        columns.beforeFirst(); // 将列重新移动到最前面

        String[] columnNames = new String[rowCount];
        int i = 0;

        while (columns.next()) {
            columnNames[i++] = columns.getString("COLUMN_NAME");
        }

        columns.close();

        return columnNames;
    }


    public Map<String, String[]> getDatabaseStruct() {
        return databaseStruct;
    }

    public static void main(String[] args) {
        Map<String, String[]> databaseStructure = getDatabaseStructure();

        // 使用范例
        for (Map.Entry<String, String[]> entry : databaseStructure.entrySet()) {
            System.out.println("Table: " + entry.getKey());
            System.out.print("Columns: ");
            for (String column : entry.getValue()) {
                System.out.print(column + " ");
            }
            System.out.println();
        }
    }

}
