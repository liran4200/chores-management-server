package playground.layout;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

import playground.layout.TOComponents.ActivityTo;
import playground.logic.EntityComponents.ActivityEntity;
import playground.logic.services.ActivityService;
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment=WebEnvironment.RANDOM_PORT)


public class ActivitiesTests {
	
	@Autowired
	private ActivityService activities;
	private RestTemplate restTemplate;
	private String url;
	
	@LocalServerPort
	private int port;
	
	private ObjectMapper jacksonMapper;
	
	@PostConstruct
	public void init() {
		this.restTemplate = new RestTemplate();
		this.url = "http://localhost:" + port + "/playground/activities";
		
		System.err.println(this.url);
		
		this.jacksonMapper = new ObjectMapper();
	}
	
	@Before
	public void setup () {
		
	}

	@After
	public void teardown() {

	}

	@Test
	public void testInvokeActivitySuccessfully() throws Exception {
		//given nothing
		String type = "type";
		String chorePlayground = "chorePlayground";
		String choreId = "choreId";
		String roommatePlayground = "roommatePlayground";
		String roommateEmail = "roommateEmail";
		Map<String, Object> attributes = new HashMap<>();
		attributes.put("Score", 10);
		
		ActivityEntity act1 = new ActivityEntity(type, chorePlayground, choreId, roommatePlayground, roommateEmail, attributes);
		ActivityTo activityTo = new ActivityTo(act1);
		
		//when POST /playground/activities with body {"type", "chorePlayground", "ChoreId", "roommatePlayground", "roommateEmail"}
		ActivityTo responseActivity = this.restTemplate.postForObject(
				this.url, // url
				activityTo, // object in the request body
				ActivityTo.class // expected response body type
					);
		
		// then the database contains the following: {"type", "chorePlayground", "ChoreId", "roommatePlayground", "roommateEmail"}
		int expectedActivityResult =  (int) activityTo.toEntity().getAttributes().get("Score");
		int actualActivityResult   =  (int) this.activities.invokeActivity(act1);
		
		assertThat(actualActivityResult)
			.isNotNull()
			.isNotZero()
			.isEqualByComparingTo(expectedActivityResult);
	}
	
	@Test(expected=Exception.class)
	public void testInvokeNonExistantActivity() throws Exception {
		//given nothing
		String type = "type";
		String chorePlayground = "chorePlayground";
		String choreId = "choreId";
		String roommatePlayground = "roommatePlayground";
		String roommateEmail = "roommateEmail";
		Map<String, Object> attributes = new HashMap<>();
		attributes.put("Score", 10);
		String ErrorAttributeKey = " ";
		
		ActivityEntity act1 = new ActivityEntity(type, chorePlayground, choreId, roommatePlayground, roommateEmail, attributes);
		ActivityTo activityTo = new ActivityTo(act1);
		
		//when POST /playground/activities with body {"type", "chorePlayground", "ChoreId", "roommatePlayground", "roommateEmail"}
		ActivityTo responseActivity = this.restTemplate.postForObject(
				this.url, // url
				activityTo, // object in the request body
				ActivityTo.class // expected response body type
					);
		// then the database contains the following: {"type", "chorePlayground", "ChoreId", "roommatePlayground", "roommateEmail"}
		int expectedActivityResult =  (int) activityTo.toEntity().getAttributes().get(ErrorAttributeKey);
		int actualActivityResult   =  (int) this.activities.invokeActivity(act1);
		
		assertThat(actualActivityResult)
			.isNotNull()
			.isNotZero()
			.isEqualByComparingTo(expectedActivityResult);		
	}

}
