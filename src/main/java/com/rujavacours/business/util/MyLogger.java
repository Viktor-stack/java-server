package com.rujavacours.business.util;

import lombok.extern.java.Log;

import java.util.logging.Level;


@Log
public class MyLogger {
    public static void debugMethodName(String text) {
        System.out.println();
        System.out.println();
        log.log(Level.INFO, text);
    }
}
