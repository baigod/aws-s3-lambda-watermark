package me.douboo.aws;

import java.io.ByteArrayInputStream;

import org.apache.commons.lang.exception.ExceptionUtils;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.S3Event;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.event.S3EventNotification.S3EventNotificationRecord;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;

public class S3WaterMark implements RequestHandler<S3Event, String> {
	@Override
	public String handleRequest(S3Event input, Context context) {
		String bucketName; // Source bucket name
		String sourceKeyName; // Source key
		String destinationKeyName; // Destination key

		LambdaLogger logger = context.getLogger();

		input.getRecords().forEach(item -> {
			logger.log(item.getRequestParameters().getSourceIPAddress());
			logger.log(item.getRequestParameters().toString());
		});

		// Get Event Record
		S3EventNotificationRecord record = input.getRecords().get(0);

		// Source Bucket Name
		bucketName = record.getS3().getBucket().getName();

		// Source File Name
		sourceKeyName = record.getS3().getObject().getKey(); // Name doesn't
																// contain any
																// special
																// characters

		// Destination File Name
		String folder = sourceKeyName.substring(0, sourceKeyName.lastIndexOf("/"));
		String filename = sourceKeyName.substring(sourceKeyName.lastIndexOf("/"));
		destinationKeyName = folder + "_vm" + filename;

		logger.log("Input: " + input);
		logger.log("Bucket: " + bucketName + "\n");
		logger.log("Source File:   " + sourceKeyName + "\n");
		logger.log("Target File:   " + destinationKeyName + "\n");

		// Instantiates a client
		AmazonS3 s3 = AmazonS3ClientBuilder.defaultClient();

		try {
			logger.log("WaterMark object...\n");
			// Water Mark
			S3Object object = s3.getObject(new GetObjectRequest(bucketName, sourceKeyName));
			String contentType = object.getObjectMetadata().getContentType();
			logger.log("Content Type:   " + contentType + "\n");
			if (contentType.startsWith("image")) {
				byte[] textWaterMark = WaterMark.textWaterMark(object.getObjectContent(), "TEST");
				// Save Object
				ObjectMetadata oMetadata = object.getObjectMetadata();
				ByteArrayInputStream bais = new ByteArrayInputStream(textWaterMark);
				oMetadata.setContentLength(bais.available());
				s3.putObject(new PutObjectRequest(bucketName, destinationKeyName,
						bais, oMetadata));
			}
		} catch (AmazonServiceException ase) {
			context.getLogger().log("Caught an AmazonServiceException, " + "which means your request made it "
					+ "to Amazon S3, but was rejected with an error " + "response for some reason.\n");
			logger.log("Error Message:    " + ase.getMessage() + "\n");
			logger.log("HTTP Status Code: " + ase.getStatusCode() + "\n");
			logger.log("AWS Error Code:   " + ase.getErrorCode() + "\n");
			logger.log("Error Type:       " + ase.getErrorType() + "\n");
			logger.log("Request ID:       " + ase.getRequestId() + "\n");
		} catch (AmazonClientException ace) {
			logger.log("Caught an AmazonClientException, " + "which means the client encountered "
					+ "an internal error while trying to " + " communicate with S3, "
					+ "such as not being able to access the network." + "\n");
			logger.log("Error Message: " +ExceptionUtils.getFullStackTrace(ace) + "\n");
		}

		s3.shutdown();

		return bucketName + "/" + destinationKeyName;
	}

}
