package com.amazonaws.lambda.demo;

import java.nio.ByteBuffer;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.amazonaws.services.kinesis.AmazonKinesis;
import com.amazonaws.services.kinesis.AmazonKinesisClientBuilder;
import com.amazonaws.services.kinesis.model.PutRecordsRequest;
import com.amazonaws.services.kinesis.model.PutRecordsRequestEntry;
import com.amazonaws.services.kinesis.model.PutRecordsResult;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.KinesisEvent;

@RestController
public class LambdaFunctionHandler implements RequestHandler<KinesisEvent, Integer> {

    @Override
    public Integer handleRequest(KinesisEvent event, Context context) 
    {
AmazonKinesisClientBuilder clientBuilder = AmazonKinesisClientBuilder.standard();
        
     
        
        AmazonKinesis kinesisClient = clientBuilder.build();
 
        PutRecordsRequest putRecordsRequest  = new PutRecordsRequest();
        putRecordsRequest.setStreamName("KinesisStreamExample");
        List <PutRecordsRequestEntry> putRecordsRequestEntryList  = new ArrayList<>(); 
        for (int i = 0; i < 1; i++) {
            PutRecordsRequestEntry putRecordsRequestEntry  = new PutRecordsRequestEntry();
            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            Date time = new Date();
            String random = dateFormat.format(time);
			putRecordsRequestEntry.setData(ByteBuffer.wrap(("Updating my data to DynamoDB " +random ).getBytes()));
            putRecordsRequestEntry.setPartitionKey(String.format("partitionKey-%d", i));
            putRecordsRequestEntryList.add(putRecordsRequestEntry); 
        }

        putRecordsRequest.setRecords(putRecordsRequestEntryList);
        PutRecordsResult putRecordsResult  = kinesisClient.putRecords(putRecordsRequest);
        System.out.println("Put Result" + putRecordsResult);
		return null;
    }
    
    @GetMapping(value="/health")
    public String healthApi(){
    	return "Kinesis Input Application is Running";
    }
}
