package org.example;

import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.CreateBucketRequest;
import software.amazon.awssdk.services.s3.model.DeleteBucketRequest;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.HeadBucketRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;
import software.amazon.awssdk.services.s3.model.HeadBucketRequest;
import software.amazon.awssdk.services.s3.model.HeadBucketResponse;

public class Handler {
    private final S3Client s3Client;

    public Handler() {
        s3Client = DependencyFactory.s3Client();
    }

    public void sendRequest() {
        //Check if bucket exists 
        checkBucketExists (s3Client, "amazon" );
        
        //create bucket using the s3Client
        //bucketName = createBucket(s3Client);
        
        //Upload object to the newly created bucket 
        //key = uploadObject(s3Client, bucketName);
        
        //Delete bucket and clean-up 
        //cleanUp(s3Client, bucketName, key);
        
        

        System.out.println("Closing the connection to {S3}");
        s3Client.close();
        System.out.println("Connection closed");
        System.out.println("Exiting...");
    }

    public static String createBucket(S3Client s3Client) {
        String bucketName = "bucketwithjava" + System.currentTimeMillis();
        try {
            s3Client.createBucket(CreateBucketRequest
                    .builder()
                    .bucket(bucketName)
                    .build());
            System.out.println("Creating bucket: " + bucketName);
            s3Client.waiter().waitUntilBucketExists(HeadBucketRequest.builder()
                    .bucket(bucketName)
                    .build());
            System.out.println(bucketName + " is ready.");
            System.out.printf("%n");
            return bucketName;
        } catch (S3Exception e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
            return null;
        }
    }

    public static String uploadObject(S3Client s3Client, String bucketName) {
    System.out.println("Uploading Object..");
    System.out.printf("%n");
    String key = "testobject";
    s3Client.putObject(PutObjectRequest.builder().bucket(bucketName).key(key)
                    .build(),
                    RequestBody.fromString("Testing with the {sdk-java}"));

    System.out.println("Upload complete");
    System.out.printf("%n");
    return key;
    } 

    public static void cleanUp(S3Client s3Client, String bucketName, String keyName) {
        System.out.println("Cleaning up...");
        try {
            System.out.println("Deleting object: " + keyName);
            DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder().bucket(bucketName).key(keyName).build();
            s3Client.deleteObject(deleteObjectRequest);
            System.out.println(keyName + " has been deleted.");
            System.out.println("Deleting bucket: " + bucketName);
            DeleteBucketRequest deleteBucketRequest = DeleteBucketRequest.builder().bucket(bucketName).build();
            s3Client.deleteBucket(deleteBucketRequest);
            System.out.println(bucketName + " has been deleted.");
            System.out.printf("%n");
        } catch (S3Exception e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
        System.out.println("Cleanup complete");
        System.out.printf("%n");
    }
    
    
    public static void checkBucketExists(S3Client s3Client, String bucketName) {
        System.out.println("Checking if bucket exists. Bucket Name: " + bucketName);
        System.out.printf("%n");
        try {
            HeadBucketRequest request=HeadBucketRequest.builder()
                .bucket(bucketName)
                .build();
            HeadBucketResponse result=s3Client.headBucket(request);
            if (result.sdkHttpResponse().statusCode() == 200) { System.out.println("Bucket existing!"); }
            
        } catch (S3Exception e) {
            switch (e.statusCode()) {
                case 404:
                    System.out.println("Error Code 404. No such bucket existing.");
                    System.out.printf("%n");
                    break;
                case 400:
                    System.out.println("Error Code 400. Attempted to access a bucket from a Region other than where it exists.");
                    System.out.printf("%n");
                    break;
                case 403:
                    System.out.println("Error Code 403. Permission errors in accessing bucket...");
                    System.out.printf("%n");
                    break;
                default: 
                    System.err.println(e.awsErrorDetails().errorMessage());
                    System.out.printf("%n");
                    break;
        }
        System.out.println("Check bucket complete ");
        System.out.printf("%n");
    }
    }
}