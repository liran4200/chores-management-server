package playground.layout;

import static org.assertj.core.api.Assertions.assertThat;

import java.text.SimpleDateFormat;
import java.util.Date;
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

import playground.logic.EntityComponents.ElementEntity;
import playground.logic.EntityComponents.ElementUniqueId;
import playground.logic.services.ElementsService;
import playground.layout.TOComponents.ElementTo;
import playground.layout.TOComponents.LocationTo;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment=WebEnvironment.RANDOM_PORT)

public class ElementsTests {
	@Autowired
	private ElementsService elements;
	private RestTemplate restTemplate;
	private String url;
	
	@LocalServerPort
	private int port;
	
	private ObjectMapper jacksonMapper;
	
	@PostConstruct
	public void init() {
		this.restTemplate = new RestTemplate();
		this.url = "http://localhost:" + port + "/playground/elements";
		
		System.err.println(this.url);
		
		this.jacksonMapper = new ObjectMapper();
	}
	
	@Before
	public void setup () {
		
	}

	@After
	public void teardown() {
		this.elements.cleanup();
	}

	
	@Test
	public void testServerInitializesProperly() throws Exception {
		
	}
	
	@Test
	public void testGetSpecificChoreByIDSuccessfully() throws Exception {
		//Given the server is up - do nothing
		
		String name = "Name";
		String type = "Type";
		double x = 1;
		double y = 1;
		Date expirationDate = new Date();
		String userPlayground = "UserPlayground";
		String email = "Email";
		ElementEntity chore = new ElementEntity(name, type, userPlayground,email, x, y, expirationDate);
		// And the database contains a Chore with name: "Name and ID: 
		ElementEntity ExpectedChore = elements.createNewElement(chore, userPlayground, email);
		String ExpectedPlayground = ExpectedChore.getIdAndPlayground().getPlayground();
		String ExpectedID		  = ExpectedChore.getIdAndPlayground().getId();

		
		
		
		// When I GET /Chores/Name and Accept:application/Name
		// invoke HTTP GET /Chores/Name with header: Accept:application/Name
		// and create a new Chore object using jackson
		ElementTo actualChore = this.restTemplate.getForObject(
				this.url + "/" + userPlayground + "/" + email + "/" + ExpectedPlayground + "/" + ExpectedID,
				ElementTo.class
				);
		
		
		/*
			Then the return status is 200
			And the returned value is:
			{
			"Chore":"Name",
			"creationDate":current date and time,
			"location": x = 1, y = 1,
			"moreAttributes":any Name
			}
		 */
		// do nothing about status
		// get chore from Chore and check that it states ""
		assertThat(actualChore)
			.isNotNull()
			.extracting("id")
			.containsExactly(ExpectedID);
	}
	@Test
	public void testShowAtMostFirst5ChoresSuccessfully () throws Exception{
		
		String playground = "playground1";
		String email	  = "email1";
		ElementEntity chore1 = new ElementEntity("name1", "type1", playground, email, 1,1,new Date());
		ElementEntity chore2 = new ElementEntity("name2", "type2", playground, email, 1,1,new Date());
		ElementEntity chore3 = new ElementEntity("name3", "type3", playground, email, 1,1,new Date());
		
		elements.createNewElement(chore1, playground, email);
		elements.createNewElement(chore2, playground, email);
		elements.createNewElement(chore3, playground, email);
		// given the database contains 3 Chores
		
		// when GET /chores 
		ElementTo[] actualChores = this.restTemplate.getForObject(this.url + "/" + playground + "/" + email + "/" + "all", ElementTo[].class);
		
		// then 
		assertThat(actualChores)
			.isNotNull()
			.hasSize(3);
	}
	
