package test.businessLogic;
/**
 * Auxiliary FacadeImplementation class for testing DataAccess
 */

import java.util.Date;

import configuration.ConfigXML;
import domain.Event;
import domain.RegularUser;
import domain.User;
import test.dataAccess.TestDataAccess;

public class TestFacadeImplementation {
	TestDataAccess dbManagerTest;
 	
    
	   public TestFacadeImplementation()  {
			
			System.out.println("Creating TestFacadeImplementation instance");
			ConfigXML c=ConfigXML.getInstance();
			dbManagerTest=new TestDataAccess(); 
			dbManagerTest.close();
		}
		
		 
		public boolean removeEvent(Event ev) {
			dbManagerTest.open();
			boolean b=dbManagerTest.removeEvent(ev);
			dbManagerTest.close();
			return b;

		}
		
		public Event addEvent(String desc, Date d) {
			dbManagerTest.open();
			Event o=dbManagerTest.addEvent(desc,d);
			dbManagerTest.close();
			return o;

		}
		
		
		public boolean removeUser(User u) {
			dbManagerTest.open();
			boolean b=dbManagerTest.removeUser(u);
			dbManagerTest.close();
			return b;
		}
		
		
		public User addUser(String userName, String userPass, String firstName, String lastName, String userId, Date birthDate, 
			String email, String bankAccount, Integer phoneNumber, String address) {
			dbManagerTest.open();
			User u=dbManagerTest.addUser(userName, userPass, firstName, lastName, userId, birthDate, email, bankAccount, phoneNumber, address);
			dbManagerTest.close();
			return u;
		}

}
