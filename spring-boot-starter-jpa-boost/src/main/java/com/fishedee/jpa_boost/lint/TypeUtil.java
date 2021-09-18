package com.fishedee.jpa_boost.lint;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class TypeUtil {

    private static Set<Class> dbType = new HashSet<>();

    static {
        dbType.add(BigDecimal.class);
        dbType.add(BigInteger.class);
        dbType.add(byte.class);
        dbType.add(Byte.class);
        dbType.add(Short.class);
        dbType.add(short.class);
        dbType.add(Integer.class);
        dbType.add(int.class);
        dbType.add(Long.class);
        dbType.add(long.class);
        dbType.add(Float.class);
        dbType.add(float.class);
        dbType.add(Double.class);
        dbType.add(double.class);
        dbType.add(Date.class);
        dbType.add(String.class);
    }

    public static boolean isSatifyDbType(Class fieldType){
        return dbType.contains(fieldType) || fieldType.isEnum();
    }
}
