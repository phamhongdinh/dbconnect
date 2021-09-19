package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;

@RestController
@RequestMapping("/")
public class TodoController {

    private final TodoRepository todoRepository;
    @Autowired
    private DataSource dataSource;



    public TodoController(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    @PostMapping("/")
    @ResponseStatus(HttpStatus.CREATED)
    public Todo createTodo(@RequestBody Todo todo) {
        return todoRepository.save(todo);
    }

    @GetMapping("/")
    @Transactional
    public Iterable<Object> getTodos() {
        List<Object> list=new ArrayList<>();
        try (Connection conn = dataSource.getConnection()) {
            conn.getMetaData().getClientInfoProperties();
            list.add(conn.getMetaData().getDatabaseProductVersion());

            list.add(conn.getMetaData().getDatabaseProductName());
            list.add(conn.getMetaData().getUserName());
            list.add(conn.getMetaData().getSchemas().getFetchSize());

            List<Object> list01 = new ArrayList<>();
            DatabaseMetaData metaData = conn.getMetaData();
            ResultSet tables = metaData.getTables(null, null, null, new String[] { "TABLE" });
            while (tables.next()) {
                String tableName=tables.getString("TABLE_NAME");
                System.out.println(tableName);
                list01.add(tableName);

                ResultSet columns = metaData.getColumns(null,  null,  tableName, "%");
                while (columns.next()) {
                    String columnName=columns.getString("COLUMN_NAME");

                }
            }
            list.add(list01);
            return list;
        } catch (SQLException throwable) {
            throwable.printStackTrace();
            return null;
        }
    }
}
