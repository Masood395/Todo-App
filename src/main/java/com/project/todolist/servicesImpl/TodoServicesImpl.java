package com.project.todolist.servicesImpl;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.project.todolist.dao.TodoRepo;
import com.project.todolist.dao.UserRepo;
import com.project.todolist.exception.TodoNotFoundException;
import com.project.todolist.model.Todo;
import com.project.todolist.model.TodoStatus;
import com.project.todolist.model.User;
import com.project.todolist.payloads.PagedResponse;
import com.project.todolist.payloads.TodoDTO;
import com.project.todolist.services.TodoServices;

@Service
public class TodoServicesImpl implements TodoServices {

	@Autowired
	TodoRepo tr;
	
	@Autowired
	UserRepo ur;

	@Autowired
	ModelMapper modelMapper;
	
	
	private User getLoggedInUser() {
		String email = SecurityContextHolder.getContext().getAuthentication().getName();
	    return ur.findByEmail(email)
	                 .orElseThrow(() -> new RuntimeException("User not found"));
	}

	@Override
	public TodoDTO create(TodoDTO dto) {
		String trim = dto.getTodoName().trim();
		if(trim.length()<5) {
			throw new IllegalArgumentException("size must be min. 5");
		}
		dto.setTodoName(trim);
		if (tr.findByTodoName(dto.getTodoName()) != null) {
			throw new IllegalArgumentException("Todo with this Name : " + dto.getTodoName() + " Already Exist...");
		}

		try {
			LocalDate parsedDate = LocalDate.parse(dto.getDuedate());
			if (parsedDate.isBefore(LocalDate.now())) {
				throw new IllegalArgumentException("Due date cannot be in the past");
			}
		} catch (DateTimeParseException e) {
			throw new IllegalArgumentException("Invalid date format.Expected: yyyy-MM-dd");
		}
		User user = getLoggedInUser();
		Todo todo = modelMapper.map(dto, Todo.class);
//		todo.setDescription(dto.getDescription());
//		todo.setDuedate(dto.getDuedate());
//		todo.setStatus(dto.getStatus());
		
		todo.setUser(user);
//		todo.setCreatedOn(LocalDate.now());
//		todo.setUpdatedOn(null);
		todo.setStatus(TodoStatus.PENDING);
		return modelMapper.map(tr.save(todo), TodoDTO.class);
	}

	@Override
	public PagedResponse<TodoDTO> getAll(int pageNo, int pageSize, String field, String dir) {
		Sort sort = (dir.equalsIgnoreCase("asc") ? Sort.by(Direction.ASC, field) : Sort.by(Direction.DESC, field));
		Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
		User user = getLoggedInUser();
		Page<Todo> page = tr.findByUser(user,pageable);
		List<Todo> todoPage = page.get().toList();

		List<TodoDTO> dtoList = todoPage.stream().map(t -> modelMapper.map(t, TodoDTO.class))
				.collect(Collectors.toList());
		return new PagedResponse<>(dtoList, page.getNumber(), page.getSize(), page.getTotalElements(),
				page.getTotalPages(), page.isLast());
	}

	@Override
	public TodoDTO update(int id, TodoDTO dto) {
//		if (!status.equals(TodoStatus.PENDING) && !status.equals(TodoStatus.IN_PROGRESS)
//				&& !status.equals(TodoStatus.COMPLETED)) {
//			throw new IllegalArgumentException("Invalid status. Allowed: PENDING, IN_PROGRESS, COMPLETED");
//		}
		
		User user = getLoggedInUser();
		Todo t = tr.findByIdAndUser(id,user).orElseThrow(() -> new TodoNotFoundException("Todo with ID " + id + " not available"));
		String trim = dto.getTodoName().trim();
		if(trim.length()<5) {
			throw new IllegalArgumentException("size must be min. 5");
		}
		dto.setTodoName(trim);
		try {
			LocalDate parsedDate = LocalDate.parse(dto.getDuedate());
			LocalDate due = LocalDate.parse(t.getDuedate());
			if (due.isBefore(LocalDate.now())) {
				throw new IllegalArgumentException("Can't Update Because Due-Date is Over");
			}
			if (parsedDate.isBefore(LocalDate.now())) {
				throw new IllegalArgumentException("Due date cannot be in the past");
			}
		} catch (DateTimeParseException e) {
			throw new IllegalArgumentException("Invalid date format.Expected: yyyy-MM-dd");
		}
		if (tr.findByTodoName(dto.getTodoName()) != null) {
			if(tr.findByTodoName(dto.getTodoName()).getId()!=t.getId()) {
				throw new IllegalArgumentException("Todo with this Name : " + dto.getTodoName() + " Already Exist...");
			}
		}
		if (dto.getTodoName() != null) {
			t.setTodoName(dto.getTodoName());
		}
		t.setDescription(dto.getDescription());
		t.setDuedate(dto.getDuedate());
		if (dto.getStatus() != null) {
			t.setStatus(dto.getStatus());
		}
//		t.setUpdatedOn(LocalDate.now());
		
		
		
		return convertToDTO(tr.save(t));
	}