	@Test
	public void testShowChoresUsingPaginationSuccessfully() throws Exception{
		// given the database contains 3 chores
		
		String playground = "playground";
		String email      = "email";
		
		ElementEntity chore1 = new ElementEntity("name1", "type1", playground, email, 1,1,new Date());
		ElementEntity chore2 = new ElementEntity("name2", "type2", playground, email, 1,1,new Date());
		ElementEntity chore3 = new ElementEntity("name3", "type3", playground, email, 1,1,new Date());
		
		elements.createNewElement(chore1, playground, email);
		elements.createNewElement(chore2, playground, email);
		elements.createNewElement(chore3, playground, email);
		
		// when GET /Chores 
		ElementTo[] actualChores = this.restTemplate.getForObject(
				this.url + "/" + playground + "/" + email + "/"  + "all" +  "?size={size}&page={page}", 
				ElementTo[].class,
				3, 1);
		
		// then 
		assertThat(actualChores)
			.isNotNull()
			.hasSize(0);
	}

	@Test(expected=Exception.class)
	public void testShowChoresUsingBadPageNumber() throws Exception{
		// No chores are stored in DB
		// when GET /chores 
		ElementTo[] actualChores = this.restTemplate.getForObject(
				this.url + "?page={page}", 
				ElementTo[].class,
				-1);
	}
	
	/*  will be used in the future when user will be validated.
	@Test
	public void testAddNewChoreSuccessfully () throws Exception{
		//given
		ElementEntity chore1 = new ElementEntity("name1", "type1", "playground1", "email1", 1,1,new Date());
		String ID = chore1.getIdAndPlayground().toString();
		String UserPlayground = chore1.getCreatorPlayground();
		ElementTo chore1To = new ElementTo(chore1);
		
		//when POST /chores with body {""name1", "type1", "playground1", "email1", 1,1, the date is the creation date"} 
		ElementTo responseChore = this.restTemplate.postForObject(
				this.url + "/" + "playground1" + "/" + "email1" + "/" + "playground1" + "/" + ID, // url
				chore1To, // object in the request body
				ElementTo.class // expected response body type
					);
		
		// then the database contains for the name: "name1"  the following:	{""name1", "type1", "playground1", "email1", 1,1, the date is the creation date"}
		ElementEntity expectedChore =  chore1To.toEntity();
		ElementEntity actualChoreInDb = this.elements.getElementById("playground1", "email1", "playground1", ID );
		
		assertThat(actualChoreInDb)
			.isNotNull()
			.usingComparator((m1,m2)->{
				int rv  = expectedChore.getName().compareTo(actualChoreInDb.getName());
				if(0 == rv)
					rv  = expectedChore.getExpirationDate().compareTo(actualChoreInDb.getExpirationDate());
				if(0 == rv)
					rv = expectedChore.getIdAndPlayground().getId().compareTo(actualChoreInDb.getIdAndPlayground().getId());
				if(0 == rv)
					rv = expectedChore.getIdAndPlayground().getPlayground().compareTo(actualChoreInDb.getIdAndPlayground().getPlayground());
				return rv;
			})
			.isEqualTo(expectedChore);
	}
	*/
	
	@Test(expected=Exception.class)
	public void testCreateChoreWithExistingKey() throws Exception{
		//given The database already contains a chore with name: "name1"
		ElementEntity chore1 = new ElementEntity("name1", "type1", "playground1", "email1", 1,1,new Date()); 
		this.elements.createNewElement(chore1, "playground", "email");
		
		//when POST /chores with body {"name1", "type1", "playground1", "email1", 1,1 and the chore creation date} 
		ElementTo thePostedChore = new ElementTo(chore1);
		Map<String, Object> moreAttributes = new HashMap<>();
		moreAttributes.put("abc", 123);
		thePostedChore.setAttributes(moreAttributes);
		
		this.restTemplate.postForObject(
				this.url, // url
				chore1, // object in the request body
				ElementTo.class // expected response body type
					);
		// then an exception will be thrown by restTemplate 
	}
	
