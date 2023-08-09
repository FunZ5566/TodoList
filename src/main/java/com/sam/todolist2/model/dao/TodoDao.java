package com.sam.todolist2.model.dao;

import com.sam.todolist2.model.entity.Todo;
import org.springframework.data.repository.CrudRepository;

public interface TodoDao extends CrudRepository<Todo,Integer> {
}
