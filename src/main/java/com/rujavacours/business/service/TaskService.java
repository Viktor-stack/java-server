package com.rujavacours.business.service;


import com.rujavacours.business.entity.Task;
import com.rujavacours.business.repo.TaskRepo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@Transactional
public class TaskService {
    private final TaskRepo taskRepo;


    public TaskService(TaskRepo taskRepo) {
        this.taskRepo = taskRepo;
    }

    public List<Task> findAll(String email) {
        return taskRepo.findByUserEmailOrderByIdAsc(email);
    }

    public Task findById(Long id) {
        return taskRepo.findById(id).get();
    }

    public Task addOnUpdate(Task task) {
        return taskRepo.save(task);
    }

    public void delete(Long id) {
        taskRepo.deleteById(id);
    }

    public Page<Task> find(String title,
                           Integer completed,
                           Long categoryId,
                           Long priorityId,
                           String email,
                           Date dateFrom,
                           Date dateTo,
                           PageRequest pageable) {
        return taskRepo.find(title, completed, categoryId, priorityId, email, dateFrom, dateTo, pageable);
    }

}
