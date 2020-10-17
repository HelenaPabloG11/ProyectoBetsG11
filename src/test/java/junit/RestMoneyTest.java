package junit;

import static org.junit.jupiter.api.Assertions.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import businessLogic.BLFacadeImplementation;
import configuration.ConfigXML;
import dataAccess.DataAccess;
import domain.RegularUser;
import exceptions.Negative;
import exceptions.NoMoneyException;
import test.businessLogic.TestFacadeImplementation;

class RestMoneyTest {
	
	private DataAccess sut = new DataAccess(ConfigXML.getInstance().getDataBaseOpenMode().equals("initialize"));
	private BLFacadeImplementation bl = new BLFacadeImplementation(sut);
	private TestFacadeImplementation testBL = new TestFacadeImplementation();
	
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
	
	@Test
	@DisplayName("LessMoneyThanKop")
	void LessMoneyThanKop() throws ParseException {
		try {
			float kop=24.5f;
			Date birthDate = date.parse(bd);
			
			testBL.addUser(userName, userPass, firstName, lastName, userId, birthDate,email, bankAccount, phoneNumber, address);
			
			RegularUser user = new RegularUser(userName, userPass, firstName, lastName, userId, birthDate,
					email, bankAccount, phoneNumber, address);
			
			user.setWallet(20);
			
			assertThrows(NoMoneyException.class,()-> bl.restMoney(kop,user));
			
		}catch (ParseException e) {
			fail("No problems should arise");
		}finally {
			Date birthDate = date.parse(bd);
			
			RegularUser user = new RegularUser(userName, userPass, firstName, lastName, userId, birthDate,
					email, bankAccount, phoneNumber, address);
			
			testBL.removeUser(user);
		}
	}
	
	@Test
	@DisplayName("KopEqualMoreThanZero")
	void KopEqualMoreThanZero() throws ParseException, Negative, NoMoneyException{
		
		try{
			float kop= 34.2f;
		
			float esperado,obtenido;
			Date birthDate = date.parse(bd);
		
			testBL.addUser(userName, userPass, firstName, lastName, userId, birthDate,email, bankAccount, phoneNumber, address);
		
			RegularUser user = new RegularUser(userName, userPass, firstName, lastName, userId, birthDate,
				email, bankAccount, phoneNumber, address);
		
			user.setWallet(50);
			bl.restMoney(kop, user);
			obtenido=user.getWallet();
			esperado= 50-kop;
			assertEquals(esperado,obtenido);
		}catch(ParseException e) {
			fail("No problems should arise");
		}finally {
			Date birthDate = date.parse(bd);
			
			RegularUser user = new RegularUser(userName, userPass, firstName, lastName, userId, birthDate,
					email, bankAccount, phoneNumber, address);
			
			testBL.removeUser(user);
		}
		
	}
	
	@Test
	@DisplayName("KopLessThanZero")
	void kopLessThanZero() throws Negative, NoMoneyException, ParseException {
		try {
			float kop= -1.2f;
			
			Date birthDate = date.parse(bd);
		
			testBL.addUser(userName, userPass, firstName, lastName, userId, birthDate,email, bankAccount, phoneNumber, address);
		
			RegularUser user = new RegularUser(userName, userPass, firstName, lastName, userId, birthDate,
				email, bankAccount, phoneNumber, address);
		
			user.setWallet(50);

			assertThrows(Negative.class,()-> bl.restMoney(kop, user));
		}catch(ParseException e) {
			fail("No problems should arise");
		}finally {
			Date birthDate = date.parse(bd);
			
			RegularUser user = new RegularUser(userName, userPass, firstName, lastName, userId, birthDate,
					email, bankAccount, phoneNumber, address);
			
			testBL.removeUser(user);
		}
	}

}
