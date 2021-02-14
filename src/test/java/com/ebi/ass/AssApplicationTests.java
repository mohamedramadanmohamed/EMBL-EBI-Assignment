package com.ebi.ass;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import com.ebi.ass.model.Person;
import com.ebi.ass.service.PersonService;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = AssApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@WithMockUser
class AssApplicationTests {

	@LocalServerPort
	private int port;

	@Autowired
	private TestRestTemplate restTemplate;

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private PersonService personService;

	Person mockPerson1 = new Person(1, "john", "Keynes", 29, "red");
	Person mockPerson2 = new Person(2, "mohamed", "ramadan", 30, "white");
	List<Person> mockPersonList = new ArrayList<Person>();

	String mockPerson1Json = "{\"person\":{\"id\":1,\"first_name\":\"john\",\"last_name\":\"Keynes\",\"age\":29,\"favorite_coulour\":\"red\"}}";
	String mockPerson2Json = "{\"person\":{\"id\":2,\"first_name\":\"mohamed\",\"last_name\":\"ramadan\",\"age\":30,\"favorite_coulour\":\"green\"}}";
	private String token= "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJlYml1c2VybmFtZSIsImV4cCI6MTY0NDQxOTA0MywiaWF0IjoxNjEzMzE1MDQzfQ.x7flj9LykfVyEfay9DsSYgUbeP3Zyy2lkamTRxnrT3p-312rkKESUA_4UsMdw9MuQHLBT4fRLtuQxROsWWQXiw";
	
	@PostConstruct
	public void init() {
		mockPersonList.add(mockPerson1);
		mockPersonList.add(mockPerson2);
	}

	private String getRootUrl() {

		return "http://localhost:" + port;
	}

	@Test
	public void getPersonById() throws Exception {
		Mockito.when(personService.getPersonById(Mockito.eq(1))).thenReturn(mockPerson1);
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer "+token);
		HttpEntity<String> entity = new HttpEntity<String>(null, headers);
		ResponseEntity<String> response = restTemplate.exchange(getRootUrl() + "/api/v1/persons/1", HttpMethod.GET,
				entity, String.class);
		String expected = "{person:{id:1,first_name:john,last_name:Keynes,age:29,favorite_coulour:red}}";
		JSONAssert.assertEquals(expected, response.getBody(), false);
	}

	@Test
	public void getAllPersons() throws Exception {
		Mockito.when(personService.getAllPersons()).thenReturn(mockPersonList);
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer "+token);
		HttpEntity<String> entity = new HttpEntity<String>(null, headers);
		ResponseEntity<String> response = restTemplate.exchange(getRootUrl() + "/api/v1/persons", HttpMethod.GET,
				entity, String.class);
		String expected = "{person:[{id:1,first_name:john,last_name:Keynes,age:29,favorite_coulour:red},{id:2,first_name:mohamed,last_name:ramadan,age:30,favorite_coulour:white}]}";
		JSONAssert.assertEquals(expected, response.getBody(), false);
	}

	@Test
	public void createPerson() throws Exception {
		String mockAddPerson = "{\"person\":[{\"first_name\":\"john\",\"last_name\":\"Keynes\",\"age\":29,\"favorite_coulour\":\"red\"}]}";
		Mockito.when(personService.savePerson(mockPersonList)).thenReturn(mockPersonList);

		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer "+token);
		
		RequestBuilder requestBuilder = MockMvcRequestBuilders.post(getRootUrl() + "/api/v1/persons")
				.headers(headers).accept(MediaType.APPLICATION_JSON).content(mockAddPerson).contentType(MediaType.APPLICATION_JSON);
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		assertEquals(201, result.getResponse().getStatus());
	}

	@Test
	public void deletePerson() throws Exception {
		Mockito.when(personService.deletePerson(Mockito.eq(1))).thenReturn(Boolean.FALSE);
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer "+token);
		HttpEntity<String> entity = new HttpEntity<String>(null, headers);
		ResponseEntity<String> response = restTemplate.exchange(getRootUrl() + "/api/v1/persons/1", HttpMethod.DELETE,
				entity, String.class);
		assertEquals(404, response.getStatusCodeValue());
	}
	
	@Test
	public void updatePerson() throws Exception {
		Person expectedMockUpdatedPErson= new Person(1, "john", "Keynes", 29, "red"); 
		String mockUpdatePerson = "{\"person\":[{\"first_name\":\"john\",\"last_name\":\"Keynes\",\"age\":29,\"favorite_coulour\":\"red\"}]}";
		Mockito.when(personService.updatePerson(expectedMockUpdatedPErson)).thenReturn(expectedMockUpdatedPErson);
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer "+token);
		RequestBuilder requestBuilder = MockMvcRequestBuilders.put(getRootUrl() + "/api/v1/persons/1")
				.headers(headers).accept(MediaType.APPLICATION_JSON).content(mockUpdatePerson).contentType(MediaType.APPLICATION_JSON);
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		assertEquals(404, result.getResponse().getStatus());
	}

}
