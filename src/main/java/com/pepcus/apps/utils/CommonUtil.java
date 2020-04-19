package com.pepcus.apps.utils;

import java.io.File;
import java.io.IOException;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.pepcus.apps.constant.ApplicationConstants;
import com.pepcus.apps.services.crypto.BCryptPasswordEncryptor;

/**
 * To keep some common util methods 
 * 
 */
public class CommonUtil {

    /**
     * This will return current date and time in UTC
     * 
     * @return
     */
    public static String getTodayInUTC() {
        DateTimeFormatter format = DateTimeFormatter.ofPattern(ApplicationConstants.VALID_FORMAT_YYYY_MM_DD_HH_MM_SS);
        ZonedDateTime utcDateTime = ZonedDateTime.now(ZoneOffset.UTC);
        return format.format(utcDateTime);
    }

    /**
     * Get tempID column value for Company entity.
     * 
     * @return
     */
    public static String getTempId() {
        Calendar cal = Calendar.getInstance();
        long currentTime = cal.getTime().getTime();
        return String.valueOf(currentTime);
    }

    /**
     * 
     * @return
     */
    public static Long getNowInMiliseconds() {
        Date now = new Date();
        return now.getTime();
    }
    
    /**
     * 
     * @return
     */
    public static Integer getNowInMiliseconds_Integer() {
        return (int) (new Date().getTime()/1000L);
    }
    
    /**
     * 
     * @return
     */
    public static Integer getDateInMilliseconds(Date date) {
        return (int) (date.getTime()/1000L);
    }
    
    /**
     * Generates and returns a hash.
     * 
     * @param value
     * @return
     */
    public static String generateHashedValue(Integer value) {
        long microTime = System.currentTimeMillis();
        String companyIdWithMicroTime = String.valueOf(microTime + value).replace(" ", "");
        String encodedString = Base64.getEncoder().encodeToString(companyIdWithMicroTime.getBytes());
        String reversedString = StringUtils.reverse(encodedString.replace("=", ""));
        return reversedString.toUpperCase();
    }

    /**
     * This will return current date in UTC
     * TODO: Need to implement so that returned date instance is for UTC time
     * @return
     */
    public static Date getCurrentDateInUTC() {
        Date dateInUTC = new Date();
        return dateInUTC;
    }

    /**
     * This function returns the first object which is instance of class cls from array of objects objs.
     * If no objects from objs is instance of cls then returns null.
     * 
     * @param cls
     * @param objs
     * @return
     */
    public static Object getObjectForClass(Class<?> cls, Object... objs) {
        for (Object o : objs) {
            if (cls.isInstance(o)) {
                return o;
            }
        }
        return null;
    }
    
    /**
     * Generate Custom sized Alpha numeric hex string
     * 
     * @param size
     * @return
     */
    public static String generateRandomAlphanumericHexString(Integer size) {
        Random r = new Random();
        StringBuffer sb = new StringBuffer();
        while(sb.length() < size){
            sb.append(Integer.toHexString(r.nextInt()));
        }

        return sb.toString().substring(0, size);
    }
    
    /**
     * Get File Object from MultipartFile Object
     * 
     * @param fileToUpload
     * @return
     * @throws IOException
     */
    public static File getFileFromMultipartObject(MultipartFile fileToUpload) throws IOException {
        File file = new File(fileToUpload.getOriginalFilename());
        FileUtils.copyInputStreamToFile(fileToUpload.getInputStream(), file);
        return file;
    }

    public static void main(String args[]) {
        System.out.println(new BCryptPasswordEncryptor().encrypt("123456"));
    }
}
