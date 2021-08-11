package com.hzc.demo.util;

import java.util.Random;

public class getRandom {
    public static String getRandomId(){
        Random random = new Random();
        StringBuilder builder = new StringBuilder();
        for (int i=0;i<32;i++){
            builder.append(random.nextInt(10));
        }
        return builder.toString();
    }
}
