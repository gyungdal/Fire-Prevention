package com.codezero.fireprevention.database;

/**
 * Created by GyungDal on 2016-07-18.
 */
public class DBConfig {
    public static final String DB_NAME = "data.db";
    public static final String TABLE_NAME = "sensors";
    public static boolean isSafe;
    public static int NotSafeNumber;
    static {
        isSafe = true;
        NotSafeNumber = 0;
    }
}
