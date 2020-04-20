package com.pepcus.apps.services.storage;

import java.io.File;
import java.io.InputStream;

/**
 * Interface to define different methods to manage document on storage.
 * 
 * @author Sandeep.Vishwakarma
 *
 */
public interface DocumentStorageService {

  public void uploadDocument(File document, String documentName, String documentLocation);

  public InputStream downloadDocument(String documentName, String documentLocation);

}