	@Override
	public void delete(int id) {
		int uid = getLoggedInUser().getId();
		Todo t = tr.findByIdIgnoreDeleted(id,uid);

		if (t == null) {
			throw new TodoNotFoundException("Todo with ID " + id + " Not Available..");
		}
		if (Boolean.TRUE.equals(t.isDeleted())) {
			throw new TodoNotFoundException("Todo with ID " + id + " is already deleted.");
		}
//		tr.delete(t);
		t.setDeleted(true);
//		t.setUpdatedOn(LocalDate.now());
		tr.save(t);
	}

	@Override
	public TodoDTO getById(int id) {
		User user = getLoggedInUser();
		Todo t = tr.findByIdAndUser(id,user).orElseThrow(() -> new TodoNotFoundException("Todo with ID " + id + " not available"));
		return convertToDTO(t);

	}

	@Override
	public PagedResponse<TodoDTO> search(String name, String desc, TodoStatus status, String dueDate, int pageNo,
			int pageSize, String field, String dir) {
		Sort sort = (dir.equalsIgnoreCase("asc") ? Sort.by(Direction.ASC, field) : Sort.by(Direction.DESC, field));
		Pageable pagable = PageRequest.of(pageNo, pageSize, sort);
		int uid = getLoggedInUser().getId();
		Page<Todo> page = tr.searchTodos(name, desc, status, dueDate, uid, pagable);
		List<Todo> t = page.get().toList();
		if (t.isEmpty()) {
			throw new TodoNotFoundException("Todo not available");
		}
		List<TodoDTO> dtoList = t.stream().map(l -> modelMapper.map(l, TodoDTO.class)).collect(Collectors.toList());
		return new PagedResponse<>(dtoList, page.getNumber(), page.getSize(), page.getTotalElements(),
				page.getTotalPages(), page.isLast());
	}

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	public List<TodoDTO> getTodoByDuedate(String duedate) {
		List<Todo> tlist = tr.findByDuedate(duedate);
		if (tlist.isEmpty()) {
			throw new TodoNotFoundException("Todo with DueDate '" + duedate + "' Not Available");
		}
		return tlist.stream().map(this::convertToDTO).collect(Collectors.toList());
	}

	private TodoDTO convertToDTO(Todo todo) {
//		return new TodoDTO(todo.getTodoName(), todo.getDescription(), todo.getDuedate(), todo.getStatus(),
//				todo.getCreatedOn(), todo.getUpdatedOn());
		return modelMapper.map(todo, TodoDTO.class);
	}
	
//	public List<TodoDTO> getTodos(int pageNo, int pageSize) {
//	Pageable pageable = PageRequest.of(pageNo, pageSize);
//	List<Todo> todoPage = tr.findAll(pageable).get().toList();
//	
//	return todoPage.stream() 
//			.map(t -> modelMapper.map(t, TodoDTO.class)).collect(Collectors.toList());
//}

public List<TodoDTO> searchByName(String keyword) {
	List<Todo> tlist = tr.findByTodoNameContainingIgnoreCase(keyword);
	if (tlist.isEmpty()) {
		throw new TodoNotFoundException("Todo with Name " + keyword + " Not Available");
	}

	return tlist.stream().map(t -> modelMapper.map(t, TodoDTO.class)).collect(Collectors.toList());
}

//public List<TodoDTO> getTodos() {
//List<Todo> todoList = tr.findAll();
//return todoList.stream().map(t-> new ModelMapper().map(t,TodoDTO.class)).collect(Collectors.toList());
//}

public List<TodoDTO> getTodoByDescription(String desc) {
	List<Todo> tlist = tr.findByDescription(desc);
	if (tlist.isEmpty()) {
		throw new TodoNotFoundException("Todo with Description " + desc + " Not Available");
	}

	return tlist.stream().map(t -> modelMapper.map(t, TodoDTO.class)).collect(Collectors.toList());
}

public List<TodoDTO> getTodoByStatus(String status) {
	TodoStatus s = TodoStatus.valueOf(status.toUpperCase());
	List<Todo> tlist = tr.findByStatus(s);
	if (tlist.isEmpty()) {
		throw new TodoNotFoundException("Todo with Status '" + status + "' Not Available");
	}
	return tlist.stream().map(this::convertToDTO).collect(Collectors.toList());
}

public List<TodoDTO> getTodoByStatus(TodoStatus status, int page, int size) {
	Pageable pageable = PageRequest.of(page, size);
	List<Todo> tlist = tr.findByStatus(status, pageable).get().toList();
	if (tlist.isEmpty()) {
		throw new TodoNotFoundException("Todo with Status " + status + " Not Available");
	}
	return tlist.stream().map(this::convertToDTO).collect(Collectors.toList());
}

public List<TodoDTO> getTodoByStatus(TodoStatus status, int page, int size, String field) {
	Pageable pageable = PageRequest.of(page, size, Sort.by(field));
	List<Todo> tlist = tr.findByStatus(status, pageable).get().toList();
	if (tlist.isEmpty()) {
		throw new TodoNotFoundException("Todo with Status " + status + " Not Available");
	}
	return tlist.stream().map(this::convertToDTO).collect(Collectors.toList());
}
}
