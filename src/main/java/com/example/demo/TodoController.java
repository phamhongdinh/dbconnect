package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;

@Controller
public class TodoController {

    @Autowired
    private DataSource dataSource;

    @GetMapping("/index")
    public String index(Model model) {

        // add `message` attribute
        model.addAttribute("message", "Thank you for visiting.");

        // return view name
        return "index";
    }

    @GetMapping("/api")
    @Transactional
    public HashMap<String, Object> getTodos(){
        HashMap<String, Object> fullTableObject = new HashMap<>();
        try (Connection conn = dataSource.getConnection()) {
            DatabaseMetaData metaData = conn.getMetaData();
            ResultSet tables = metaData.getTables(null, null, null, new String[] { "TABLE" });
            while (tables.next()) {
                String tableName = "";
                List<String> columnNames = new ArrayList<>();
                tableName = tables.getString("TABLE_NAME");
                ResultSet columns = metaData.getColumns(null,  null,  tableName, "%");
                while (columns.next()) {
                    String columnName =columns.getString("COLUMN_NAME");
                    columnNames.add(columnName);
                }
                if(!tableName.equals("trace_xe_event_map") && !tableName.equals("trace_xe_action_map")){
                    fullTableObject.put(tableName,columnNames);
                }
            }
            return fullTableObject;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
