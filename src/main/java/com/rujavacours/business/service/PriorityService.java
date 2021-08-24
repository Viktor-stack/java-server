package com.rujavacours.business.service;

import com.rujavacours.business.entity.Priority;
import com.rujavacours.business.repo.PriorityRepos;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class PriorityService {
    private final PriorityRepos priorityRepos;

    public PriorityService(PriorityRepos priorityRepos) {
        this.priorityRepos = priorityRepos;
    }

    public List<Priority> findAll(String email) {
        return priorityRepos.findByUserEmailOrderByIdAsc(email);
    }

    public Priority addOrUpdate(Priority priority) {
        return priorityRepos.save(priority);
    }

    public List<Priority> search(String title, String email) {
        return priorityRepos.search(title, email);
    }


    public Priority findById(Long id) {
        return priorityRepos.findById(id).get();
    }

    public void deleteById(Long id) {
        priorityRepos.deleteById(id);
    }
}
