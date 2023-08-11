package com.sam.todolist2.controller;

import com.sam.todolist2.model.dao.TodoDao;
import com.sam.todolist2.model.entity.Todo;
import com.sam.todolist2.service.TodoService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Sql(scripts = "classpath:test/data.sql") // sql 檔案放置的地方
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class TestTodoController {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    TodoService todoService;
    @Autowired
    private TodoDao todoDao; // 請替換為您的 Repository 類

    @Test
    public void testGetTodos() throws Exception {
        String strDate = "2023-08-10 19:00:00";
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = format.parse(strDate);

        // [Arrange] 預期回傳的值
        List<Todo> expectedList = new ArrayList();
        Todo todo = new Todo();
        todo.setId(1);
        todo.setTask("打掃");
        todo.setCreateTime(date);
        todo.setUpdateTime(date);
        expectedList.add(todo);

        // [Act] 模擬網路呼叫[GET] /api/todos
        String returnString = mockMvc.perform(MockMvcRequestBuilders.get("/api/todos")
                        .accept(MediaType.APPLICATION_JSON ))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);

        Iterable<Todo> actualList = objectMapper.readValue(returnString, new TypeReference<Iterable<Todo>>() {
        });
        // [Assert] 判定回傳的body是否跟預期的一樣
        assertEquals(expectedList,  actualList);
    }

//    @Test
//    public void testCreateTodos() throws Exception {
//        // [安排] 預期的回傳值
//        JSONObject todoObject = new JSONObject();
//        todoObject.put("task", "寫文章");
//
//        // [Act] 模擬網路呼叫[POST] /api/todos
//        String actualId = mockMvc.perform(MockMvcRequestBuilders.post("/api/todos")
//                        .accept(MediaType.APPLICATION_JSON) // 設定回應的型別
//                        .contentType(MediaType.APPLICATION_JSON) // 設定請求的型別
//                        .content(String.valueOf(todoObject))) // 設定內容
//                .andExpect(status().isCreated()) // 預期回應的狀態碼為 201(Created)
//                .andReturn().getResponse().getContentAsString();
//
//        // [斷言] 判斷回傳的內容是否與預期相同
//        assertEquals("2", actualId); // 注意這裡將 actualId 視為字串進行比較
//    }


    @Test
    public void testUpdateTodoButIdNotExist() throws Exception {
        JSONObject todoObject = new JSONObject();
        todoObject.put("status", 2);

        // [Act] 模擬網路呼叫[PUT] /api/todos/{id}
        mockMvc.perform(MockMvcRequestBuilders.put("/api/todos/100")
                        .contentType(MediaType.APPLICATION_JSON) // request 設定型別
                        .content(String.valueOf(todoObject))) // body 內容
                .andExpect(status().isBadRequest()); // [Assert] 預期回應的status code 為 400(Bad Request)
    }


    @Test
    public void testDeleteTodoSuccess() throws Exception {
        // [Act] 模擬網路呼叫[DELETE] /api/todos/{id}
        Iterable<Todo> todos = todoDao.findAll(); // 假設您的 Repository 有 findAll 方法
        for (Todo todo : todos) {
            System.out.println("ID: " + todo.getId() + ", Task: " + todo.getTask());
        }
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/todos/1")
                        .contentType(MediaType.APPLICATION_JSON)) // request 設定型別
                .andExpect(status().isNoContent()); // [Assert] 預期回應的status code 為 204(No Content)
    }

    @Test
    public void testDeleteTodoButIdNotExist() throws Exception {
        // [Act] 模擬網路呼叫[DELETE] /api/todos/{id}
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/todos/100")
                        .contentType(MediaType.APPLICATION_JSON)) // request 設定型別
                .andExpect(status().isBadRequest()); // [Assert] 預期回應的status code 為 400(Bad Request)
    }
}
