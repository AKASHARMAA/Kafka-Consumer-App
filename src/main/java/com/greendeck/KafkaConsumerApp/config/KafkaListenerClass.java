package com.greendeck.KafkaConsumerApp.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
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

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JsonParser;
import org.springframework.boot.json.JsonParserFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.PartitionOffset;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.stereotype.Component;


@Component
public class KafkaListenerClass {
	private Set<Object> allowedStates = new HashSet<>();
	
	JsonParser jp = JsonParserFactory.getJsonParser();
	ObjectMapper mapper = new ObjectMapper();

	@Autowired
	EndpointRepository endpointRepository ;
	{
		
		try {
			// getting event_states that are allowed from filterFile.json and putting them in a java.util.Set for use
			InputStream fileStream = getClass().getResourceAsStream("/filterFile.json");
			Scanner scanner = new Scanner(fileStream);
			String fileData = scanner.nextLine();
			while(scanner.hasNextLine()){
				fileData += scanner.nextLine();
			}
			scanner.close();
			System.out.println("====================================================================================");
			System.out.println(fileData);
			List<String> filterList = (List<String>) jp.parseMap(fileData).get("event_states");
			for(String thisState : filterList) {
				allowedStates.add(thisState);
			}
			
			
		} catch (Exception e) {
			System.out.println("could not read the filter file");
			e.printStackTrace();
		}
		
	}
	
	public NewTopic topic() {
		return TopicBuilder.name("greendeck").build();
	}
    
	private int counter = 0 ;
	
	
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
				counter ++;
				System.out.println(counter);
				endpointRepository.save(endpointInfo);
				System.out.println(endpointInfo);
				
			}
		} catch (JsonMappingException e) {
			// don't do anything
		} catch (JsonProcessingException e) {
			// don't do anything
		}
    	
    	
    }
}
