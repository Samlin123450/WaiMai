package com.sam.Reggie.common;

public class threadValue {
    private static ThreadLocal<Long> local= new ThreadLocal<>();

    public static void setId(long id){
        local.set(id);
    }

    public static long getId(){
        return local.get();
    }
}
