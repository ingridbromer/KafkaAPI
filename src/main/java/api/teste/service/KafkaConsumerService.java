package api.teste.service;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumerService {
	
	@Autowired
	TesteService testeService;
	
	@KafkaListener(topics = "company", groupId = "group_id", containerFactory = "kafkaListenerContainerFactory")
	public void consumer(String message) throws IOException, JSONException {
		System.out.println(message);
		
		testeService.addName(message);
	}
}
