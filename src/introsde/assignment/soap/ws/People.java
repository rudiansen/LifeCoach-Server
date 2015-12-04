package introsde.assignment.soap.ws;

import introsde.assignment.soap.model.HealthMeasureHistory;
import introsde.assignment.soap.model.LifeStatus;
import introsde.assignment.soap.model.MeasureDefinition;
import introsde.assignment.soap.model.Person;

import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.jws.WebResult;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.Style;
import javax.jws.soap.SOAPBinding.Use;

@WebService
@SOAPBinding(style = Style.DOCUMENT, use=Use.LITERAL) //optional
public interface People {
    @WebMethod(operationName="readPerson")
    @WebResult(name="person") 
    public Person readPerson(@WebParam(name="personId") int id);
 
    @WebMethod(operationName="getPeopleList")
    @WebResult(name="people") 
    public List<Person> getPeople();
 
    @WebMethod(operationName="createPerson")
    @WebResult(name="personId") 
    public int addPerson(@WebParam(name="person") Person person);
 
    @WebMethod(operationName="updatePerson")
    @WebResult(name="personId") 
    public int updatePerson(@WebParam(name="person") Person person);
    
    @WebMethod(operationName="deletePerson")
    @WebResult(name="personId") 
    public int deletePerson(@WebParam(name="personId") int id);
    
    @WebMethod(operationName="updatePersonHealthProfile")
    @WebResult(name="hpId") 
    public int updatePersonHP(@WebParam(name="personId") int id, 
    		@WebParam(name="healthProfile") LifeStatus hp);
    
    @WebMethod(operationName="readPersonHistory")
    @WebResult(name="personHistory") 
    public List<HealthMeasureHistory> readPersonHistory(@WebParam(name="personId") int id, 
    		@WebParam(name="measureType") String mt);
    
    @WebMethod(operationName="readMeasureTypes")
    @WebResult(name="measureTypes") 
    public List<MeasureDefinition> readMeasureTypes();
    
    @WebMethod(operationName="readPersonMeasure")
    @WebResult(name="personMeasure") 
    public HealthMeasureHistory readPersonMeasure(@WebParam(name="personId") int id, 
    		@WebParam(name="measureType") String mt,
    		@WebParam(name="measureId") int mid);
    
    @WebMethod(operationName="savePersonMeasure")
    @WebResult(name="healthProfile") 
    public LifeStatus savePersonMeasure(@WebParam(name="personId") int id, 
    		@WebParam(name="healthProfile") LifeStatus hp);
}
