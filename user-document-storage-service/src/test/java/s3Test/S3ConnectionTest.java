package s3Test;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class S3ConnectionTest {

    private static final String ACCESS_KEY = "AKIATBW7IMGHJLBKG6LC";
    private static final String SECRET_KEY = "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";
    private static final String BUCKET_NAME = "file-service-user";
    private static final String REGION = "us-east-1";
    private static final String ENDPOINT_URL = "s3.amazonaws.com";

    @Test
    public void testS3Connection() {
        AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(ACCESS_KEY, SECRET_KEY)))
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(ENDPOINT_URL, REGION))
                .build();

        List<Bucket> buckets = s3Client.listBuckets();
        assertNotNull(buckets, "Bucket list should not be null");

        ObjectListing objectListing = s3Client.listObjects(BUCKET_NAME);
        List<S3ObjectSummary> objectSummaries = objectListing.getObjectSummaries();
        assertNotNull(objectSummaries, "Object list should not be null");


        for (S3ObjectSummary objectSummary : objectSummaries) {
            System.out.println("Object: " + objectSummary.getKey() + " Size: " + objectSummary.getSize());
        }


        assertEquals(5, objectSummaries.size(), "Expected number of objects in the bucket");
    }
}