package junitMock;

import static org.junit.jupiter.api.Assertions.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import businessLogic.BLFacade;
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

class LoginTestMock {
	
	private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

	DataAccess dataAccess = Mockito.mock(DataAccess.class);
	RegularUser mockedRegularUser = Mockito.mock(RegularUser.class);

	BLFacade sut = new BLFacadeImplementation(dataAccess);

	// sut.createQuestion: The event has one question with a queryText.

	@BeforeEach
	public void setUp() throws ParseException {
		
		MockitoAnnotations.initMocks(this);
	}
	

	@Test
	@DisplayName("Correct login")
	void LoginCorrectMockTest1() throws UserDoesNotExistException, IncorrectPassException, ParseException {
		String userName = "pablo";
		String userPass = "1234";
		String firstName = "Pablo";
		String lastName = "Ferreras";
		String userId = "001";
		Date birthDate = sdf.parse("24/07/1993");
		String email = "aaa@aaa.com";
		String bankAccount = "111";
		Integer phoneNumber = 666666666;
		String address = "Calle a 1ºD";
		
		RegularUser ru = new RegularUser(userName, userPass, firstName, lastName, userId, birthDate,
					email, bankAccount, phoneNumber, address);

		String un = "pablo";
		String pass = "1234";
		

		// configure Mock
		Mockito.doReturn(ru).when(dataAccess).login(un, pass);

		// invoke System Under Test (sut)
		User expected = ru;
		User obtained = sut.login(un, pass);
		assertEquals(expected, obtained);
	}

	
	
	@Test
	@DisplayName("Incorrect userName")
	void LoginIncorrectUserTest2() throws UserDoesNotExistException, IncorrectPassException, ParseException {

		String un = "aaa";
		String pass = "1234";
		

		// configure Mock
		Mockito.when(dataAccess.login(Mockito.anyString(), Mockito.anyString())).thenThrow(UserDoesNotExistException.class);

		// invoke System Under Test (sut)
		assertThrows(UserDoesNotExistException.class, () -> sut.login(un, pass));
	}
	
	
	@Test
	@DisplayName("Incorrect password")
	void LoginIncorrectPassTest3() throws UserDoesNotExistException, IncorrectPassException, ParseException {

		String un = "aaa";
		String pass = "1234";
		

		// configure Mock
		Mockito.when(dataAccess.login(Mockito.anyString(), Mockito.anyString())).thenThrow(IncorrectPassException.class);

		// invoke System Under Test (sut)
		assertThrows(IncorrectPassException.class, () -> sut.login(un, pass));
	}

}
