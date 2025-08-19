package com.project.todolist.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.project.todolist.model.TodoStatus;
import com.project.todolist.payloads.ApiResponse;
import com.project.todolist.payloads.PagedResponse;
import com.project.todolist.payloads.TodoDTO;
import com.project.todolist.services.TodoServices;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/todos")
public class TodoController {

	private static final Logger log = LoggerFactory.getLogger(TodoController.class);
	
	@Autowired
	TodoServices ts;
	
	@PostMapping
	public ResponseEntity<TodoDTO> addTodo( @RequestBody  @Valid TodoDTO dto) {
		log.info("TODO is added.....");
		return new ResponseEntity<>(ts.create(dto),HttpStatus.CREATED);
	}
	

	@GetMapping
	public ResponseEntity<ApiResponse<PagedResponse<TodoDTO>>> getTodos(
			@RequestParam(defaultValue = "0",required = false) int pageNo
			,@RequestParam(defaultValue = "10",required = false) int pageSize
			,@RequestParam(defaultValue = "todoName",required = false) String sortBy
			,@RequestParam(defaultValue = "ASC",required = false) String sortDir) {
		return ResponseEntity.ok(new ApiResponse<>(true, ts.getAll(pageNo,pageSize,sortBy,sortDir)));
	}
	


	@GetMapping("/{id}")
	public ResponseEntity<ApiResponse<TodoDTO>> getTodoById(@PathVariable("id") int id) {
		return ResponseEntity.ok(new ApiResponse<>(true,ts.getById(id)));
	}
 
	@PutMapping("/{id}")
	public ResponseEntity<ApiResponse<TodoDTO>> updateStatus(@PathVariable int  id, @RequestBody @Valid TodoDTO dto) {
		return new ResponseEntity<>(new ApiResponse<>(true,ts.update(id,dto)),HttpStatus.ACCEPTED);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<ApiResponse<?>> remove(@PathVariable("id") int id) {
		ts.delete(id);
		return new ResponseEntity<ApiResponse<?>>(new ApiResponse(true,"Todo With ID "+id+" Deleted Successfully!! "),HttpStatus.OK);
	}
	
	@GetMapping("/search")
	public ResponseEntity<ApiResponse<PagedResponse<TodoDTO>>> searchTodos(@RequestParam(required = false) String name,
			@RequestParam(required = false) String desc, @RequestParam(required = false) TodoStatus status,
			@RequestParam(required = false) String dueDate,
			@RequestParam(defaultValue = "0", required = false) int pageNo,
			@RequestParam(defaultValue = "10", required = false) int pageSize,
			@RequestParam(defaultValue = "todoName", required = false) String sortBy,
			@RequestParam(defaultValue = "ASC", required = false) String sortDir) {

		PagedResponse<TodoDTO> search = ts.search(name, desc, status, dueDate, pageNo, pageSize, sortBy, sortDir);
		return ResponseEntity.ok(new ApiResponse<>(true, search));
	}
	
	
}

//@GetMapping("/desc/{desc}")
//public ResponseEntity<ApiResponse<List<TodoDTO>>> getTodoByDescription(@PathVariable String desc) {
//	return ResponseEntity.ok(new ApiResponse<>(true,ts.getTodoByDescription(desc)));
//}
//@GetMapping("/todo/desc/")
//public TodoDTO TodoByDescription(@RequestParam("description") String desc) {
//	return ts.getTodoByDescription(desc);
//}

//@GetMapping("/due/{duedate}")
//public ResponseEntity<ApiResponse<List<TodoDTO>>> getTodoByDuedate(@PathVariable String duedate) {
//	return ResponseEntity.ok(new ApiResponse<>(true,ts.getTodoByDuedate(duedate)));
//}


//@GetMapping("/status/{status}")
//public ResponseEntity<ApiResponse<List<TodoDTO>>> getTodoByStatus(@PathVariable String status) {
//	return ResponseEntity.ok(new ApiResponse<>(true,ts.getTodoByStatus(status)));
//}
//@GetMapping("/status/{status}/page/{page}/{size}")
//public List<TodoDTO> getTodoByStatus(@PathVariable TodoStatus status,@PathVariable int page,@PathVariable int size) {
//	return ts.getTodoByStatus(status,page,size);
//}
//@GetMapping("/status/{status}/page/{page}/{size}/{field}")
//public List<TodoDTO> getTodoByStatus( @PathVariable("status") TodoStatus status,@PathVariable int page,@PathVariable int size,@PathVariable String field) {
//	return ts.getTodoByStatus(status,page,size,field);
//}

//@PostMapping("/todo")
//public TodoDTO addTodos(@RequestParam("description") String desc,@RequestParam("duedate") String date,@RequestParam("status") String status) {
//	
//	return ts.addTodo(new TodoDTO(desc,date,status));
//}

//@GetMapping("/page/{page}/{size}")
//public List<TodoDTO> getTodos(@PathVariable int page,@PathVariable int size) {
//	return ts.getTodos(page,size);
//}
//
//@GetMapping("/page/{page}/{size}/{field}")
//public List<TodoDTO> getTodos(@PathVariable int page,@PathVariable int size,@PathVariable String field) {
//	return ts.getTodos(page,size,field);
//}


//@GetMapping("/search/{name}")
//public ResponseEntity<ApiResponse<List<TodoDTO>>> searchByTodoName(@PathVariable String name) {
//	return ResponseEntity.ok(new ApiResponse<>(true,ts.searchByName(name)));
//}
