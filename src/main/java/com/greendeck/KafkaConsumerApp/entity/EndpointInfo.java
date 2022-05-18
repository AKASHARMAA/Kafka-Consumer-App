package com.greendeck.KafkaConsumerApp.entity;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * 
 * All the endpoints will be converted as this class and saved into the database
 * 
 */

@Entity
public class EndpointInfo {

	@Id
	String _id;
	String endpoint;
	String event_state;
	String status_code;
	String response_message;
	String time_taken;
	String timestamp;
	
	public String get_id() {
		return _id;
	}
	public void set_id(String _id) {
		this._id = _id;
	}
	public String getEndpoint() {
		return endpoint;
	}
	public void setEndpoint(String endpoint) {
		this.endpoint = endpoint;
	}
	public String getEvent_state() {
		return event_state;
	}
	public void setEvent_state(String event_state) {
		this.event_state = event_state;
	}
	public String getStatus_code() {
		return status_code;
	}
	public void setStatus_code(String status_code) {
		this.status_code = status_code;
	}
	public String getResponse_message() {
		return response_message;
	}
	public void setResponse_message(String response_message) {
		this.response_message = response_message;
	}
	public String getTime_taken() {
		return time_taken;
	}
	public void setTime_taken(String time_taken) {
		this.time_taken = time_taken;
	}
	public String getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
	
	
	@Override
	public String toString() {
		return "EndpointInfo [_id=" + _id + ", endpoint=" + endpoint + ", event_state=" + event_state + ", status_code="
				+ status_code + ", response_message=" + response_message + ", time_taken=" + time_taken + ", timestamp="
				+ timestamp + "]";
	}
	
	
}
