package com.example.demo;
import java.util.List;

public class DatabaseTable {
    String tableName;
    List<String> columnName;

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public List<String> getColumnName() {
        return columnName;
    }

    public void setColumnName(List<String> columnName) {
        this.columnName = columnName;
    }

    public DatabaseTable() {
    }

    public DatabaseTable(String tableName, List<String> columnName) {
        this.tableName = tableName;
        this.columnName = columnName;
    }
}
