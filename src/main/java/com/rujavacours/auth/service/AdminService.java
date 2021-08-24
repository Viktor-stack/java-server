package com.rujavacours.auth.service;

import com.rujavacours.auth.repos.UserRepos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AdminService {
    private final UserRepos userRepos;
    private final UserService userService;

    @Autowired
    public AdminService(UserRepos userRepos, UserService userService) {
        this.userRepos = userRepos;
        this.userService = userService;
    }

    public int deActivate(String uuid) {
        return userService.deActivate(uuid);
    }

}
