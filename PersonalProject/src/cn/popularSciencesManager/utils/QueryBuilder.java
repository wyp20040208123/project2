package cn.popularSciencesManager.utils;
import java.util.ArrayList;
import java.util.List;

/***
 * 用于构建排序语句
 */
public class QueryBuilder {
    private List<String> selectColumns;
    private List<String> tables;
    private List<String> joinConditions;
    private List<String> whereConditions;
    private String orderBy;
    private String groupBy;
    private String limit;

    public QueryBuilder() {
        this.selectColumns = new ArrayList<>();
        this.tables = new ArrayList<>();
        this.joinConditions = new ArrayList<>();
        this.whereConditions = new ArrayList<>();
        this.orderBy = "";
        this.groupBy = "";
        this.limit = "";
    }

    public QueryBuilder select(String column) {
        this.selectColumns.add(column);
        return this;
    }

    public QueryBuilder from(String table) {
        this.tables.add(table);
        return this;
    }

    public QueryBuilder join(String joinCondition) {
        this.joinConditions.add(joinCondition);
        return this;
    }

    public QueryBuilder where(String condition) {
        this.whereConditions.add(condition);
        return this;
    }

    public QueryBuilder orderBy(String column) {
        this.orderBy = column;
        return this;
    }

    public QueryBuilder groupBy(String column) {
        this.groupBy = column;
        return this;
    }

    public QueryBuilder limit(int count) {
        this.limit = "LIMIT " + count;
        return this;
    }

    public QueryBuilder orderBy(String column, boolean ascending) {
        String order = ascending ? "ASC" : "DESC";
        if (this.orderBy.isEmpty()) {
            this.orderBy = String.format("ORDER BY %s %s", column, order);
        } else {
            this.orderBy += String.format(", %s %s", column, order);
        }
        return this;
    }

    public String build() {
/*        if (selectColumns.isEmpty()) {
            selectColumns.add("*"); // 默认选择所有列
        }*/

        StringBuilder query = new StringBuilder("SELECT ");
        query.append(String.join(", ", selectColumns));
        query.append(" FROM ");
        query.append(String.join(", ", tables));

        if (!joinConditions.isEmpty()) {
            for (String join : joinConditions) {
                query.append(" ").append(join);
            }
        }

        if (!whereConditions.isEmpty()) {
            query.append(" WHERE ");
            query.append(String.join(" AND ", whereConditions));
        }

        if (!groupBy.isEmpty()) {
            query.append(" GROUP BY ").append(groupBy);
        }

        if (!orderBy.isEmpty()) {
            query.append(" ").append(orderBy);
        }

        if (!limit.isEmpty()) {
            query.append(" ").append(limit);
        }

        System.out.println("Table Query:"+query.toString());
        return query.toString();
    }

    public static void main(String[] args) {
        QueryBuilder queryBuilder = new QueryBuilder();

        String singleTableQuery = queryBuilder
                .select("id")
                .select("name")
                .from("table1")
                .where("id > 10")
                .orderBy("name")
                .limit(5)
                .build();

        System.out.println("Single Table Query: " + singleTableQuery);

        queryBuilder = new QueryBuilder();
        String multiTableQuery = queryBuilder
                .select("table1.id")
                .select("table1.name")
                .select("table2.description")
                .from("table1")
                .join("INNER JOIN table2 ON table1.id = table2.id")
                .where("table1.id > 10")
                .orderBy("table1.name")
                .limit(5)
                .build();

        System.out.println("Multi-Table Query: " + multiTableQuery);



        queryBuilder = new QueryBuilder();
        String multiLevelSortingQuery = queryBuilder
                .select("id")
                .select("name")
                .from("table1")
                .orderBy("name", true)  // 第一级排序：按名字升序
                .orderBy("age", false)  // 第二级排序：按年龄降序
                .build();

        System.out.println("Multi-Level Sorting Query: " + multiLevelSortingQuery);
    }
}
