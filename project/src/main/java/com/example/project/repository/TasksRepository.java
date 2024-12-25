package com.example.project.repository;

import com.example.project.models.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;


public interface TasksRepository extends JpaRepository<Task, Long> {
    Page<Task> findByAuthorId(@Param("authorId") Long authorId, Pageable pageable);
    Page<Task> findByExecutorId(@Param("executorId") Long executorId, Pageable pageable);
    Page<Task> findByTitleContaining(@Param("title") String title, Pageable pageable);
}
