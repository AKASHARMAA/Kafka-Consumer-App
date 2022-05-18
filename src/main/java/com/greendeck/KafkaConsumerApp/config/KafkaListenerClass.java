package com.greendeck.KafkaConsumerApp.config;

import java.io.InputStream;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.greendeck.KafkaConsumerApp.entity.EndpointInfo;
import com.greendeck.KafkaConsumerApp.repo.EndpointRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JsonParser;
import org.springframework.boot.json.JsonParserFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.PartitionOffset;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.stereotype.Component;


@Component
public class KafkaListenerClass {

	/*
		states will be kept in this property,
		we are using map because it can check if state is correct in O(1) 
	*/
	private Set<Object> allowedStates = new HashSet<>();
	
	// for parsing json
	JsonParser jp = JsonParserFactory.getJsonParser();
	ObjectMapper mapper = new ObjectMapper();

	// repository for saving endpoints
	@Autowired
	EndpointRepository endpointRepository ;


	// static block to get states from filterFile.json
	{
		
		try {
			// getting event_states from filterFile.json
			InputStream fileStream = getClass().getResourceAsStream("/filterFile.json");
			Scanner scanner = new Scanner(fileStream);
			String fileData = scanner.nextLine();
			while(scanner.hasNextLine()){
				fileData += scanner.nextLine();
			}
			scanner.close();
			
			// parsing json and putting states into allowedStates property
			List<String> filterList = (List<String>) jp.parseMap(fileData).get("event_states");
			for(String thisState : filterList) {
				allowedStates.add(thisState);
			}
			
			
		} catch (Exception e) {
			System.out.println("could not read the filter file");
			e.printStackTrace();
		}
		
	}
	
	
    @KafkaListener(topicPartitions = {
            @TopicPartition(topic = "greendeck",
                    partitionOffsets = {
                    			@PartitionOffset(partition = "0-5" , initialOffset = "0")
                    		}
            )})
    void listener(String data){
    	
    	try {
			EndpointInfo endpointInfo = mapper.readValue(data, EndpointInfo.class);
			if(allowedStates.contains(endpointInfo.getEvent_state())) {

				// saving endpointInfo object into db
				endpointRepository.save(endpointInfo);
				
			}
		} catch (JsonMappingException e) {
			// We want to ignore the events that cannot be parsed as Endpointinfo.class
		} catch (JsonProcessingException e) {
			// We want to ignore the events that cannot be parsed as Endpointinfo.class
		}
    	
    	
    }
}
