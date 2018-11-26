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
import playground.logic.ChoresService;
import playground.logic.ChoreEntity;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment=WebEnvironment.RANDOM_PORT)

public class ChoresTests {
	@Autowired
	private ChoresService chores;
	private RestTemplate restTemplate;
	private String url;
	
	@LocalServerPort
	private int port;
	
	private ObjectMapper jacksonMapper;
	
	@PostConstruct
	public void init() {
		this.restTemplate = new RestTemplate();
		this.url = "http://localhost:" + port + "/playground/chores";
		
		System.err.println(this.url);
		
		this.jacksonMapper = new ObjectMapper();
	}
	
	@Before
	public void setup () {
		
	}

	@After
	public void teardown() {
		this.chores.cleanup();
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
		String playground = "Playground";
		String ID	= "ID";
		String userPlayground = "userPlayGround";
		String email = "Email";
		ChoreEntity chore = new ChoreEntity(name, type, playground,email, x, y, expirationDate);
		// And the database contains a Chore with name: "Name and ID: "ID""
		chores.createNewChore(chore, userPlayground, email);
		
		// When I GET /Chores/Name and Accept:application/Name
		// invoke HTTP GET /Chores/Name with header: Accept:application/Name
		// and create a new Chore object using jackson
		ChoreTo actualChore = this.restTemplate.getForObject(this.url + "/{userPlayground}/{email}/{playground}/{id}"
				, ChoreTo.class, ID);
		
		
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
			.extracting("Chore")
			.containsExactly(ID);
	}
	@Test
	public void testShowAtMostFirst5ChoresSuccessfully () throws Exception{
		
		ChoreEntity chore1 = new ChoreEntity("name1", "type1", "playground1", "email1", 1,1,new Date());
		ChoreEntity chore2 = new ChoreEntity("name2", "type2", "playground2", "email2", 1,1,new Date());
		ChoreEntity chore3 = new ChoreEntity("name3", "type3", "playground3", "email3", 1,1,new Date());
		
		chores.createNewChore(chore1, "playground1", "email1");
		chores.createNewChore(chore2, "playground2", "email2");
		chores.createNewChore(chore3, "playground3", "email3");
		// given the database contains 3 Chores
		
		// when GET /chores 
		ChoreTo[] actualChores = this.restTemplate.getForObject(this.url + "/{userPlayground}/{email}/all", ChoreTo[].class);
		
		// then 
		assertThat(actualChores)
			.isNotNull()
			.hasSize(3);
	}
	
	@Test
	public void testShowChoresUsingPaginationSuccessfully() throws Exception{
		// given the database contains 3 chores
		ChoreEntity chore1 = new ChoreEntity("name1", "type1", "playground1", "email1", 1,1,new Date());
		ChoreEntity chore2 = new ChoreEntity("name2", "type2", "playground2", "email2", 1,1,new Date());
		ChoreEntity chore3 = new ChoreEntity("name3", "type3", "playground3", "email3", 1,1,new Date());
		
		chores.createNewChore(chore1, "playground1", "email1");
		chores.createNewChore(chore2, "playground2", "email2");
		chores.createNewChore(chore3, "playground3", "email3");
		
		// when GET /Chores 
		ChoreTo[] actualChores = this.restTemplate.getForObject(
				this.url + "?size={size}&page={page}", 
				ChoreTo[].class,
				5, 1);
		
		// then 
		assertThat(actualChores)
			.isNotNull()
			.hasSize(0);
	}

	@Test(expected=Exception.class)
	public void testShowChoresUsingBadPageNumber() throws Exception{
		// No chores are stored in DB
		// when GET /chores 
		ChoreTo[] actualChores = this.restTemplate.getForObject(
				this.url + "?page={page}", 
				ChoreTo[].class,
				-1);
	}
	
	@Test
	public void testAddNewChoreSuccessfully () throws Exception{
		//given
		ChoreEntity chore1 = new ChoreEntity("name1", "type1", "playground1", "email1", 1,1,new Date());
		String ID = chore1.getId();
		ChoreTo chore1To = new ChoreTo(chore1);
		
		//when POST /chores with body {""name1", "type1", "playground1", "email1", 1,1, the date is the creation date"} 
		ChoreTo responseChore = this.restTemplate.postForObject(
				this.url + "/{userPlayground}/{email}", // url
				chore1To, // object in the request body
				ChoreTo.class // expected response body type
					);
		
		// then the database contains for the name: "name1"  the following:	{""name1", "type1", "playground1", "email1", 1,1, the date is the creation date"}
		ChoreEntity expectedChore =  chore1To.toEntity();
		ChoreEntity actualChoreInDb = this.chores.getChoreById("playground1", "email1", "playground1", ID );
		
		assertThat(actualChoreInDb)
			.isNotNull()
			.usingComparator((m1,m2)->{
				int rv  = expectedChore.getName().compareTo(actualChoreInDb.getName());
				if(0 == rv)
					rv  = expectedChore.getExpirationDate().compareTo(actualChoreInDb.getExpirationDate());
				if(0 == rv)
					rv = expectedChore.getId().compareTo(actualChoreInDb.getId());
				return rv;
			})
			.isEqualTo(expectedChore);
	}
	
