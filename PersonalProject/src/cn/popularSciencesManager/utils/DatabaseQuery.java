package cn.popularSciencesManager.utils;

import java.sql.*;
import java.util.*;

public class DatabaseQuery {

    // 数据库连接信息
    private static final String URL = "jdbc:mysql://localhost:3306/dbname";
    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";

    public static void main(String[] args) {
        // 示例：单表查询
        List<Map<String, Object>> customers = queryTable("customers", "name", "John Doe");
        for (Map<String, Object> customer : customers) {
            System.out.println(customer);
        }

        // 示例：跨表联合查询
        List<Map<String, Object>> result = joinTables("customers", "orders", "customer_id", "name", "John Doe");
        for (Map<String, Object> row : result) {
            System.out.println(row);
        }
    }

    // 单表查询
    public static List<Map<String, Object>> queryTable(String tableName, String columnName, String value) {
        List<Map<String, Object>> results = new ArrayList<>();
        String sql = "SELECT * FROM " + tableName + " WHERE " + columnName + " = ?";

        try (Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, value);
            ResultSet rs = stmt.executeQuery();

            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();

            while (rs.next()) {
                Map<String, Object> row = new HashMap<>();
                for (int i = 1; i <= columnCount; i++) {
                    row.put(metaData.getColumnName(i), rs.getObject(i));
                }
                results.add(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return results;
    }

    // 扩展 queryTable 方法
    public static List<Map<String, Object>> queryTable(String tableName, Map<String, Object> conditions) {
        List<Map<String, Object>> results = new ArrayList<>();
        StringBuilder sqlBuilder = new StringBuilder("SELECT * FROM ").append(tableName).append(" WHERE ");

        int index = 0;
        for (String column : conditions.keySet()) {
            if (index > 0) {
                sqlBuilder.append(" AND ");
            }
            sqlBuilder.append(column).append(" = ?");
            index++;
        }

        String sql = sqlBuilder.toString();

        try (Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            int parameterIndex = 1;
            for (Object value : conditions.values()) {
                stmt.setObject(parameterIndex, value);
                parameterIndex++;
            }

            ResultSet rs = stmt.executeQuery();

            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();

            while (rs.next()) {
                Map<String, Object> row = new HashMap<>();
                for (int i = 1; i <= columnCount; i++) {
                    row.put(metaData.getColumnName(i), rs.getObject(i));
                }
                results.add(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return results;
    }


    // 添加分页支持
    public static List<Map<String, Object>> queryTableWithPagination(String tableName, Map<String, Object> conditions, int limit, int offset) {
        List<Map<String, Object>> results = new ArrayList<>();
        StringBuilder sqlBuilder = new StringBuilder("SELECT * FROM ").append(tableName).append(" WHERE ");

        int index = 0;
        for (String column : conditions.keySet()) {
            if (index > 0) {
                sqlBuilder.append(" AND ");
            }
            sqlBuilder.append(column).append(" = ?");
            index++;
        }

        sqlBuilder.append(" LIMIT ? OFFSET ?");
        String sql = sqlBuilder.toString();

        try (Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            int parameterIndex = 1;
            for (Object value : conditions.values()) {
                stmt.setObject(parameterIndex, value);
                parameterIndex++;
            }
            stmt.setInt(parameterIndex++, limit);
            stmt.setInt(parameterIndex, offset);

            ResultSet rs = stmt.executeQuery();

            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();

            while (rs.next()) {
                Map<String, Object> row = new HashMap<>();
                for (int i = 1; i <= columnCount; i++) {
                    row.put(metaData.getColumnName(i), rs.getObject(i));
                }
                results.add(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return results;
    }

    // 添加排序支持
    public static List<Map<String, Object>> queryTableWithSorting(String tableName, Map<String, Object> conditions, String sortBy, String sortOrder) {
        List<Map<String, Object>> results = new ArrayList<>();
        StringBuilder sqlBuilder = new StringBuilder("SELECT * FROM ").append(tableName).append(" WHERE ");

        int index = 0;
        for (String column : conditions.keySet()) {
            if (index > 0) {
                sqlBuilder.append(" AND ");
            }
            sqlBuilder.append(column).append(" = ?");
            index++;
        }

        sqlBuilder.append(" ORDER BY ").append(sortBy).append(" ").append(sortOrder);
        String sql = sqlBuilder.toString();

        try (Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            int parameterIndex = 1;
            for (Object value : conditions.values()) {
                stmt.setObject(parameterIndex, value);
                parameterIndex++;
            }

            ResultSet rs = stmt.executeQuery();

            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();

            while (rs.next()) {
                Map<String, Object> row = new HashMap<>();
                for (int i = 1; i <= columnCount; i++) {
                    row.put(metaData.getColumnName(i), rs.getObject(i));
                }
                results.add(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return results;
    }


    // 跨表联合查询
    public static List<Map<String, Object>> joinTables(String table1, String table2, String joinColumn, String filterColumn, String value) {
        List<Map<String, Object>> results = new ArrayList<>();
        String sql = "SELECT * FROM " + table1 +
                " INNER JOIN " + table2 + " ON " + table1 + "." + joinColumn + " = " + table2 + "." + joinColumn +
                " WHERE " + table1 + "." + filterColumn + " = ?";

        try (Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, value);
            ResultSet rs = stmt.executeQuery();

            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();

            while (rs.next()) {
                Map<String, Object> row = new HashMap<>();
                for (int i = 1; i <= columnCount; i++) {
                    row.put(metaData.getColumnName(i), rs.getObject(i));
                }
                results.add(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return results;
    }
}

