package com.project.todolist.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.hibernate.annotations.SQLRestriction;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@EntityListeners(AuditingEntityListener.class)
//@SQLDelete(sql = "UPDATE todo SET deleted = true WHERE id = ?")
@SQLRestriction("is_deleted=false")
//@Where(clause = "is_deleted=false")
@Setter
@Getter
public class Todo {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private String todoName;
	private String description;
	private String duedate;
	
	@Enumerated(EnumType.STRING)
	private TodoStatus status;
	
	@CreatedDate
	@Column(updatable = false)
	private LocalDateTime createdOn;
	
	@LastModifiedDate
	private LocalDateTime updatedOn;
	@Value("false")
	private boolean isDeleted;
	
	 @CreatedBy
	 @Column(updatable = false)
	 private String createdBy;
	 
	 @LastModifiedBy
	 private String updatedBy;
	
	@ManyToOne
	@JoinColumn(name = "user_id", nullable = false)
	private User user;
	
}
