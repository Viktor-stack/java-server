package com.rujavacours.business.controllers;


import com.rujavacours.business.entity.Stat;
import com.rujavacours.business.service.StatService;
import com.rujavacours.business.util.MyLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StatController {
    private final StatService statService;

    @Autowired
    public StatController(StatService statService) {
        this.statService = statService;
    }

    @PostMapping("/stat")
    public ResponseEntity<Stat> findByEmail(@RequestBody String email) {
        MyLogger.debugMethodName("StatController findByUserEmail(String)----------------------------------------->");
        return ResponseEntity.ok(statService.findStat(email));
    }

}
