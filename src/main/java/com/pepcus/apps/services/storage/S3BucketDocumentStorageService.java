package com.pepcus.apps.services.storage;

import java.io.File;
import java.io.InputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.jcabi.aspects.Loggable;

/**
 * Service class for document storage on Amazon S3 bucket
 * 
 * @author sbhawsar
 * @since 2018-12-27
 *
 */
@Service
public class S3BucketDocumentStorageService implements DocumentStorageService {
    
    private static final Logger logger = LoggerFactory.getLogger(S3BucketDocumentStorageService.class);

    @Autowired 
    AmazonS3 s3client;

    @Override
    @Loggable(prepend=true, value=Loggable.DEBUG)
    public InputStream downloadDocument(String fileName, String bucketName) {

        try {
            S3Object s3object = s3client.getObject(new GetObjectRequest(bucketName, fileName));
            logger.debug("===================== Downloading Object - Done! =====================");
            return s3object.getObjectContent();
        } catch (AmazonServiceException ase) {
            logger.error("Caught an AmazonServiceException from GET requests, rejected reasons:");
            logger.error("Error Message:    " + ase.getMessage());
            logger.error("HTTP Status Code: " + ase.getStatusCode());
            logger.error("AWS Error Code:   " + ase.getErrorCode());
            logger.error("Error Type:       " + ase.getErrorType());
            logger.error("Request ID:       " + ase.getRequestId());
            throw ase;
        } catch (AmazonClientException ace) {
            logger.error("Caught an AmazonClientException: ");
            logger.error("Error Message: " + ace.getMessage());
            throw ace;
        }
    }
 
    @Override
    @Loggable(prepend=true, value=Loggable.DEBUG)
    public void uploadDocument(File file, String keyName, String bucketName) {

        try {
            s3client.putObject(new PutObjectRequest(bucketName, keyName, file));
            logger.debug("===================== Upload File - Done! =====================");
        } catch (AmazonServiceException ase) {
            logger.error("Caught an AmazonServiceException from PUT requests, rejected reasons:");
            logger.error("Error Message:    " + ase.getMessage());
            logger.error("HTTP Status Code: " + ase.getStatusCode());
            logger.error("AWS Error Code:   " + ase.getErrorCode());
            logger.error("Error Type:       " + ase.getErrorType());
            logger.error("Request ID:       " + ase.getRequestId());
            throw ase;
        } catch (AmazonClientException ace) {
            logger.error("Caught an AmazonClientException: ");
            logger.error("Error Message: " + ace.getMessage());
            throw ace;
        }
    }
    
}
