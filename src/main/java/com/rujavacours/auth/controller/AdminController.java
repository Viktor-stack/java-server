package com.rujavacours.auth.controller;

import com.rujavacours.auth.Dto.AdminDto;
import com.rujavacours.auth.entity.Role;
import com.rujavacours.auth.entity.User;
import com.rujavacours.auth.exception.JsonException;
import com.rujavacours.auth.repos.UserRepos;
import com.rujavacours.auth.service.AdminService;
import com.rujavacours.auth.service.UserService;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.management.relation.RoleNotFoundException;


@Log
@RestController
@RequestMapping("/admin")
public class AdminController {
    private final AdminService adminService;
    private final UserService userService;

    @Autowired
    public AdminController(AdminService adminService, UserRepos userRepos, UserService userService) {
        this.adminService = adminService;
        this.userService = userService;
    }

    @PostMapping("/deActivate")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Boolean> deActivateUser(@RequestBody String uuid) {
        int activate = adminService.deActivate(uuid);
        return ResponseEntity.ok().body(activate == 1);
    }

    @PatchMapping("/addRole")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<User> addRole(@RequestBody AdminDto adminDto) throws RoleNotFoundException {
        User user = userService.getUserById(adminDto.getUserId());
        Role userRole = userService.findByName(adminDto.getRoleName().toUpperCase())
                .orElseThrow(() -> new RoleNotFoundException("Default Role USER not found.")); // если в БД нет такой роли - выбрасываем исключение
        user.getRoles().add(userRole);
        return ResponseEntity.ok().body(userService.updateUser(user));
    }

    @PatchMapping("/deleteRole")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<User> deleteRole(@RequestBody AdminDto adminDto) throws RoleNotFoundException {
        User user = userService.getUserById(adminDto.getUserId());
        Role userRole = userService.findByName(adminDto.getRoleName().toUpperCase())
                .orElseThrow(() -> new RoleNotFoundException("Default Role USER not found.")); // если в БД нет такой роли - выбрасываем исключение
        user.getRoles().remove(userRole);
        return ResponseEntity.ok().body(userService.updateUser(user));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<JsonException> handlerException(Exception ex) {
        return new ResponseEntity(new JsonException(ex.getClass().getSimpleName(), ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

}
