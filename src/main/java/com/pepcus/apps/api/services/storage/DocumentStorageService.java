package com.pepcus.apps.api.services.storage;

import java.io.File;
import java.io.InputStream;

/**
 * Interface to define different methods to manage document on storage.  
 * 
 */
public interface DocumentStorageService {

    public void uploadDocument(File document, String documentName, String documentLocation);
    
    public InputStream downloadDocument(String documentName, String documentLocation);

}
