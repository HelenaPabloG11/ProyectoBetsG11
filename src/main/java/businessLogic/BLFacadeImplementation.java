package businessLogic;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Vector;

import javax.jws.WebMethod;
import javax.jws.WebService;

import configuration.ConfigXML;
import dataAccess.DataAccess;
import domain.Question;
import domain.RegularUser;
import domain.User;
import domain.Bet;
import domain.Event;
import domain.IntegerAdapter;
import domain.Kuota;
import domain.Movement;
import exceptions.*;

/**
 * It implements the business logic as a web service.
 */
@WebService(endpointInterface = "businessLogic.BLFacade")
public class BLFacadeImplementation  implements BLFacade {

	DataAccess dbManager;

	
    public BLFacadeImplementation(DataAccess da)  {
		
		System.out.println("Creating BLFacadeImplementation instance with DataAccess parameter");
		ConfigXML c=ConfigXML.getInstance();
		
		if (c.getDataBaseOpenMode().equals("initialize")) {
			da.open(true);
			da.initializeDB();
			da.close();

		}
		dbManager=da;		
	}


	/**
	 * This method creates a question for an event, with a question text and the minimum bet
	 * 
	 * @param event to which question is added
	 * @param question text of the question
	 * @param betMinimum minimum quantity of the bet
	 * @return the created question, or null, or an exception
	 * @throws EventFinished if current data is after data of the event
	 * @throws QuestionAlreadyExist if the same question already exists for the event
	 */
	@WebMethod
	public Question createQuestion(Event event, String question, float betMinimum) throws EventFinished, QuestionAlreadyExist{

		//The minimum bed must be greater than 0
		dbManager.open(false);
		Question qry=null;


		if(new Date().compareTo(event.getEventDate())>0)
			throw new EventFinished(ResourceBundle.getBundle("Etiquetas").getString("ErrorEventHasFinished"));


		qry=dbManager.createQuestion(event,question,betMinimum);		

		dbManager.close();

		return qry;
	}

	/**
	 * This method invokes the data access to retrieve the events of a given date 
	 * 
	 * @param date in which events are retrieved
	 * @return collection of events
	 */
	@WebMethod	
	public Vector<Event> getEvents(Date date)  {
		dbManager.open(false);
		Vector<Event>  events=dbManager.getEvents(date);
		dbManager.close();
		return events;
	}


	/**
	 * This method invokes the data access to retrieve the dates a month for which there are events
	 * 
	 * @param date of the month for which days with events want to be retrieved 
	 * @return collection of dates
	 */
	@WebMethod public Vector<Date> getEventsMonth(Date date) {
		dbManager.open(false);
		Vector<Date>  dates=dbManager.getEventsMonth(date);
		dbManager.close();
		return dates;
	}

	/**
	 * This method invokes the data access to initialize the database with some events and questions.
	 * It is invoked only when the option "initialize" is declared in the tag dataBaseOpenMode of resources/config.xml file
	 */	
	@WebMethod	
	public void initializeBD(){
		dbManager.open(false);
		dbManager.initializeDB();
		dbManager.close();
	}

	@WebMethod
	public void createNewUser(RegularUser user) throws UserAlreadyExistsException {
		dbManager.open(false);
		dbManager.createNewUser(user);
		dbManager.close();
	}

	@WebMethod
	public User getUserByUsername(String userName) throws UserDoesNotExistException {
		dbManager.open(false);
		User user = dbManager.getUserByUsername(userName);
		dbManager.close();
		return user;
	}

	@WebMethod 
	public User login(String userName, String password) throws UserDoesNotExistException,IncorrectPassException{
		dbManager.open(false);
		User login = dbManager.login(userName, password);
		dbManager.close();
		return login;
	}

	@WebMethod
	public Kuota createKuota(Question question, String result, float kuota) throws EventFinished, KuotaAlreadyExist {

		//The minimum bed must be greater than 0
		dbManager.open(false);
		Kuota qry=null;


		//if(new Date().compareTo(question.getEvent().getEventDate())>0)
		//throw new EventFinished(ResourceBundle.getBundle("Etiquetas").getString("ErrorEventHasFinished"));
		//Koutak sortu GUI-an inplementatua

		qry=dbManager.createKuota(question, result, kuota);		

		dbManager.close();

		return qry;
	}

