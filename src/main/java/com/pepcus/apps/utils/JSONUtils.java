package com.pepcus.apps.utils;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.springframework.web.multipart.MultipartFile;

public class JSONUtils {
	
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


}
