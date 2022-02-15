package api.teste.controller;

import java.time.Duration;
import java.time.LocalTime;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter.SseEventBuilder;

import api.teste.service.KafkaPublisherService;
import api.teste.service.TesteService;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/teste")
public class Controller {

	@Autowired
	KafkaPublisherService service;
	
	@Autowired
	TesteService testeService;
	
	@PostMapping
	public void send(@RequestParam String name) {
		service.publish(name);
	}
	
	/*@GetMapping
	public List<String> findAll(){
		return testeService.findAll();
		
	}*/

	//Agrupa a Lista de String de saída em um objeto ServerSentSevent
	/*@GetMapping("/stream-sse")
	public Flux<ServerSentEvent<List<String>>> streamEvents() {
	    return Flux.interval(Duration.ofSeconds(1))
	      .map(sequence -> ServerSentEvent.<List<String>> builder()
	          .data(testeService.findAll())
	          .build());
	}*/
	
	@GetMapping("/stream-sse-mvc")
	public SseEmitter streamSseMvc() {
	    SseEmitter emitter = new SseEmitter();
	    ExecutorService sseMvcExecutor = Executors.newSingleThreadExecutor();
	    sseMvcExecutor.execute(() -> {
	        try {
	            for (int i = 0; true; i++) {
	                SseEventBuilder event = SseEmitter.event()
	                  .data(testeService.findAll())
	                  .id(String.valueOf(i))
	                  .name("sse event - mvc");
	                emitter.send(event);
	            }
	        } catch (Exception ex) {
	            emitter.completeWithError(ex);
	        }
	    });
	    return emitter;
	}
	
	
}
