package org.example;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.regions.providers.DefaultAwsRegionProviderChain;
import java.util.Scanner;

public class App {
    private static final Logger logger = LoggerFactory.getLogger(App.class);

    public static void main(String... args) {
        logger.info("Application starts");
        
        DefaultAwsRegionProviderChain regionProvider = new DefaultAwsRegionProviderChain();
        Region region = regionProvider.getRegion();
        System.out.println("Current AWS Region: " + region.id());
        
        Handler handler = new Handler();
        
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("\nPlease select an option:");
            System.out.println("1. Check bucket exists");
            System.out.println("2. Create bucket");
            System.out.println("3. Upload object");
            System.out.println("4. Get object");
            System.out.println("5. Exit");
            System.out.print("Enter your choice (1-5): ");
            
            int choice = scanner.nextInt();  // Read user's choice as an integer
            switch (choice) {
                case 1: 
                    handler.checkBucketExists();
                    break; 
                case 2: 
                    handler.createBucket();
                    break; 

                case 3: 
                    handler.uploadObject();
                    break; 
                
                case 4: 
                    //handler.getObject();
                    break; 
                
                case 5:
                    System.out.println("Exiting application.");
                    logger.info("Application ends");
                    scanner.close();
                    handler.closeS3Client();
                    return;
                
                default:
                    System.out.println("Invalid choice. Please enter a number between 1-5.");
 
            } // switch     
        //handler.sendRequest();
    } // while
    }
}
