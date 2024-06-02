package org.example;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.regions.providers.DefaultAwsRegionProviderChain;

public class App {
    private static final Logger logger = LoggerFactory.getLogger(App.class);

    public static void main(String... args) {
        logger.info("Application starts");
        
        DefaultAwsRegionProviderChain regionProvider = new DefaultAwsRegionProviderChain();
        Region region = regionProvider.getRegion();
        System.out.println("Current AWS Region: " + region.id());
        
        Handler handler = new Handler();
        handler.sendRequest();

        logger.info("Application ends");
    }
}
