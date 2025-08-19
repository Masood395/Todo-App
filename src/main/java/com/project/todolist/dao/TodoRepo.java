package com.project.todolist.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.project.todolist.model.Todo;
import com.project.todolist.model.TodoStatus;
import com.project.todolist.model.User;

public interface TodoRepo extends JpaRepository<Todo, Integer>, JpaSpecificationExecutor<Todo> {

	@Query(value = "SELECT * FROM todo WHERE id = :id AND user_id= :uid", nativeQuery = true)
	Todo findByIdIgnoreDeleted(@Param("id") int id ,@Param("uid") int uid);

	
	List<Todo> findByDescription(String desc);
	Todo findByTodoName(String name);
	List<Todo> findByStatus(TodoStatus status);
	List<Todo> findByDuedate(String duedate);
	
	List<Todo> findByIsDeletedFalse();
	List<Todo> findByIsDeletedTrue();

	List<Todo> findByTodoNameContainingIgnoreCase(String keyword);
	
	Page<Todo> findByStatus(TodoStatus status,Pageable pageable);

	Todo deleteByTodoName(String desc);
	Todo findByTodoNameAndIsDeletedFalse(String desc);
	
	@Query("""
			    SELECT t
			    FROM Todo t
			    WHERE (:name IS NULL OR :name = '' OR LOWER(t.todoName) LIKE LOWER(CONCAT('%', :name, '%')))
			    AND (:status IS NULL OR :status = '' OR t.status = :status)
			    AND (:dueDate IS NULL OR :dueDate = '' OR t.duedate = :dueDate)
			    AND (:description IS NULL OR :description = '' OR LOWER(t.description) LIKE LOWER(CONCAT('%', :description, '%')))
			    AND t.user.id= :uid
			""")
	Page<Todo> searchTodos(@Param("name") String name, @Param("description") String desc,
			@Param("status") TodoStatus status, @Param("dueDate") String dueDate,@Param("uid") int uid, Pageable pageable);
	
	
	Page<Todo> findByUser(User user,Pageable pageable);


	Optional<Todo> findByIdAndUser(int id, User user);
	
	
	
	
	
	
	
	
}
