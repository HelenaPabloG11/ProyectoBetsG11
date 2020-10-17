package junit;

import static org.junit.jupiter.api.Assertions.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import businessLogic.BLFacadeImplementation;
import configuration.ConfigXML;
import dataAccess.DataAccess;
import domain.Event;
import domain.RegularUser;
import domain.User;
import exceptions.EventFinished;
import exceptions.IncorrectPassException;
import exceptions.QuestionAlreadyExist;
import exceptions.UserAlreadyExistsException;
import exceptions.UserDoesNotExistException;
import test.businessLogic.TestFacadeImplementation;

class LoginTest {
	// sut- System Under Test
	DataAccess dbManager;
	protected static EntityManager  db;
	private DataAccess sut = new DataAccess(ConfigXML.getInstance().getDataBaseOpenMode().equals("initialize"));
	private BLFacadeImplementation bl = new BLFacadeImplementation(sut);
	private TestFacadeImplementation testBL = new TestFacadeImplementation();

	private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	String userName = "pablo";
	String userPass = "1234";
	String firstName = "Pablo";
	String lastName = "Ferreras";
	String userId = "001";
	String bd = "24/07/1993";
	String email = "aaa@aaa.com";
	String bankAccount = "111";
	Integer phoneNumber = 666666666;
	String address = "Calle a 1ºD";
	


	@Test
	@DisplayName("Correct login")
	void LoginCorrectTest1() throws EventFinished, UserAlreadyExistsException, UserDoesNotExistException, IncorrectPassException, ParseException {

		try {
			
			String un = "pablo";
			String pass = "1234";
			
			Date birthDate = sdf.parse(bd);

			// configure the state of the system (create object in the dabatase)
			
			testBL.addUser(userName, userPass, firstName, lastName, userId, birthDate,
					email, bankAccount, phoneNumber, address);
			
			User expected = bl.getUserByUsername("pablo");
			User obtained = bl.login(un, pass);
			
			
			// invoke System Under Test (sut)
			assertEquals(expected, obtained);
			

		} catch (ParseException e) {
			fail("No problems should arise");
		} finally {
			// Remove the created objects in the database (cascade removing)
			Date birthDate = sdf.parse(bd);
			RegularUser ru = new RegularUser(userName, userPass, firstName, lastName, userId, birthDate,
					email, bankAccount, phoneNumber, address);
			testBL.removeUser(ru);
		}

	}
	
	
	@Test
	@DisplayName("Incorrect password")
	void LoginIncorrectPassTest2() throws EventFinished, UserAlreadyExistsException, UserDoesNotExistException, IncorrectPassException, ParseException {

		try {
			
			String un = "pablo";
			String pass = "0000";
			
			Date birthDate = sdf.parse(bd);

			// configure the state of the system (create object in the dabatase)
			
			testBL.addUser(userName, userPass, firstName, lastName, userId, birthDate,
					email, bankAccount, phoneNumber, address);
			
			
			
			// invoke System Under Test (sut)
			assertThrows(IncorrectPassException.class, () -> bl.login(un, pass));
			

		} catch (ParseException e) {
			fail("No problems should arise");
		} finally {
			// Remove the created objects in the database (cascade removing)
			Date birthDate = sdf.parse(bd);
			RegularUser ru = new RegularUser(userName, userPass, firstName, lastName, userId, birthDate,
					email, bankAccount, phoneNumber, address);
			testBL.removeUser(ru);
		}

	}
	
	
	@Test
	@DisplayName("Incorrect userName")
	void LoginIncorrectUserTest3() throws EventFinished, UserAlreadyExistsException, UserDoesNotExistException, IncorrectPassException, ParseException {

		try {
			
			String un = "aaa";
			String pass = "0000";
			
			Date birthDate = sdf.parse(bd);

			// configure the state of the system (create object in the dabatase)
			
			testBL.addUser(userName, userPass, firstName, lastName, userId, birthDate,
					email, bankAccount, phoneNumber, address);
			
			
			
			// invoke System Under Test (sut)
			assertThrows(UserDoesNotExistException.class, () -> bl.login(un, pass));
			

		} catch (ParseException e) {
			fail("No problems should arise");
		} finally {
			// Remove the created objects in the database (cascade removing)
			Date birthDate = sdf.parse(bd);
			RegularUser ru = new RegularUser(userName, userPass, firstName, lastName, userId, birthDate,
					email, bankAccount, phoneNumber, address);
			testBL.removeUser(ru);
		}

	}

}
