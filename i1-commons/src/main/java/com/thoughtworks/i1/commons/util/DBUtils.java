package com.thoughtworks.i1.commons.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityTransaction;

public class DBUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(DBUtils.class);

    public static void rollBackQuietly(EntityTransaction transaction){
        if(transaction!=null){
            try {
                transaction.rollback();
            } catch (Exception e) {
                LOGGER.info("Failed to roll back transaction: " + e.getMessage());
            }
        }
    }
}
