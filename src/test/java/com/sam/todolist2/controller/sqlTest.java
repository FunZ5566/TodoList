package com.sam.todolist2.controller;

import com.sam.todolist2.model.dao.TodoDao;
import com.sam.todolist2.model.entity.Todo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
public class sqlTest {

    @Autowired
    private TodoDao todoDao; // 請替換為您的 Repository 類

    @Test
    @Sql(scripts = "classpath:test/data.sql")
    public void testCreateTodos() throws Exception {
        // 測試程式碼...

        // 檢查資料是否插入數據庫中
        Iterable<Todo> todos = todoDao.findAll(); // 假設您的 Repository 有 findAll 方法
        for (Todo todo : todos) {
            System.out.println("ID: " + todo.getId() + ", Task: " + todo.getTask());
        }

        // 斷言等等...
    }
}
