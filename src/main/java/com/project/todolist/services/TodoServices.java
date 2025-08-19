package com.project.todolist.services;

import com.project.todolist.model.TodoStatus;
import com.project.todolist.payloads.PagedResponse;
import com.project.todolist.payloads.TodoDTO;

public interface TodoServices {

	TodoDTO create(TodoDTO dto);
	TodoDTO update(int id, TodoDTO dto);
	TodoDTO getById(int id);
	PagedResponse<TodoDTO> getAll(int pageNo, int pageSize,String field,String dir) ;
    PagedResponse<TodoDTO> search(String name, String desc, TodoStatus status, String dueDate,
    		int pageNo, int pageSize,String field,String dir);
	void delete(int id);
	
}
