package com.sam.todolist2.controller;

import com.sam.todolist2.model.dao.TodoDao;
import com.sam.todolist2.model.entity.Todo;
import com.sam.todolist2.service.TodoService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.nio.charset.StandardCharsets;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class controllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    TodoService todoService;

    @Test
    public void testGetTodos() throws Exception {
        // 設定資料
        List<Todo> expectedList = new ArrayList<>();
        Todo todo = new Todo();
        todo.setTask("做家事");
        todo.setId(1);
        expectedList.add(todo);

        // 模擬todoService.getTodos() 回傳 expectedList
        Mockito.when(todoService.getTodos()).thenReturn(expectedList);

        // 模擬呼叫[GET] /api/todos
        String returnString = mockMvc.perform(MockMvcRequestBuilders.get("/api/todos")
                        .accept(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
                        .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);  // 指定字符編碼

        Iterable<Todo> actualList = objectMapper.readValue(returnString, new TypeReference<Iterable<Todo>>() {
        });

        // 判定回傳的body是否跟預期的一樣
        assertEquals(expectedList, actualList);
    }

    @Test
    public void testrCreateTodo() throws Exception {
        // 設定資料
        Todo mockTodo = new Todo();
        mockTodo.setId(1);
        mockTodo.setTask("做家事");
        mockTodo.setStatus(1);

        JSONObject todoObject = new JSONObject();
        todoObject.put("id", 1);
        todoObject.put("task", "做家事");

        // 模擬todoService.createTodo(todo) 回傳 id 1
        Mockito.when(todoService.createTodo(any())).thenReturn(1);

        // 模擬呼叫[POST] /api/todos
        String actual = mockMvc.perform(MockMvcRequestBuilders.post("/api/todos")
                        .accept(MediaType.APPLICATION_JSON) //response 設定型別
                        .contentType(MediaType.APPLICATION_JSON) // request 設定型別
                        .content(String.valueOf(todoObject))) // body 內容
                .andExpect(status().isCreated()) // 預期回應的status code 為 201(Created)
                .andReturn().getResponse().getContentAsString();

        assertEquals(1, Integer.parseInt(actual));
    }

    @Test
    public void testUpdateTodoSuccess() throws Exception {
        // 模擬todoService.updateTodo(1, xxx) 成功，回傳true
        Mockito.when(todoService.updateTodo(any(),any())).thenReturn(true);

        JSONObject todoObject = new JSONObject();
        todoObject.put("id", 1);
        todoObject.put("task", "做家事");
        todoObject.put("status", 1);


        // 模擬呼叫[PUT] /api/todos/{id}
        mockMvc.perform(MockMvcRequestBuilders.put("/api/todos/1")
                        .contentType(MediaType.APPLICATION_JSON) // request 設定型別
                        .content(String.valueOf(todoObject))) // body 內容
                .andExpect(status().isOk()); // 預期回應的status code 為 200(Ok)
    }

    @Test
    public void testUpdateTodoIdNotExist() throws Exception {
        // 模擬todoService.updateTodo(x, xxx) 成功，回傳false
        Mockito.when(todoService.updateTodo(any(),any())).thenReturn(false);

        JSONObject todoObject = new JSONObject();
        todoObject.put("id", 1);
        todoObject.put("task", "做家事");
        todoObject.put("status", 1);


        // 模擬呼叫[PUT] /api/todos/{id}
        mockMvc.perform(MockMvcRequestBuilders.put("/api/todos/1")
                        .contentType(MediaType.APPLICATION_JSON) // request 設定型別
                        .content(String.valueOf(todoObject))) // body 內容
                .andExpect(status().isBadRequest()); // 預期回應的status code 為 400(Bad Request)
    }
    @Test
    public void testDeleteTodoSuccess() throws Exception {
        // 模擬todoService.deleteTodo(1) 成功回傳true
        Mockito.when(todoService.deleteTodo(1)).thenReturn(true);

        // 模擬呼叫[DELETE] /api/todos/{id}
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/todos/1")
                        .accept(MediaType.APPLICATION_JSON_UTF8 ) //response 設定型別
                        .contentType(MediaType.APPLICATION_JSON)) // request 設定型別
                .andExpect(status().isNoContent()); // 預期回應的status code 為 204(No Content)
    }

    @Test
    public void testDeleteTodoIdNotExist() throws Exception {

        //模擬delete id 不存在，所以todoService.deleteTodo(100)回傳false
        Mockito.when(todoService.deleteTodo(100)).thenReturn(false);

        // 模擬呼叫[DELETE] /api/todos/{id}
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/todos/100")
                        .accept(MediaType.APPLICATION_JSON_UTF8 ) //response 設定型別
                        .contentType(MediaType.APPLICATION_JSON)) // request 設定型別
                .andExpect(status().isBadRequest()); // 預期回應的status code 為 400(Bad Request)
    }
}