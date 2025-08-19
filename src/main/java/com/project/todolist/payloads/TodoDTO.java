package com.project.todolist.payloads;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.project.todolist.model.TodoStatus;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TodoDTO {

    @NotBlank(message = "TodoName is required")
	@Size(min = 5,max = 20)
	private String todoName;
    @NotBlank(message = "Description is required")
	@Size(min = 5,max = 200)
	private String description;
    @NotBlank(message = "DueDate is required")
	private String duedate;
	private TodoStatus status;
	private String createdBy;
	private LocalDateTime createdOn;
	private String updatedBy;
	private LocalDateTime updatedOn;
	
	@Override
	public String toString() {
		return " [todoName=" + todoName + ", description=" + description + ", duedate=" + duedate + ", status="
				+ status + "]";
	}
	
}
