package com.sam.todolist2.controller;

import com.sam.todolist2.model.entity.Todo;
import com.sam.todolist2.service.TodoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.Optional;
@Api(tags = "Todo list 相關api")
@RestController
@RequestMapping(value = "/api")
public class TodoController {
    @Autowired
    TodoService todoService;

    @ApiOperation("取得所有代辦事項列表")
    @ApiResponses({
            @ApiResponse(code=401,message="沒有權限"),
            @ApiResponse(code=404,message="找不到路徑")
    })
    @GetMapping("/todos")
    public ResponseEntity getTodos() {
        Iterable<Todo> todoList = todoService.getTodos();
        return ResponseEntity.status(HttpStatus.OK).body(todoList);
    }
    @ApiOperation("取得所有代辦事項列表")
    @ApiResponses({
            @ApiResponse(code=401,message="沒有權限"),
            @ApiResponse(code=404,message="找不到路徑")
    })
    @GetMapping("/todos/{id}")
    public Optional<Todo> getTodo(@PathVariable Integer id) {
        Optional<Todo> todo = todoService.findById(id);
        return todo;
    }
    @ApiOperation("新增代辦事項")
    @ApiResponses({
            @ApiResponse(code=401,message="沒有權限"),
            @ApiResponse(code=404,message="找不到路徑")
    })
    @PostMapping("/todos")
    public ResponseEntity createTodo(@RequestBody Todo todo) {
        Integer rlt = todoService.createTodo(todo);
        return ResponseEntity.status(HttpStatus.CREATED).body(rlt);
    }
    @ApiOperation("更改代辦事項")
    @ApiResponses({
            @ApiResponse(code=401,message="沒有權限"),
            @ApiResponse(code=404,message="找不到路徑")
    })
    @PutMapping("/todos/{id}")
    public ResponseEntity upadteTodo(@PathVariable Integer id, @RequestBody Todo todo) {
        Boolean rlt = todoService.updateTodo(id ,todo);
        if (!rlt) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Status 欄位不能為空");
        }
        return ResponseEntity.status(HttpStatus.OK).body("");
    }
    @ApiOperation("刪除代辦事項")
    @ApiResponses({
            @ApiResponse(code=401,message="沒有權限"),
            @ApiResponse(code=404,message="找不到路徑")
    })
    @DeleteMapping("/todos/{id}")
    public ResponseEntity deleteTodo(@PathVariable Integer id) {
        Boolean rlt = todoService.deleteTodo(id);
        if (!rlt) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Id 不存在");
        }
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("");
    }
}
