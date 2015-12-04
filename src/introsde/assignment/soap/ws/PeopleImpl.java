package introsde.assignment.soap.ws;

import introsde.assignment.soap.model.LifeStatus;
import introsde.assignment.soap.model.Person;
import introsde.assignment.soap.model.HealthMeasureHistory;
import introsde.assignment.soap.model.MeasureDefinition;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.jws.WebService;

//Service Implementation

@WebService(endpointInterface = "introsde.assignment.soap.ws.People",
	serviceName="PeopleService")
public class PeopleImpl implements People {

	@Override
	public Person readPerson(int id) {
		System.out.println("---> Reading Person by id = "+id);
		Person p = Person.getPersonById(id);
		if (p!=null) {
			System.out.println("---> Found Person by id = "+id+" => "+p.getName());
		} else {
			System.out.println("---> Didn't find any Person with  id = "+id);
		}
		return p;
	}

	@Override
	public List<Person> getPeople() {
		return Person.getAll();
	}

	@Override
	public int addPerson(Person person) {
		System.out.println("Creating new person...");   
		int idMeasureDef = 0;
		
		//Check whether the person with the same id already exist
		Person existing = getPersonById(person.getIdPerson());
		if (existing == null) {
			//Set idPerson to the LifeStatus
	        List<LifeStatus> lifeStatus = person.getLifeStatus(); 
	        if(lifeStatus != null){
	        	for(LifeStatus life: lifeStatus){
	        		life.setPerson(person);
	        		
	        		//Set idMeasureDef to the health profile
	        		MeasureDefinition measureDef = MeasureDefinition.getIdMeasureDef(life.getMeasureDefinition().getMeasureName());
		        	idMeasureDef = measureDef.getIdMeasureDef();
		        	life.getMeasureDefinition().setIdMeasureDef(idMeasureDef);
	        	}
	        }	      
			Person.savePerson(person);
		}
		else
			return -1;
        
		return person.getIdPerson();
	}

	@Override
	public int updatePerson(Person person) {
		System.out.println("--> Updating Person... " +person.getIdPerson());
		
		Person existing = getPersonById(person.getIdPerson());
		if (existing == null) {
			return -1;
		}
		else {
			int idMeasureDef = 0;
	    	
	        //Preserve current LifeStatus. Prevent duplicate entry with different idMeasure
	        List<LifeStatus> list = person.getLifeStatus();	       
	        for(LifeStatus lifeStatus: list){
	        	lifeStatus.setPerson(person);	
	        	
	        	MeasureDefinition measureDef = MeasureDefinition.getIdMeasureDef(lifeStatus.getMeasureDefinition().getMeasureName());
	        	idMeasureDef = measureDef.getIdMeasureDef();
	            lifeStatus.getMeasureDefinition().setIdMeasureDef(idMeasureDef);
	            
	        	LifeStatus currentLifeStatusInDn = LifeStatus.getByMeasureDef(person.getIdPerson(), idMeasureDef);
	        	lifeStatus.setIdMeasure(currentLifeStatusInDn.getIdMeasure());   	        	
	        }            
			Person.updatePerson(person);
		}				
		return person.getIdPerson();
	}

	@Override
	public int deletePerson(int id) {
		Person p = Person.getPersonById(id);
		if (p!=null) {
			Person.removePerson(p);
			return 1;
		} else {
			return -1;
		}
	}

	@Override
	public int updatePersonHP(int id, LifeStatus hp) {
		int idMeasureDef = 0;
    	
    	Person person = Person.getPersonById(id);
    	if(person == null)
    		return -1;

    	MeasureDefinition measureDef = MeasureDefinition.getIdMeasureDef(hp.getMeasureDefinition().getMeasureName());
    	idMeasureDef = measureDef.getIdMeasureDef();
    	
    	LifeStatus ls = LifeStatus.getLifeStatusById(hp.getIdMeasure());    	
    	if(ls.getPerson().getIdPerson() == id){
    		hp.setPerson(person);
    		hp.setMeasureDefinition(new MeasureDefinition(idMeasureDef));
    		LifeStatus.updateLifeStatus(hp);    		   		
    	}
    	else
    		return -1;
    	
    	return hp.getIdMeasure();		
	}
	
