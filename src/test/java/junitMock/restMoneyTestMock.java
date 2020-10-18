package junitMock;

import static org.junit.jupiter.api.Assertions.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import businessLogic.BLFacade;
import businessLogic.BLFacadeImplementation;
import dataAccess.DataAccess;
import domain.RegularUser;
import exceptions.Negative;
import exceptions.NoMoneyException;


class restMoneyTestMock {
	

	DataAccess dataAccess = Mockito.mock(DataAccess.class);

	BLFacade sut = new BLFacadeImplementation(dataAccess);
	
	private SimpleDateFormat date = new SimpleDateFormat("dd/MM/yyyy");
	String userName = "helena";
	String userPass = "4321";
	String firstName = "Helena";
	String lastName = "Fajin";
	String userId = "002";
	String bd = "01/09/2000";
	String email = "bdbdsb@bdfbz.com";
	String bankAccount = "1234567890";
	Integer phoneNumber = 666666666;
	String address = "C/Felipe IV";

	@BeforeEach
	public void setUp() throws ParseException {
		
		MockitoAnnotations.initMocks(this);
	}
	
	@Test
	@DisplayName("LessMoneyThanKop")
	void lessMoneyThanKop() throws ParseException, Negative, NoMoneyException {
		float kop=24.5f;
		float wallet=20f;
		
		Date birthDate = date.parse(bd);
		
		RegularUser user = new RegularUser(userName, userPass, firstName, lastName, userId, birthDate,
				email, bankAccount, phoneNumber, address);
		
		Mockito.doReturn(wallet).when(dataAccess).howMuchMoney(user);
		
		Mockito.doThrow(NoMoneyException.class).when(dataAccess).restMoney(kop,user);
		
		assertThrows(NoMoneyException.class,()-> sut.restMoney(kop,user));
	}
	
	@Test
	@DisplayName("KopEqualMoreThanZero")
	void kopEqualMoreThanZero() throws ParseException, Negative, NoMoneyException {
		float kop=24.5f;
		float wallet=50f;
		float esperado,obtenido;
		Date birthDate = date.parse(bd);
		
		RegularUser user = new RegularUser(userName, userPass, firstName, lastName, userId, birthDate,
				email, bankAccount, phoneNumber, address);
		
		esperado=wallet-kop;
		Mockito.doReturn(esperado).when(dataAccess).howMuchMoney(user);
		sut.restMoney(kop, user);
		obtenido=sut.howMuchMoney(user);
		assertEquals(esperado,obtenido);
		
	}

	@Test
	@DisplayName("KopLessThanZero")
	void kopLessThanZero() throws ParseException, Negative, NoMoneyException {
		float kop=-1.2f;
		
		Date birthDate = date.parse(bd);
		
		RegularUser user = new RegularUser(userName, userPass, firstName, lastName, userId, birthDate,
				email, bankAccount, phoneNumber, address);
		
		Mockito.doThrow(Negative.class).when(dataAccess).restMoney(kop,user);
		
		assertThrows(Negative.class,()-> sut.restMoney(kop,user));
	}
}