	@Test(expected=Exception.class)
	public void testCreateChoreWithExistingKey() throws Exception{
		//given The database already contains a chore with name: "name1"
		ChoreEntity chore1 = new ChoreEntity("name1", "type1", "playground1", "email1", 1,1,new Date()); 
		this.chores.createNewChore(chore1, "playground", "email");
		
		//when POST /chores with body {"name1", "type1", "playground1", "email1", 1,1 and the chore creation date} 
		ChoreTo thePostedChore = new ChoreTo(chore1);
		Map<String, Object> moreAttributes = new HashMap<>();
		moreAttributes.put("abc", 123);
		thePostedChore.setAttributes(moreAttributes);
		
		this.restTemplate.postForObject(
				this.url, // url
				chore1, // object in the request body
				ChoreTo.class // expected response body type
					);
		// then an exception will be thrown by restTemplate 
	}
	
	@Test
	public void testUpdateChoreSuccessfully () throws Exception {
		// given the database contains {"name1", "type1", "playground1", "email1", 1,1 and the chore creation date} 
		String entityJson = "{\"name\":\"name1\", \"type\":\"type1\", \"playground\":\"playground1\","
				+ "\"email\":\"email1\",\"location\":{\"x\":1.0, \"y\":1.0}}";
		// Jackson unmarshallon
		ChoreEntity entity = this.jacksonMapper.readValue(entityJson, ChoreEntity.class);
		this.chores.createNewChore(entity, "playground", "email");
		
		// when I PUT /Chores/name1 with body {"name1", "type1", "playground1", "email1", 1,1 and the chore creation date}
		String toJson = "{\"name\":\"name1\", \"type\":\"type1\", \"playground\":\"playground1\","
				+ "\"email\":\"email1\",\"location\":{\"x\":1.0, \"y\":1.0}}";
				ChoreTo toForPut = this.jacksonMapper.readValue(toJson, ChoreTo.class);
		
		this.restTemplate.put(
				this.url + "/{userPlayground}/{email}/{playground}/{id}", // url 
				toForPut, // object to send 
				entity.getName()); // url parameters
		
		// then the database contains for name "name1" {"name1", "type1", "playground1", "email1", 1,1 and the chore creation date}
		ChoreEntity actualEntityInDb = this.chores.getChoreById("userPlayground", "email1", "playground1", entity.getId());
		actualEntityInDb.setCreationDate(null);
		
		String expectedJson = this.jacksonMapper.writeValueAsString(
				this.jacksonMapper.readValue(
						"{\"creationDate\":null,\"name\":\"name1\", \"type\":\"type1\", \"playground\":\"playground1\","
								+ "\"email\":\"email1\",\"location\":{\"x\":1.0, \"y\":1.0}}",
						ChoreEntity.class)
				);
		assertThat(this.jacksonMapper.writeValueAsString(actualEntityInDb))
			.isEqualTo(expectedJson);
	}
	
	@Test(expected=Exception.class)
	public void testUpdateNonExistingChore () throws Exception {
		String name = "name1";
		// given - nothing
		
		// when I PUT /chores/name1 with body {"name1" {"name1", "type1", "playground1", "email1", 1.0,1.0} 
		String toJson = "{\"creationDate\":null,\"name\":\"name1\", \"type\":\"type1\", \"playground\":\"playground1\","
				+ "\"email\":\"email1\",\"location\":{\"x\":1.0, \"y\":1.0}}";
		ChoreTo toForPut = this.jacksonMapper.readValue(toJson, ChoreTo.class);
		
		this.restTemplate.put(
				this.url + "/{userPlayground}/{email}/{playground}/{id}", // url 
				toForPut, // object to send 
				name); // url parameters
		
		// then the return status is <> 2xx 
	}
	
	@Test
	public void testShowChoresCreatedAfterACertainDateSuccessfully() throws Exception{
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
		
		ChoreEntity chore1 = new ChoreEntity("name1", "type1", "playground1", "email1", 1,1,new Date());
		ChoreEntity chore2 = new ChoreEntity("name2", "type2", "playground2", "email2", 1,1,new Date());
		ChoreEntity chore3 = new ChoreEntity("name3", "type3", "playground3", "email3", 1,1,new Date());
		
		chores.createNewChore(chore1, "playground1", "email1");
		chores.createNewChore(chore2, "playground2", "email2");
		chores.createNewChore(chore3, "playground3", "email3");
		// given the database contains 3 Chores
		
		chore1.setCreationDate(formatter.parse("31-12-2018"));
		chore2.setCreationDate(formatter.parse("01-01-2018"));
		chore3.setCreationDate(formatter.parse("01-01-2018"));
		//Given the database contains [{"name":"name1", " name ":"name2", " name ":"name3"}] 
		//Where name1 and name2 creation date is before 2018-11-19 and name3 creation date is after 2018-11-19
		// When I Get /chores/searchByCreationDate/01-01-2018
		ChoreTo[] actualMessages 
			= this.restTemplate.getForObject(
					this.url + "/searchByCreationDate/{date}", 
					ChoreTo[].class, 
					"2018-11-19");
		
		// Then the response status is 200 and the body is an array of 1 element: with Chore:"name1"
		assertThat(actualMessages)
			.isNotNull()
			.hasSize(1)
			.usingElementComparator((c1, c2)->c1.getId().compareTo(c2.getId()))
			.contains(new ChoreTo(chore1));
	}
}
