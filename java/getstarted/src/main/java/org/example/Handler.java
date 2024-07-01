package org.example;


import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
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
import software.amazon.awssdk.services.s3.model.CreateBucketConfiguration;
import software.amazon.awssdk.regions.Region;
import java.util.Scanner;
import java.io.OutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
//import software.amazon.awssdk.services.s3.model.ResponseHeaderOverrides;
//import software.amazon.awssdk.services.s3.model.GetObjectRequest;


public class Handler {
    private final S3Client s3Client;

    public Handler() {
        s3Client = DependencyFactory.s3Client();
    }
    public void closeS3Client() {
        if (s3Client != null) {
        System.out.println("Closing the connection to {S3}");
        s3Client.close();
        }
    }

    public void checkBucketExists() { //S3Client s3Client, String bucketName
        Scanner scanner = new Scanner(System.in);  // Create a Scanner object
        System.out.println("Enter bucket name:");
        String bucketName = scanner.nextLine();  // Read user input

        System.out.println("Checking if bucket exists. Bucket Name: " + bucketName);
        System.out.printf("%n");
        try {
            HeadBucketRequest request=HeadBucketRequest.builder()
                .bucket(bucketName)
                .build();
            HeadBucketResponse result=s3Client.headBucket(request);
            if (result.sdkHttpResponse().statusCode() == 200) { System.out.println("Bucket exists and is owned by this account."); }
            
        } catch (S3Exception e) {
            switch (e.statusCode()) {
                case 404:
                    System.out.println("Error Code 404. No such bucket exists.");
                    System.out.printf("%n");
                    break;
                case 403:
                    System.out.println("Error Code 403. Permission errors in accessing bucket. Likely bucket owned by another account.");
                    System.out.printf("%n");
                    break;
                case 400:
                case 301:
                    System.out.println("Error Code 400/301. Bucket exists; not in this region OR bad request");
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

    public String createBucket() { //S3Client s3Client, String bucketName
        
        Region region = Region.US_EAST_2;
        String bucketName = "bucketwithjava" + System.currentTimeMillis();
        System.out.println("Creating new bucket with Name: " + bucketName + " in region " + region);

        
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

    public String uploadObject() { //S3Client s3Client, String bucketName
    Scanner scanner = new Scanner(System.in);  // Create a Scanner object
    System.out.println("Enter bucket name:");
    String bucketName = scanner.nextLine();  // Read user input
    scanner.close();
    
    
    
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


    public void getObject() { //S3Client s3Client, String bucketName, String key
        System.out.println("Inside getObject");
        Scanner scanner = new Scanner(System.in);  // Create a Scanner object
        System.out.println("Enter bucket name:");
        String bucketName = scanner.nextLine();  // Read user input
        System.out.println("Enter object key:");
        String keyName = scanner.nextLine();  // Read user input
        //scanner.close();
        
        String path = "/home/ec2-user/environment/aai_demos/java/getstarted/tempfiles/airport-codes.csv";
        
        getObjectBytes(s3Client, bucketName, keyName, path);
        
    }

    public static void getObjectBytes(S3Client s3, String bucketName, String keyName, String path) {
        try {
            GetObjectRequest objectRequest = GetObjectRequest
                    .builder()
                    .key(keyName)
                    .bucket(bucketName)
                    .build();

            ResponseBytes<GetObjectResponse> objectBytes = s3.getObjectAsBytes(objectRequest);
            byte[] data = objectBytes.asByteArray();

            // Write the data to a local file.
            File myFile = new File(path);
            OutputStream os = new FileOutputStream(myFile);
            os.write(data);
            System.out.println("Successfully obtained bytes from an S3 object");
            os.close();

        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (S3Exception e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
    }

/*  public void cleanUp() { //S3Client s3Client, String bucketName, String keyName
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
    
*/  
    

}