package com.example.commons.core.utils;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.Objects;
import java.util.TimeZone;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import com.alibaba.fastjson2.JSON;
import com.example.commons.core.exceptions.CommonUtilsException;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * <p>
 * 类型转换 {@link com.alibaba.fastjson2.util.TypeUtils}
 * <P>对toDate进行加强</P>
 * </p>
 *
 * @author : 21
 * @since : 2024/9/24 15:50
 */

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public abstract class TypeUtils {

    private static boolean oracleTimestampMethodInited = false;
    private static Method oracleTimestampMethod;

    private static boolean oracleDateMethodInited = false;
    private static Method oracleDateMethod;

    //从fastjson1中抽取的变量
    public static TimeZone defaultTimeZone = TimeZone.getDefault();
    public static Locale defaultLocale = Locale.getDefault();
    public static String DEFFAULT_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    public static String castToString(Object value) {
        if (value == null) {
            return null;
        }
        return value.toString();
    }

    public static String castToString(Object value, String defaults) {
        String castString = castToString(value);
        return Objects.isNull(castString) ? defaults : castString;
    }

    public static Byte castToByte(Object value) {
        if (value == null) {
            return null;
        }

        if (value instanceof Number) {
            return ((Number) value).byteValue();
        }

        if (value instanceof String strVal) {
            if (strVal.length() == 0
                    || "null".equals(strVal)
                    || "NULL".equals(strVal)) {
                return null;
            }

            return Byte.parseByte(strVal);
        }

        throw new CommonUtilsException("can not cast to byte, value : " + value);
    }

    public static Byte castToByte(Object value, Byte defaults) {
        Byte castToByte = castToByte(value);
        return Objects.isNull(castToByte) ? defaults : castToByte;
    }

    public static Character castToChar(Object value) {
        if (value == null) {
            return null;
        }

        if (value instanceof Character) {
            return (Character) value;
        }

        if (value instanceof String strVal) {

            if (strVal.length() == 0) {
                return null;
            }

            if (strVal.length() != 1) {
                throw new CommonUtilsException("can not cast to char, value : " + value);
            }

            return strVal.charAt(0);
        }

        throw new CommonUtilsException("can not cast to char, value : " + value);
    }

    public static Character castToChar(Object value, Character defaults) {
        Character castToChar = castToChar(value);
        return Objects.isNull(castToChar) ? defaults : castToChar;
    }

    public static Short castToShort(Object value) {
        if (value == null) {
            return null;
        }

        if (value instanceof Number) {
            return ((Number) value).shortValue();
        }

        if (value instanceof String strVal) {
            if (strVal.length() == 0
                    || "null".equals(strVal)
                    || "NULL".equals(strVal)) {
                return null;
            }

            return Short.parseShort(strVal);
        }

        throw new CommonUtilsException("can not cast to short, value : " + value);
    }

    public static Short castToShort(Object value, Short defaults) {
        Short castToShort = castToShort(value);
        return Objects.isNull(castToShort) ? defaults : castToShort;
    }

    public static BigDecimal castToBigDecimal(Object value) {
        if (value == null) {
            return null;
        }

        if (value instanceof BigDecimal) {
            return (BigDecimal) value;
        }

        if (value instanceof BigInteger) {
            return new BigDecimal((BigInteger) value);
        }

        String strVal = value.toString();
        if (strVal.length() == 0) {
            return null;
        }

        return new BigDecimal(strVal);
    }

    public static BigDecimal castToBigDecimal(Object value, BigDecimal defaults) {
        BigDecimal bigDecimal = castToBigDecimal(value);
        return Objects.isNull(bigDecimal) ? defaults : bigDecimal;

    }

    public static BigInteger castToBigInteger(Object value) {
        if (value == null) {
            return null;
        }

        if (value instanceof BigInteger) {
            return (BigInteger) value;
        }

        if (value instanceof Float || value instanceof Double) {
            return BigInteger.valueOf(((Number) value).longValue());
        }

        String strVal = value.toString();
        if (strVal.length() == 0
                || "null".equals(strVal)
                || "NULL".equals(strVal)) {
            return null;
        }

        return new BigInteger(strVal);
    }

    public static BigInteger castToBigInteger(Object value, BigInteger defaults) {

        BigInteger bigInteger = castToBigInteger(value);
        return Objects.isNull(bigInteger) ? defaults : bigInteger;

    }

    public static Float castToFloat(Object value) {
        if (value == null) {
            return null;
        }

        if (value instanceof Number) {
            return ((Number) value).floatValue();
        }

        if (value instanceof String) {
            String strVal = value.toString();
            if (strVal.length() == 0
                    || "null".equals(strVal)
                    || "NULL".equals(strVal)) {
                return null;
            }

            if (strVal.indexOf(',') != 0) {
                strVal = strVal.replaceAll(",", "");
            }
            try {
                return Float.parseFloat(strVal);
            } catch (Exception e) {
                return null;
            }
        }

        throw new CommonUtilsException("can not cast to float, value : " + value);
    }

    public static Float castToFloat(Object value, Float defaults) {
        Float castToFloat = castToFloat(value);
        return Objects.isNull(castToFloat) ? defaults : castToFloat;

    }

    public static Double castToDouble(Object value) {
        if (value == null) {
            return null;
        }

        if (value instanceof Number) {
            return ((Number) value).doubleValue();
        }

        if (value instanceof String strVal) {
            if (strVal.length() == 0
                    || "null".equals(strVal)
                    || "NULL".equals(strVal)) {
                return null;
            }

            if (strVal.indexOf(',') != 0) {
                strVal = strVal.replaceAll(",", "");
            }

            return Double.parseDouble(strVal);
        }

        throw new CommonUtilsException("can not cast to double, value : " + value);
    }

    public static Double castToDouble(Object value, Double defaults) {
        Double castToDouble = castToDouble(value);
        return Objects.isNull(castToDouble) ? defaults : castToDouble;

    }

    public static Date castToDate(Object value) {
        if (value == null) {
            return null;
        }
        // 使用频率最高的，应优先处理
        if (value instanceof Date) {
            return (Date) value;
        }

        if (value instanceof Calendar) {
            return ((Calendar) value).getTime();
        }

        long longValue = -1;

        if (value instanceof Number) {
            longValue = ((Number) value).longValue();
            return new Date(longValue);
        }

        if (value instanceof String strVal) {
            if (strVal.length() == 0) {
                return null;
            }
            if (strVal.startsWith("/Date(") && strVal.endsWith(")/")) {
                strVal = strVal.substring(6, strVal.length() - 2);
            }
            if (strVal.indexOf('-') != -1) {
                String format;
                if (strVal.length() == DEFFAULT_DATE_FORMAT.length()) {
                    format = DEFFAULT_DATE_FORMAT;
                } else if (strVal.length() == 10) {
                    format = "yyyy-MM-dd";
                } else if (strVal.length() == "yyyy-MM-dd HH:mm:ss".length()) {
                    format = "yyyy-MM-dd HH:mm:ss";
                } else {
                    format = "yyyy-MM-dd HH:mm:ss.SSS";
                }

                SimpleDateFormat dateFormat = new SimpleDateFormat(format, defaultLocale);
                dateFormat.setTimeZone(defaultTimeZone);
                try {
                    return dateFormat.parse(strVal);
                } catch (ParseException e) {
                    throw new CommonUtilsException("can not cast to Date, value : " + strVal);
                }
            }
            longValue = Long.parseLong(strVal);
        }

        if (longValue < 0) {
            Class<?> clazz = value.getClass();
            if ("oracle.sql.TIMESTAMP".equals(clazz.getName())) {
                if (oracleTimestampMethod == null && !oracleTimestampMethodInited) {
                    try {
                        oracleTimestampMethod = clazz.getMethod("toJdbc");
                    } catch (NoSuchMethodException e) {
                        // skip
                    } finally {
                        oracleTimestampMethodInited = true;
                    }
                }

                Object result;
                try {
                    result = oracleTimestampMethod != null ? oracleTimestampMethod.invoke(value) : null;
                } catch (Exception e) {
                    throw new CommonUtilsException("can not cast oracle.sql.TIMESTAMP to Date", e);
                }
                return (Date) result;
            }

            if ("oracle.sql.DATE".equals(clazz.getName())) {
                if (oracleDateMethod == null && !oracleDateMethodInited) {
                    try {
                        oracleDateMethod = clazz.getMethod("toJdbc");
                    } catch (NoSuchMethodException e) {
                        // skip
                    } finally {
                        oracleDateMethodInited = true;
                    }
                }

                Object result;
                try {
                    result = oracleDateMethod != null ? oracleDateMethod.invoke(value) : null;
                } catch (Exception e) {
                    throw new CommonUtilsException("can not cast oracle.sql.DATE to Date", e);
                }
                return (Date) result;
            }

            throw new CommonUtilsException("can not cast to Date, value : " + value);
        }

        return new Date(longValue);
    }

    public static Date castToDate(Object value, Date defaults) {
        Date castToDate = castToDate(value);
        return Objects.isNull(castToDate) ? defaults : castToDate;

    }

    @SuppressWarnings("Duplicates")
    public static java.sql.Date castToSqlDate(Object value) {
        if (value == null) {
            return null;
        }

        if (value instanceof java.sql.Date) {
            return (java.sql.Date) value;
        }

        if (value instanceof Date) {
            return new java.sql.Date(((Date) value).getTime());
        }

        if (value instanceof Calendar) {
            return new java.sql.Date(((Calendar) value).getTimeInMillis());
        }

        long longValue = 0;

        if (value instanceof Number) {
            longValue = ((Number) value).longValue();
        }

        if (value instanceof String strVal) {
            if (strVal.length() == 0
                    || "null".equals(strVal)
                    || "NULL".equals(strVal)) {
                return null;
            }

            longValue = Long.parseLong(strVal);
        }

        if (longValue <= 0) {
            throw new CommonUtilsException("can not cast to Date, value : " + value);
        }

        return new java.sql.Date(longValue);
    }

    public static java.sql.Date castToSqlDate(Object value, java.sql.Date defaults) {
        java.sql.Date castToSqlDate = castToSqlDate(value);
        return Objects.isNull(castToSqlDate) ? defaults : castToSqlDate;

    }

    @SuppressWarnings("Duplicates")
    public static java.sql.Timestamp castToTimestamp(Object value) {
        if (value == null) {
            return null;
        }

        if (value instanceof Calendar) {
            return new java.sql.Timestamp(((Calendar) value).getTimeInMillis());
        }

        if (value instanceof java.sql.Timestamp) {
            return (java.sql.Timestamp) value;
        }

        if (value instanceof Date) {
            return new java.sql.Timestamp(((Date) value).getTime());
        }

        long longValue = 0;

        if (value instanceof Number) {
            longValue = ((Number) value).longValue();
        }

        if (value instanceof String strVal) {
            if (strVal.length() == 0
                    || "null".equals(strVal)
                    || "NULL".equals(strVal)) {
                return null;
            }

            longValue = Long.parseLong(strVal);
        }

        if (longValue <= 0) {
            throw new CommonUtilsException("can not cast to Date, value : " + value);
        }

        return new java.sql.Timestamp(longValue);
    }

    public static java.sql.Timestamp castToTimestamp(Object value, java.sql.Timestamp defaults) {
        java.sql.Timestamp castToTimestamp = castToTimestamp(value);
        return Objects.isNull(castToTimestamp) ? defaults : castToTimestamp;

    }

    public static Long castToLong(Object value) {
        if (value == null) {
            return null;
        }

        if (value instanceof Number) {
            return ((Number) value).longValue();
        }

        if (value instanceof String strVal) {
            if (strVal.length() == 0
                    || "null".equals(strVal)
                    || "NULL".equals(strVal)) {
                return null;
            }

            if (strVal.indexOf(',') != 0) {
                strVal = strVal.replaceAll(",", "");
            }

            try {
                return Long.parseLong(strVal);
            } catch (NumberFormatException ex) {
                //
            }
        }

        throw new IllegalArgumentException("can not cast to long, value : " + value);
    }

    public static Long castToLong(Object value, Long defaults) {
        Long castToLong = castToLong(value);
        return Objects.isNull(castToLong) ? defaults : castToLong;

    }

    public static Integer castToInt(Object value) {
        if (value == null) {
            return null;
        }

        if (value instanceof Integer) {
            return (Integer) value;
        }

        if (value instanceof Number) {
            return ((Number) value).intValue();
        }

        if (value instanceof String strVal) {

            if (strVal.length() == 0
                    || "null".equals(strVal)
                    || "NULL".equals(strVal)) {
                return null;
            }

            if (strVal.indexOf(',') != 0) {
                strVal = strVal.replaceAll(",", "");
            }
            try {
                return Integer.parseInt(strVal);
            } catch (Exception e) {
                return null;
            }
        }

        if (value instanceof Boolean) {
            return (Boolean) value ? 1 : 0;
        }

        return null;
    }

    public static Integer castToInt(Object value, Integer defaults) {
        Integer castToInt = castToInt(value);
        return Objects.isNull(castToInt) ? defaults : castToInt;

    }

    public static byte[] castToBytes(Object value) {
        if (value instanceof byte[]) {
            return (byte[]) value;
        }

        if (value instanceof String) {
            return Base64.getDecoder().decode((String) value);
        }
        throw new CommonUtilsException("can not cast to int, value : " + value);
    }

    public static byte[] castToBytes(Object value, byte[] defaults) {
        byte[] castToBytes = castToBytes(value);
        return Objects.isNull(castToBytes) ? defaults : castToBytes;

    }

    public static Boolean castToBoolean(Object value) {
        if (value == null) {
            return null;
        }

        if (value instanceof Boolean) {
            return (Boolean) value;
        }

        if (value instanceof Number) {
            return ((Number) value).intValue() == 1;
        }

        if (value instanceof String strVal) {

            if (strVal.length() == 0
                    || "null".equals(strVal)
                    || "NULL".equals(strVal)) {
                return null;
            }

            if ("true".equalsIgnoreCase(strVal)
                    || "1".equals(strVal)) {
                return Boolean.TRUE;
            }

            if ("false".equalsIgnoreCase(strVal)
                    || "0".equals(strVal)) {
                return Boolean.FALSE;
            }
        }

        throw new CommonUtilsException("can not cast to boolean, value : " + value);
    }

    public static Boolean castToBoolean(Object value, Boolean defaults) {
        Boolean castToBoolean = castToBoolean(value);
        return Objects.isNull(castToBoolean) ? defaults : castToBoolean;

    }

    @SuppressWarnings({"rawtypes"})
    public static <T> T castToEnum(Object obj, Class<T> clazz) {
        try {
            if (obj instanceof String name) {
                if (name.length() == 0) {
                    return null;
                }

                return (T) Enum.valueOf((Class<? extends Enum>) clazz, name);
            }

            if (obj instanceof Number) {
                int ordinal = ((Number) obj).intValue();
                Object[] values = clazz.getEnumConstants();
                if (ordinal < values.length) {
                    return (T) values[ordinal];
                }
            }
        } catch (Exception ex) {
            throw new CommonUtilsException("can not cast to : " + clazz.getName(), ex);
        }

        throw new CommonUtilsException("can not cast to : " + clazz.getName());
    }

    public static <T> T castToEnum(Object value, Class<T> clazz, T defaults) {
        T castToEnum = castToEnum(value, clazz);
        return Objects.isNull(castToEnum) ? defaults : castToEnum;
    }

    public static <T> T cast(Object obj, Class<T> clazz) {
        if (obj == null) {
            if (clazz == int.class) {
                return (T) Integer.valueOf(0);
            } else if (clazz == long.class) {
                return (T) Long.valueOf(0);
            } else if (clazz == short.class) {
                return (T) Short.valueOf((short) 0);
            } else if (clazz == byte.class) {
                return (T) Byte.valueOf((byte) 0);
            } else if (clazz == float.class) {
                return (T) Float.valueOf(0);
            } else if (clazz == double.class) {
                return (T) Double.valueOf(0);
            } else if (clazz == boolean.class) {
                return (T) Boolean.FALSE;
            }
            return null;
        }

        if (clazz == null) {
            throw new IllegalArgumentException("clazz is null");
        }

        if (clazz == obj.getClass()) {
            return (T) obj;
        }

        if (clazz.isAssignableFrom(obj.getClass())) {
            return (T) obj;
        }

        if (clazz == boolean.class || clazz == Boolean.class) {
            return (T) castToBoolean(obj);
        }

        if (clazz == byte.class || clazz == Byte.class) {
            return (T) castToByte(obj);
        }

        if (clazz == char.class || clazz == Character.class) {
            return (T) castToChar(obj);
        }

        if (clazz == short.class || clazz == Short.class) {
            return (T) castToShort(obj);
        }

        if (clazz == int.class || clazz == Integer.class) {
            return (T) castToInt(obj);
        }

        if (clazz == long.class || clazz == Long.class) {
            return (T) castToLong(obj);
        }

        if (clazz == float.class || clazz == Float.class) {
            return (T) castToFloat(obj);
        }

        if (clazz == double.class || clazz == Double.class) {
            return (T) castToDouble(obj);
        }

        if (clazz == String.class) {
            return (T) castToString(obj);
        }

        if (clazz == BigDecimal.class) {
            return (T) castToBigDecimal(obj);
        }

        if (clazz == BigInteger.class) {
            return (T) castToBigInteger(obj);
        }

        if (clazz == Date.class) {
            return (T) castToDate(obj);
        }

        if (clazz == java.sql.Date.class) {
            return (T) castToSqlDate(obj);
        }

        if (clazz == java.sql.Timestamp.class) {
            return (T) castToTimestamp(obj);
        }

        if (Calendar.class.isAssignableFrom(clazz)) {
            Date date = castToDate(obj);
            Calendar calendar;
            if (clazz == Calendar.class) {
                calendar = Calendar.getInstance(defaultTimeZone, defaultLocale);
            } else {
                try {
                    calendar = (Calendar) clazz.getDeclaredConstructor().newInstance();
                } catch (Exception e) {
                    throw new CommonUtilsException("can not cast to : " + clazz.getName(), e);
                }
            }
            calendar.setTime(date);
            return (T) calendar;
        }

        String className = clazz.getName();
        if (className.equals("javax.xml.datatype.XMLGregorianCalendar")) {
            Date date = castToDate(obj);
            Calendar calendar = Calendar.getInstance(defaultTimeZone, defaultLocale);
            calendar.setTime(date);
            GregorianCalendar gregorianCalendar = new GregorianCalendar();
            XMLGregorianCalendar xmlGregorianCalendar;
            try {
                xmlGregorianCalendar = DatatypeFactory.newInstance().newXMLGregorianCalendar(gregorianCalendar);
                return (T) xmlGregorianCalendar;
            } catch (DatatypeConfigurationException e) {
                throw new CommonUtilsException("can not cast to : " + clazz.getName(), e);
            }
        }

        if (obj instanceof String strVal) {
            if (strVal.length() == 0 //
                    || "null".equals(strVal) //
                    || "NULL".equals(strVal)) {
                return null;
            }

            if (clazz == java.util.Currency.class) {
                return (T) java.util.Currency.getInstance(strVal);
            }

            if (className.startsWith("java.time.")) {
                String json = JSON.toJSONString(strVal);
                return JSON.parseObject(json, clazz);
            }
        }
        throw new CommonUtilsException("can not cast to : " + clazz.getName());
    }

}