	@Test
	public void testUpdateChoreSuccessfully () throws Exception {

		// given the database contains {"name1", "type1", "playground1", "email1", 1,1 and the chore creation date} 
		String entityJson = "{\"name\":\"name1\", \"type\":\"type1\", \"creatorPlayground\":\"playground1\","
				+ "\"creatorEmail\":\"email1\",\"x\":1.0, \"y\":1.0}";
		// Jackson unmarshallon
		ElementEntity entity = this.jacksonMapper.readValue(entityJson, ElementEntity.class);
		this.elements.createNewElement(entity, "playground", "email");
		String playground = entity.getIdAndPlayground().getPlayground();
		String ID 		  = entity.getIdAndPlayground().getId(); 
	
		// when I PUT /Chores/name1 with body {"name1", "type1", "playground1", "email1", 1,1 and the chore creation date}
		String toJson = "{\"name\":\"NEWname\", \"type\":\"type1\", \"layground\":\"playground1\","
				+ "\"creatorEmail\":\"email1\", \"Location\":{\"x\":1.0, \"y\":1.0}";
				ElementTo toForPut = this.jacksonMapper.readValue(toJson, ElementTo.class);
		
				
		this.restTemplate.put(
				this.url + "/playground1/email1" + "/" + playground + "/" + ID , // url 
				toForPut, // object to send 
				entity.getName()); // url parameters
		
		// then the database contains for name "name1" {"name1", "type1", "playground1", "email1", 1,1 and the chore creation date}
		ElementEntity actualEntityInDb = this.elements.getElementById("userPlayground", "email1", "playground1", entity.getIdAndPlayground().getId());
		actualEntityInDb.setCreationDate(null);
		
		String expectedJson = this.jacksonMapper.writeValueAsString(
				this.jacksonMapper.readValue(
						"{\"creationDate\":\"null\",\"name\":\" NEWname\", \"type\":\"type1\", \"creatorPlayground\":\"playground1\","
								+ "\"creatorEmail\":\"email1\",\"x\":1.0, \"y\":1.0}",
						ElementEntity.class)
				);
		assertThat(this.jacksonMapper.writeValueAsString(actualEntityInDb))
			.isEqualTo(expectedJson);
	}
	
	@Test(expected=Exception.class)
	public void testUpdateNonExistingChore () throws Exception {
		// given - nothing
		
		// when I PUT /chores/name1 with body {"name1" {"name1", "type1", "playground1", "email1", 1.0,1.0} 
		String toJson = "{\"creationDate\":null,\"name\":\"name1\", \"type\":\"type1\", \"playground\":\"playground1\","
				+ "\"email\":\"email1\",\"location\":{\"x\":1.0, \"y\":1.0}}";
		ElementTo toForPut = this.jacksonMapper.readValue(toJson, ElementTo.class);
		
		this.restTemplate.put(
				this.url + "/playground1/email1/playground1/{id}", // url 
				toForPut, // object to send 
				toForPut.getId()); // url parameters
		
		// then the return status is <> 2xx 
	}
	
	@Test
	public void testShowChoresCreatedAfterACertainDateSuccessfully() throws Exception{
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
		
		String playground = "playground";
		String email      = "email";
		ElementEntity chore1 = new ElementEntity("boolseye", "type1", playground, email, 1,1,new Date());
		ElementEntity chore2 = new ElementEntity("name2", "type2", playground, email, 1,1,new Date());
		ElementEntity chore3 = new ElementEntity("name3", "type3", playground, email, 1,1,new Date());
		
		elements.createNewElement(chore1, playground, email);
		elements.createNewElement(chore2, playground, email);
		elements.createNewElement(chore3, playground, email);
		//Given the database contains {"name":"boolseye", " name ":"name2", " name ":"name3"}] 
		
		// When I Get /chores/playground/email/search/name/boolseye
		ElementTo[] actualMessages = this.restTemplate.getForObject(
					this.url + "/" + playground + "/" + email + "/" + "search" + "/" + "{attributeName}/{value}", 
					ElementTo[].class,
					"name",
					"boolseye");
		
		// Then the response status is 200 and the body is an array of 1 element: with Chore:"boolseye"
		assertThat(actualMessages)
			.isNotNull()
			.hasSize(1)
			.usingElementComparator((c1, c2)->c1.getId().compareTo(c2.getId()))
			.contains(new ElementTo(chore1));
	}
}
