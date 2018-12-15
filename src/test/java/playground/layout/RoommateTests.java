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

import playground.layout.TOComponents.NewUserForm;
import playground.layout.TOComponents.UserTo;
import playground.logic.EntityComponents.UserEntity;
import playground.logic.services.UserService;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment=WebEnvironment.RANDOM_PORT)

public class RoommateTests {
	
	@Autowired
	private UserService users;
	private RestTemplate restTemplate;
	private String url;
	
	@LocalServerPort
	private int port;
	
	private ObjectMapper jacksonMapper;
	
	@PostConstruct
	public void init() {
		this.restTemplate = new RestTemplate();
		this.url = "http://localhost:" + port + "/playground/users";
		
		System.err.println(this.url);
		
		this.jacksonMapper = new ObjectMapper();
	}
	
	@Before
	public void setup () {

	}
	
	@After
	public void teardown() {
		this.users.cleanup();
	}
	
	@Test
	public void testRegisterRoommateSuccessfully() throws Exception {
	//given nothing
	String name  	= "name";
	String email 	= "email";
	String avatar 	= "avatar";
	String role		= "role";
	
	UserEntity rmtE = new UserEntity(name, email, avatar, role);
	UserTo rmtTo = new UserTo(rmtE);

	
	//when POST /playground/roommate with body {"name", "email", "avatar", "role"}
	UserTo responseRoommate = this.restTemplate.postForObject(
			this.url, // url
			rmtTo, // object in the request body
			UserTo.class // expected response body type
				);
	
	//confirm the user after 
	long   roommateCode = responseRoommate.toEntity().getConfirmCode();
	String roommatePlayGround = responseRoommate.toEntity().getUserId().getPlayground();
	String roommateEmail = responseRoommate.toEntity().getUserId().getEmail();
	
	UserEntity confirmedEntity =  this.users.getConfirmUser(roommateEmail, roommatePlayGround, roommateCode);
	
	// then the database contains for the name "name" the following: {"name", "email", "avatar", "role"}
	long expectedRommateCode  =  roommateCode;
	
	UserEntity tempEntity =  this.users.getCustomUser(roommateEmail, roommatePlayGround);
	long actualRoommateCode = tempEntity.getConfirmCode();
		
		assertThat(actualRoommateCode)
			.isNotNull()
			.isNotZero()
			.isEqualByComparingTo(expectedRommateCode);
	}

	
	@Test(expected=Exception.class)
	public void testRegisterAlreadyExistUser() throws Exception {
		//given in database {"name", "email", "avatar", "role"}
	String name  	= "name_1";
	String email 	= "email_1";
	String avatar 	= "avatar_1";
	String role		= "role_1";
	
	UserEntity rmtE = new UserEntity(name, email, avatar, role);
	UserTo rmtTo = new UserTo(rmtE);
	
	this.users.createUser(rmtE);
	
	//when POST /playground/roommate with body {"name", "email", "avatar", "role"}
	UserTo responseActivity = this.restTemplate.postForObject(
			this.url, // url
			rmtTo, // object in the request body
			UserTo.class // expected response body type
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
	
	UserEntity rmtE = new UserEntity(name, email, avatar, role);
	long roommateConfirmationCode = this.users.createUser(rmtE);
	rmtE.setConfirmCode(roommateConfirmationCode);
	
	// set new values in UserTo, to be sent to the Server
	UserTo rmtToPut = new UserTo(rmtE);
	
	rmtToPut.setEmail(newEmail);
	rmtToPut.setRoommateName(newName);
	
	String roommateEmail 			= rmtE.getUserId().getEmail();
	String roommatePlayGround   	= rmtE.getUserId().getPlayground();

	
	//when PUT /playground/roommate with body {"newName", "newEmail"}
	this.restTemplate.put(
			this.url + "/{playground}/{email}",// url
			rmtToPut,// expected response body type
			roommatePlayGround,
			roommateEmail);
	
	
	// then the database contains for the name "name" the following: {"newName", "newEmail", "avatar", "role"}
	String actualRommateName  =  rmtToPut.toEntity().getUserName();
	String actualRommatEmail  =  rmtToPut.toEntity().getUserId().getEmail();
		
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
	
	UserEntity rmtE = new UserEntity(name, email, avatar, role);
	long tempCode =	this.users.createUser(rmtE);
	String roommatePlayground = rmtE.getUserId().getPlayground();
	String roommateEmail 	  = rmtE.getUserId().getEmail();
	
	//create user TO for the HTTP request:
	UserTo rmtTo = new UserTo(rmtE);
	
	//when Get /playground/roommate with body {"newName", "newEmail", "avatar", "role", confirmationCode}
	UserTo responseRoommate = this.restTemplate.getForObject(
			this.url + "/" + "confirm" + "/" + roommatePlayground + "/" + roommateEmail + "/"  + tempCode , 
			UserTo.class,
			tempCode);
	
		
	assertThat(responseRoommate.getCode())
		.isNotNull()
		.isNotZero()
		.isEqualByComparingTo((long) tempCode);	
}

	@Test
	public void testRommateLoginSuccessfully() throws Exception {
		//given nothing
	String name  	= "name";
	String email 	= "email";
	String avatar 	= "avatar";
	String role		= "role";
	
	UserEntity rmtE = new UserEntity(name, email, avatar, role);
	long tempCode =	this.users.createUser(rmtE);
	String roommatePlayground = rmtE.getUserId().getPlayground();
	String roommateEmail 	  = rmtE.getUserId().getEmail();
	UserTo rmtTo = new UserTo(rmtE);
	
	
	rmtE.setConfirmCode(12345);
	
	//when PUT /playground/roommate/ {"newName", "newEmail", "avatar", "role" confirmation code = 12345}
	UserTo responseRoommate = this.restTemplate.getForObject(
			this.url + "/" + "confirm" + "/" + roommatePlayground + "/" + roommateEmail + "/"  + tempCode, 
			UserTo.class,
			email,
			"playground");
	UserEntity ExpectedRommate =  rmtTo.toEntity();
	UserEntity  actualRommate  =  rmtTo.toEntity();
		
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
	
	UserEntity rmtE = new UserEntity(name, email, avatar, role);
	this.users.createUser(rmtE);
	
	String roommatePlayGround = rmtE.getUserId().getPlayground();
	String roommateEmail = rmtE.getUserId().getEmail();
	long roommateConfirmationCode = rmtE.getConfirmCode();
	
	//when PUT /playground/roommate/ with body {"newName", "newEmail", "avatar", "role"}
	UserTo responseRoommate = this.restTemplate.getForObject(
			this.url + "/" + "confirm" + "/" + roommatePlayGround + "/" + roommateEmail + "/" + roommateConfirmationCode, 
			UserTo.class,
			000000);
	//then RoommateNotFound Exception shall be thrown
	}
}

//add test for user that is confirmed before log in and other activity.

