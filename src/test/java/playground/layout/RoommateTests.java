package playground.layout;

import static org.assertj.core.api.Assertions.assertThat;

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
import playground.logic.RoommateEntity;
import playground.logic.RoommateService;
import playground.layout.RoommateTo;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment=WebEnvironment.RANDOM_PORT)

public class RoommateTests {
	
	@Autowired
	private RoommateService roommates;
	private RestTemplate restTemplate;
	private String url;
	
	@LocalServerPort
	private int port;
	
	private ObjectMapper jacksonMapper;
	
	@PostConstruct
	public void init() {
		this.restTemplate = new RestTemplate();
		this.url = "http://localhost:" + port + "/playground/roommate";
		
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
	public void testRegisterRoommateSuccessfully() throws Exception {
		//given nothing
	String name  	= "name";
	String email 	= "email";
	String avatar 	= "avatar";
	String role		= "role";
	
	RoommateEntity rmtE = new RoommateEntity(name, email, avatar, role);
	RoommateTo rmtTo = new RoommateTo(rmtE);
	
	//when POST /playground/roommate with body {"name", "email", "avatar", "role"}
	RoommateTo responseRoommate = this.restTemplate.postForObject(
			this.url, // url
			rmtTo, // object in the request body
			RoommateTo.class // expected response body type
				);
	
	// then the database contains for the name "name" the following: {"name", "email", "avatar", "role"}
	long expectedRommateCode  =  rmtTo.toEntity().getConfirmCode();
	long actualRoommateCode   =  this.roommates.createRoommate(rmtE);
		
		assertThat(actualRoommateCode)
			.isNotNull()
			.isNotZero()
			.isEqualByComparingTo(expectedRommateCode);
	}

	
	@Test(expected=Exception.class)
	public void testRegisterAlreadyExistUser() throws Exception {
		//given in database {"name", "email", "avatar", "role"}
	String name  	= "name";
	String email 	= "email";
	String avatar 	= "avatar";
	String role		= "role";
	
	RoommateEntity rmtE = new RoommateEntity(name, email, avatar, role);
	this.roommates.createRoommate(rmtE);
	RoommateTo rmtTo = new RoommateTo(rmtE);
	
	//when POST /playground/roommate with body {"name", "email", "avatar", "role"}
	RoommateTo responseActivity = this.restTemplate.postForObject(
			this.url, // url
			rmtTo, // object in the request body
			RoommateTo.class // expected response body type
				);
	// then RoommateAlreadyExist exception is thrown.
	}
	

	@Test
	public void testUpdateRoommateSuccessfully() throws Exception {
		//given nothing
	String name  	= "name";
	String email 	= "email";
	String avatar 	= "avatar";
	String role		= "role";
	String newName  = "newName";
	String newEmail = "newEmail";
	
	RoommateEntity rmtE = new RoommateEntity(name, email, avatar, role);
	this.roommates.createRoommate(rmtE);
	RoommateTo rmtTo = new RoommateTo(rmtE);
	
	//when PUT /playground/roommate with body {"newName", "newEmail"}
	this.restTemplate.put(
			this.url + "{playground}/{email}", // url
			rmtTo, // object in the request body
			newName,
			newEmail// expected response body type
				);
	
	// then the database contains for the name "name" the following: {"newName", "newEmail", "avatar", "role"}
	String actualRommateName  =  rmtTo.toEntity().getRoommateName();
	String actualRommatEmail  =  rmtTo.toEntity().getEmail();
		
	assertThat(actualRommateName)
		.isNotNull()
		.isEqualToIgnoringCase(newName);
	
	assertThat(actualRommatEmail)
	.isNotNull()
	.isEqualToIgnoringCase(newEmail);
}
	

	@Test
	public void testConfirmRoommateSuccessfully() throws Exception {
		//given nothing
	String name  	= "name";
	String email 	= "email";
	String avatar 	= "avatar";
	String role		= "role";
	
	RoommateEntity rmtE = new RoommateEntity(name, email, avatar, role);
	this.roommates.createRoommate(rmtE);
	RoommateTo rmtTo = new RoommateTo(rmtE);
	
	rmtE.setConfirmCode(12345);
	
	//when PUT /playground/roommate with body {"newName", "newEmail", "avatar", "role", confirmationCode = 12345}
	RoommateTo responseRoommate = this.restTemplate.getForObject(
			this.url + "/confirm/{playground}/{email}/{code}", 
			RoommateTo.class,
			12345);
	
		
	assertThat(responseRoommate.getCode())
		.isNotNull()
		.isNotZero()
		.isEqualByComparingTo((long) 12345);	
}

	@Test
	public void testRommateLoginSuccessfully() throws Exception {
		//given nothing
	String name  	= "name";
	String email 	= "email";
	String avatar 	= "avatar";
	String role		= "role";
	
	RoommateEntity rmtE = new RoommateEntity(name, email, avatar, role);
	this.roommates.createRoommate(rmtE);
	RoommateTo rmtTo = new RoommateTo(rmtE);
	
	rmtE.setConfirmCode(12345);
	
	//when PUT /playground/roommate/ {"newName", "newEmail", "avatar", "role" confirmation code = 12345}
	RoommateTo responseRoommate = this.restTemplate.getForObject(
			this.url + "/confirm/{playground}/{email}/{code}", 
			RoommateTo.class,
			email,
			"playground");
	RoommateEntity ExpectedRommate =  rmtTo.toEntity();
	RoommateEntity  actualRommate  =  rmtTo.toEntity();
		
	assertThat(ExpectedRommate)
		.isNotNull()
		.isEqualTo(ExpectedRommate);
	}
	
	@Test
	public void testDidNotConfirmRoommate() throws Exception {
	//given nothing
	String name  	= "name";
	String email 	= "email";
	String avatar 	= "avatar";
	String role		= "role";
	
	RoommateEntity rmtE = new RoommateEntity(name, email, avatar, role);
	this.roommates.createRoommate(rmtE);
	
	//when PUT /playground/roommate/ with body {"newName", "newEmail", "avatar", "role"}
	RoommateTo responseRoommate = this.restTemplate.getForObject(
			this.url + "/login/{playground}/{email}", 
			RoommateTo.class,
			000000);
	//then RoommateNotFound Exception shall be thrown
	}
}

