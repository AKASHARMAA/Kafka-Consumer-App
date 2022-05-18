package com.greendeck.KafkaConsumerApp.repo;

import com.greendeck.KafkaConsumerApp.entity.EndpointInfo;

import org.springframework.data.repository.CrudRepository;

/**
 * InnerEndpointRepository
 */
public interface EndpointRepository extends CrudRepository<EndpointInfo, String> {

}