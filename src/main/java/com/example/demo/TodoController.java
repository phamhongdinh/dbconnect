package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


import org.springframework.transaction.annotation.Transactional;

@Controller
public class TodoController {

    @Autowired
    private DataSource dataSource;

    @GetMapping("/")
    @Transactional
    public String index(Model model) {
        try (Connection conn = dataSource.getConnection()) {
            DatabaseMetaData metaData = conn.getMetaData();
            String databaseName = conn.getCatalog();
            ResultSet tables = metaData.getTables(null, null, null, new String[]{"TABLE"});
            List<DatabaseTable> listDatabaseTable = new ArrayList<>();
            while (tables.next()) {
                DatabaseTable tableDb = new DatabaseTable();
                String tableName = "";
                List<String> columnNames = new ArrayList<>();
                tableName = tables.getString("TABLE_NAME");
                tableDb.setTableName(tableName);
                ResultSet columns = metaData.getColumns(null, null, tableName, "%");
                while (columns.next()) {
                    String columnName = columns.getString("COLUMN_NAME");
                    columnNames.add(columnName);
                }
                if (!tableName.equals("trace_xe_event_map") && !tableName.equals("trace_xe_action_map")) {
                    tableDb.setColumnName(columnNames);
                    listDatabaseTable.add(tableDb);
                }
            }
            model.addAttribute("tables", listDatabaseTable);
            model.addAttribute("databaseName", databaseName);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "index";
    }
}

