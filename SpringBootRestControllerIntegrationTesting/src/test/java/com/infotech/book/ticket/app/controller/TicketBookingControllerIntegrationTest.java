package com.infotech.book.ticket.app.controller;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.infotech.book.ticket.app.TicketBookingManagementApplication;
import com.infotech.book.ticket.app.entities.Ticket;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TicketBookingManagementApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TicketBookingControllerIntegrationTest {

	@LocalServerPort
	private int port;

	@Autowired
	private TestRestTemplate testRestTemplate;
	private HttpHeaders headers = new HttpHeaders();
	
	@Test
	public void testCreateTicket() throws Exception {
		Ticket ticket = new Ticket();
		ticket.setTicketId(1);
		ticket.setPassengerName("Martin Bingel");
		ticket.setSourceStation("Kolkata");
		ticket.setDestStation("Delhi");
		ticket.setBookingDate(new Date());
		ticket.setEmail("martin.s2017@gmail.com");
		
		String URIToCreateTicket = "/api/tickets/create";
		
	    String inputInJson = this.mapToJson(ticket);
		
		HttpEntity<Ticket> entity = new HttpEntity<Ticket>(ticket, headers);
		ResponseEntity<String> response = testRestTemplate.exchange(
				formFullURLWithPort(URIToCreateTicket),
				HttpMethod.POST, entity, String.class);
		
		String responseInJson = response.getBody();
		assertThat(responseInJson).isEqualTo(inputInJson);
	}

	@Test
	public void testGetTicketById() throws Exception {
		
		Ticket ticket = new Ticket();
		ticket.setTicketId(2);
		ticket.setPassengerName("Sean Murphy");
		ticket.setSourceStation("Kolkata");
		ticket.setDestStation("Mumbai");
		ticket.setBookingDate(new Date());
		ticket.setEmail("sean.s2017@gmail.com");
		
		String inputInJson = this.mapToJson(ticket);
		
		String URIToCreateTicket = "/api/tickets/create";
		HttpEntity<Ticket> entity = new HttpEntity<Ticket>(ticket, headers);
		testRestTemplate.exchange(formFullURLWithPort(URIToCreateTicket),
				HttpMethod.POST, entity, String.class);
	
		String URI = "/api/tickets/ticketId/2";

	    String bodyJsonResponse = testRestTemplate.getForObject(formFullURLWithPort(URI), String.class);
		assertThat(bodyJsonResponse).isEqualTo(inputInJson);
	}

	@Test
	public void testGetTicketByEmail() throws Exception {
		
		Ticket ticket = new Ticket();
		ticket.setTicketId(3);
		ticket.setPassengerName("Marry Johnson");
		ticket.setSourceStation("Delhi");
		ticket.setDestStation("Mumbai");
		ticket.setBookingDate(new Date());
		ticket.setEmail("marrry.j2017@gmail.com");
		
		String URI = "/api/tickets/create";
		
	    String inputInJson = this.mapToJson(ticket);
		
		HttpEntity<Ticket> entity = new HttpEntity<Ticket>(ticket, headers);
		testRestTemplate.exchange(
				formFullURLWithPort(URI),
				HttpMethod.POST, entity, String.class);
		
		String URIToGetTicket = "/api/tickets/email/marrry.j2017@gmail.com";
		String bodyJsonResponse = testRestTemplate.getForObject(formFullURLWithPort(URIToGetTicket), String.class);
		assertThat(bodyJsonResponse).isEqualTo(inputInJson);
		
	}

	/**
	 * this utility method Maps an Object into a JSON String. Uses a Jackson ObjectMapper.
	 */
	private String mapToJson(Object object) throws JsonProcessingException {
		ObjectMapper objectMapper = new ObjectMapper();
		return objectMapper.writeValueAsString(object);
	}

    /**
     * This utility method to create the url for given uri. It appends the RANDOM_PORT generated port
     */
	private String formFullURLWithPort(String uri) {
		return "http://localhost:" + port + uri;
	}
}