	@WebMethod
	public Event createEvent(Date data, String event) throws EventAlreadyExist, EventFinished {
		dbManager.open(false);
		if(new Date().compareTo(data)>0)
			throw new EventFinished(ResourceBundle.getBundle("Etiquetas").getString("ErrorEventHasFinished"));
		Event e = dbManager.createEvent(data,event);		
		dbManager.close();	
		return e; 
	}

	@WebMethod public void updateQueResult(int zein, String result) {
		dbManager.open(false);
		dbManager.updateQueResult(zein, result);
		dbManager.close();
	}

	@WebMethod
	public void addBet(RegularUser user,Bet bet) throws BetAlreadyExist,Negative,MovementAlreadyExistsException, NoMoneyException{
		dbManager.open(false);
		System.out.println("DAD");

		dbManager.addBet(user, bet);
		dbManager.close();
	}

	@WebMethod
	public void restMoney(float kop,RegularUser user) throws Negative, NoMoneyException {
		dbManager.open(false);
		dbManager.restMoney( kop,  user);
		dbManager.close();
	}

	@WebMethod
	public double putMoney(float kop,RegularUser user) throws Negative {
		dbManager.open(false);
		dbManager.putMoney( kop,  user);
		dbManager.close();
		return kop;
	}

	@WebMethod
	public float howMuchMoney(RegularUser user) {
		dbManager.open(false);
		Float money=dbManager.howMuchMoney(user);
		dbManager.close();
		return money;
	}

	/*
	@WebMethod
	public Collection<Bet> getBetsByUser(RegularUser user,ArrayList<Kuota> kuota) {
		DataAccess dBManager = new DataAccess();
		Collection<Bet> bets =dBManager.getBetsByUser( user,kuota);
		dBManager.close();
		return bets;
	}
	 */

	@WebMethod
	public Collection<Bet> getBetsByUser(RegularUser user) {
		dbManager.open(false);
		Collection<Bet> bets =dbManager.getBetsByUser(user);
		dbManager.close();
		return bets;
	}

	@WebMethod
	public void removeBet(Bet bet) throws Negative, MovementAlreadyExistsException,EventFinished {
		dbManager.open(false);
		dbManager.removeBet(bet);
		dbManager.close();
	}

	@WebMethod public void createMovement(Movement movement) throws MovementAlreadyExistsException {
		dbManager.open(false);
		dbManager.createMovement(movement);
		dbManager.close();
	}

	@WebMethod public Collection<Movement> getMovementsByUser(RegularUser user) {
		dbManager.open(false);
		Collection<Movement> movements = dbManager.getMovementsByUser(user);
		dbManager.close();
		return movements;
	}

	@WebMethod 
	public Vector<Kuota> getKuotas(int qZein) {
		dbManager.open(false);
		Vector<Kuota> kuotas = dbManager.getKuotas(qZein);
		dbManager.close();
		return kuotas;
	}

	@WebMethod 
	public void calculateProfits(Bet bet) throws Negative, MovementAlreadyExistsException, ResultNotEqual,EventFinished{
		dbManager.open(false);
		dbManager.calculateProfits(bet);
		dbManager.close();
	}

	@WebMethod 
	public Question getQuestion(int qZein) {
		dbManager.open(false);
		Question question = dbManager.getQuestion(qZein);
		dbManager.close();
		return question;
	}

	@WebMethod 
	public void cancelEvent(Event event) throws Negative, MovementAlreadyExistsException, EventFinished {
		dbManager.open(false);
		dbManager.cancelEvent(event);
		dbManager.close();
	}

	@WebMethod 
	public ArrayList<RegularUser>getUsers(){
		dbManager.open(false);
		ArrayList<RegularUser>users=dbManager.getUsers();
		return users;
	}
	@WebMethod 
	public boolean betExists(RegularUser user , Bet bet) {
		dbManager.open(false);
		boolean exist= dbManager.betExists(user, bet);
		dbManager.close();
		return exist;

	}

}

