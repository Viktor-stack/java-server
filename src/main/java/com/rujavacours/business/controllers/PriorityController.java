package com.rujavacours.business.controllers;

import com.rujavacours.business.DTO.PriorityDTO;
import com.rujavacours.business.entity.Priority;
import com.rujavacours.business.service.PriorityService;
import com.rujavacours.business.util.MyLogger;
import com.rujavacours.business.util.ValidationStringAndId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/priority")
public class PriorityController {
    private final PriorityService priorityService;

    @Autowired
    public PriorityController(PriorityService priorityService) {
        this.priorityService = priorityService;
    }


    @PostMapping("/all")
    public ResponseEntity<List<Priority>> findPriority(@RequestBody String email) {
        MyLogger.debugMethodName("Priority findPriority()-------------------------------------------------------->");
        return ResponseEntity.ok(priorityService.findAll(email));
    }


    @PostMapping("/search")
    public ResponseEntity<List<Priority>> search(@RequestBody PriorityDTO priorityDTO) {
        MyLogger.debugMethodName("PriorityController  search(String)--------------------------------------------->");
        return ResponseEntity.ok(priorityService.search(priorityDTO.getTitle(), priorityDTO.getEmail()));
    }

    @PutMapping("/add")
    public ResponseEntity<Priority> addPriority(@RequestBody Priority priority) {
        MyLogger.debugMethodName("Priority add(priority)--------------------------------------------------------->");
        if (ValidationStringAndId.isNoValidId(priority.getId())) {
            return new ResponseEntity("redundant param: is MUST be null", HttpStatus.NOT_ACCEPTABLE);
        }
        if (ValidationStringAndId.isValidString(priority.getTitle())) {
            return new ResponseEntity("The title must not be empty", HttpStatus.NOT_ACCEPTABLE);
        }

        return ResponseEntity.ok(priorityService.addOrUpdate(priority));
    }

    @PatchMapping("/update")
    public ResponseEntity update(@RequestBody Priority priority) {
        if (ValidationStringAndId.isValidId(priority.getId())) {
            return new ResponseEntity("missed param: id", HttpStatus.NOT_ACCEPTABLE);
        }
        if (ValidationStringAndId.isValidString(priority.getTitle())) {
            return new ResponseEntity("missed param: title", HttpStatus.NOT_ACCEPTABLE);
        }
        if (ValidationStringAndId.isValidString(priority.getColor())) {
            return new ResponseEntity("missed param: color", HttpStatus.NOT_ACCEPTABLE);
        }
        priorityService.addOrUpdate(priority);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PostMapping("/id")
    public ResponseEntity<Priority> fainById(@RequestBody Long id) {
        MyLogger.debugMethodName("Priority fainById()------------------------------------------------------------>");
        if (ValidationStringAndId.isValidId(id)) {
            return new ResponseEntity("missed param: id", HttpStatus.NOT_ACCEPTABLE);
        }
        Priority priority = null;
        try {
            priority = priorityService.findById(id);
        } catch (NoSuchElementException e) {
            e.printStackTrace();
            return new ResponseEntity("id= " + id + " not found", HttpStatus.NOT_ACCEPTABLE);
        }
        return ResponseEntity.ok(priority);
    }

    @DeleteMapping("/delete")
    public ResponseEntity delete(@RequestBody Long id) {
        MyLogger.debugMethodName("PriorityController delete(id)-------------------------------------------------->");
        if (ValidationStringAndId.isValidId(id)) {
            return new ResponseEntity("id= " + id + " not found", HttpStatus.NOT_ACCEPTABLE);
        }
        try {
            priorityService.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            e.printStackTrace();
            return new ResponseEntity("id= " + id + " not found", HttpStatus.NOT_ACCEPTABLE);
        }
        return ResponseEntity.ok(HttpStatus.OK);
    }
}