	@Override
	public List<HealthMeasureHistory> readPersonHistory(int id, String mt){
		System.out.println("Reading HealthMeasureHistory from DB with id: "+id+" and measureType: "+ mt);

    	int idMeasureDef = 0;
    	
    	MeasureDefinition measureDef = MeasureDefinition.getIdMeasureDef(mt);
    	idMeasureDef = measureDef.getIdMeasureDef();
    		
    	List<HealthMeasureHistory> healthMeasureHistory = HealthMeasureHistory.getMeasureByPersonId(id, idMeasureDef);
        return healthMeasureHistory;
	}
	
	@Override
	public List<MeasureDefinition> readMeasureTypes(){
		System.out.println("Getting list of measureTypes...");
        List<MeasureDefinition> measureType = MeasureDefinition.getAll();
        return measureType;
	}
	
	@Override
	public HealthMeasureHistory readPersonMeasure(int id, String mt, int mid){
		System.out.println("Reading HealthMeasureHistory from DB with id: "+id+", measureType: "+ mt + " and mid: " + mid);
		
    	int idMeasureDef = 0;
    	
    	MeasureDefinition measureDef = MeasureDefinition.getIdMeasureDef(mt);
    	idMeasureDef = measureDef.getIdMeasureDef();
    	    	
    	HealthMeasureHistory healthMeasureHistory = HealthMeasureHistory.getMeasureByMid(id, idMeasureDef, mid);        
        
        return healthMeasureHistory;
	}
	
	@Override
	public LifeStatus savePersonMeasure(int id, LifeStatus ls){
		try{
			int idMeasureDef = 0;
	    	
	    	MeasureDefinition measureDef = MeasureDefinition.getIdMeasureDef(ls.getMeasureDefinition().getMeasureName());
	    	idMeasureDef = measureDef.getIdMeasureDef();
	    	ls.getMeasureDefinition().setIdMeasureDef(idMeasureDef);
	    	    	
	    	//Get a Person by given idPerson
	    	Person person = Person.getPersonById(id);
	    	//Get a LifeStatus by given idPerson and MeasureType
	    	LifeStatus currentLifeStatus = LifeStatus.getByMeasureDef(id, idMeasureDef);
	    	
	    	DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
	    	Date today = Calendar.getInstance().getTime();
	    	
	    	//Save currentLifeStatus into HealthMeasureHistory Table
	    	HealthMeasureHistory newHealthMeasureHistory = new HealthMeasureHistory();
	    	newHealthMeasureHistory.setPerson(person);
	    	newHealthMeasureHistory.setMeasureDefinition(new MeasureDefinition(idMeasureDef));
	    	newHealthMeasureHistory.setTimestamp(df.format(today));
	    	newHealthMeasureHistory.setValue(currentLifeStatus.getValue());
	    	HealthMeasureHistory.saveHealthMeasureHistory(newHealthMeasureHistory);
	    	
	    	//Delete currentLifeStatus from LifeStatus Table    	
	    	LifeStatus.removeLifeStatus(currentLifeStatus);
	    	
	    	//Create a new LifeStatus for the corresponding person
	    	ls.setPerson(person);
	    	LifeStatus.saveLifeStatus(ls);	    	    		    	
		}
		catch(Exception e){
			e.printStackTrace();
		}		
		return ls;
	}		
	
	public Person getPersonById(int personId) {
        System.out.println("Reading person from DB with id: "+personId);
        
        Person person = Person.getPersonById(personId);
        if(person != null)        	     	    
        	System.out.println("Person: "+person.toString());
        
        return person;
    }
}

