package com.rujavacours.business.controllers;

import com.rujavacours.business.DTO.TaskDTO;
import com.rujavacours.business.entity.Task;
import com.rujavacours.business.service.TaskService;
import com.rujavacours.business.util.MyLogger;
import com.rujavacours.business.util.ValidationStringAndId;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/task")
public class TaskController {
    private static final String ID_COLUMN = "id";
    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }


    @GetMapping("/test")
    public String test() {
        return "TEST-TEST";
    }


    @PostMapping("/all")
    public ResponseEntity<List<Task>> findAll(@RequestBody String email) {
        MyLogger.debugMethodName("task findAll(String)----------------------------------------------------------->");
        return ResponseEntity.ok(taskService.findAll(email));
    }

    @PutMapping("/add")
    public ResponseEntity<Task> add(@RequestBody Task task) {
        MyLogger.debugMethodName("task add(Task)----------------------------------------------------------------->");
        if (ValidationStringAndId.isNoValidId(task.getId())) {
            return new ResponseEntity("id Должен быть пустым", HttpStatus.NOT_ACCEPTABLE);
        }
        if (ValidationStringAndId.isValidString(task.getTitle())) {
            return new ResponseEntity("missing param: title", HttpStatus.NOT_ACCEPTABLE);
        }
        return ResponseEntity.ok(taskService.addOnUpdate(task));
    }

    @PatchMapping("/update")
    public ResponseEntity update(@RequestBody Task task) {
        MyLogger.debugMethodName("task update(Task)-------------------------------------------------------------->");
        if (ValidationStringAndId.isValidId(task.getId())) {
            return new ResponseEntity("ID= " + task.getId() + " not fount", HttpStatus.NOT_ACCEPTABLE);
        }
        if (ValidationStringAndId.isValidString(task.getTitle())) {
            return new ResponseEntity("missed params: title", HttpStatus.NOT_ACCEPTABLE);
        }
        taskService.addOnUpdate(task);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PostMapping("/id")
    public ResponseEntity<Task> findById(@RequestBody Long id) {
        MyLogger.debugMethodName("task findById(Long ID)---------------------------------------------------------------->");
        Task task = null;
        try {
            task = taskService.findById(id);
        } catch (NoSuchElementException e) {
            e.printStackTrace();
            return new ResponseEntity("id= " + id + " not found", HttpStatus.NOT_ACCEPTABLE);
        }
        return ResponseEntity.ok(task);
    }

    @DeleteMapping("/delete")
    public ResponseEntity delete(@RequestBody Long id) {
        MyLogger.debugMethodName("task delete(Long)------------------------------------------------------------------>");
        try {
            taskService.delete(id);
        } catch (EmptyResultDataAccessException e) {
            e.printStackTrace();
            return new ResponseEntity("ID= " + id + " not found", HttpStatus.NOT_ACCEPTABLE);
        }
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PostMapping("/search")
    public ResponseEntity<Page<Task>> search(@RequestBody TaskDTO taskDTO) {
        MyLogger.debugMethodName("task search(TaskDTO)------------------------------------------------------------------->");
        String title = taskDTO.getTitle();
        Integer completed = taskDTO.getCompleted();
        Long categoryId = taskDTO.getCategoryId();
        Long priorityId = taskDTO.getPriorityId();
        String sortColumn = taskDTO.getSortColumn();
        String sortDirection = taskDTO.getSortDirection();
        Integer pageNumber = taskDTO.getPageNumber();
        Integer pageSize = taskDTO.getPageSize();
        String email = taskDTO.getEmail();
        Date dateFrom = null;
        Date dateTo = null;
        if (taskDTO.getDateFrom() != null) {
            Calendar calendarFrom = Calendar.getInstance();
            calendarFrom.setTime(taskDTO.getDateFrom());
            calendarFrom.set(Calendar.HOUR_OF_DAY, 0);
            calendarFrom.set(Calendar.MINUTE, 0);
            calendarFrom.set(Calendar.SECOND, 0);
            calendarFrom.set(Calendar.MILLISECOND, 0);
            dateFrom = calendarFrom.getTime();
        }
        if (taskDTO.getDateTo() != null) {
            Calendar calendarFrom = Calendar.getInstance();
            calendarFrom.setTime(taskDTO.getDateTo());
            calendarFrom.set(Calendar.HOUR_OF_DAY, 23);
            calendarFrom.set(Calendar.MINUTE, 59);
            calendarFrom.set(Calendar.SECOND, 59);
            calendarFrom.set(Calendar.MILLISECOND, 999);
            dateTo = calendarFrom.getTime();
        }
        Sort.Direction direction = sortDirection == null || sortDirection.trim().length() == 0 || sortDirection.trim().equals("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        if (sortColumn == null) {
            sortColumn = ID_COLUMN;
        }
        Sort sort = Sort.by(direction, sortColumn, ID_COLUMN);
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize, sort);
        Page<Task> result = taskService.find(title, completed, categoryId, priorityId, email, dateFrom, dateTo, pageRequest);
        return ResponseEntity.ok(result);
    }

}
