package com.rujavacours.auth.service;

import com.rujavacours.auth.entity.Activity;
import com.rujavacours.auth.entity.Role;
import com.rujavacours.auth.entity.User;
import com.rujavacours.auth.repos.ActivityRepos;
import com.rujavacours.auth.repos.RoleRepos;
import com.rujavacours.auth.repos.UserRepos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class UserService {
    public static final String DEFAULT_ROLE = "USER";
    private final RoleRepos roleRepos;
    private final UserRepos userRepos;
    private final ActivityRepos activityRepos;

    @Autowired
    public UserService(RoleRepos roleRepos, UserRepos userRepos, ActivityRepos activityRepos) {
        this.roleRepos = roleRepos;
        this.userRepos = userRepos;
        this.activityRepos = activityRepos;
    }

    public void register(User user, Activity activity) {
        userRepos.save(user);
        activityRepos.save(activity);
    }

    public User updateUser(User user) {
        return userRepos.save(user);
    }

    public User getUserById(Long id) {
        return userRepos.findById(id).get();
    }

    public boolean userExists(String username, String email) {
        if (userRepos.existsByUsername(username)) {
            return true;
        }
        if (userRepos.existsByEmail(email)) {
            return true;
        }
        return false;
    }

    public Optional<Role> findByName(String name) {
        return roleRepos.findByName(name);
    }

    public Optional<Activity> findActivityByUuid(String uuid) {
        return activityRepos.findByUuid(uuid);
    }

    public Optional<Activity> findActivityById(Long id) {
        return activityRepos.findByUserId(id);
    }

    public int activate(String uuid) {
        return activityRepos.changeActivated(uuid, true);
    }

    public int deActivate(String uuid) {
        return activityRepos.changeActivated(uuid, false);
    }

    public int updatePassword(String password, String username) {
        return userRepos.updatePassword(password, username);
    }

}
