package com.avmathman;

import com.avmathman.snowflake.SnowflakeIdGenerator;

public class Main {
    public static void main(String[] args) {
        SnowflakeIdGenerator snowflakeIdGenerator = new SnowflakeIdGenerator(1L, 1L);

        int size = 100;
        for (int i = 0; i < size; i++) {
            long id = snowflakeIdGenerator.generate();

            System.out.println("generated: " + id);
        }
    }
}