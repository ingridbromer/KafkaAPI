package api.teste.service;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Flux;

@Service
public class TesteService {
	
	private static final Logger LOG=LoggerFactory.getLogger(TesteService.class);

	private List<String> lista = new ArrayList<String>();
		
	public void addName(String name) {
		lista.add(name);
	}
	
	public List<String> findAll(){
		return lista;
	}
	
	//WebClient é uma interface que representa o principal ponto de entrada para realizar solicitações da web.
	//Consumo de eventos em 'localhost:8080/sse-server'
	public void consumeServerSentEvent() {
	    WebClient client = WebClient.create("http://localhost:8080/teste/sse-server");
	    ParameterizedTypeReference<ServerSentEvent<List<String>>>type
	     = new ParameterizedTypeReference<ServerSentEvent<List<String>>>() {};

	    Flux<ServerSentEvent<List<String>>> eventStream = client.get()
	      .uri("/stream-sse-mvc")
	      .retrieve()
	      .bodyToFlux(type);

	    eventStream.subscribe(
	      content -> LOG.info("Time: {} - event: name[{}], id [{}], content[{}] ",
	        LocalTime.now(), content.event(), content.id(), content.data()),
	      error -> LOG.error("Error receiving SSE: {}", error),
	      () -> LOG.info("Completed!!!"));
	}
}
