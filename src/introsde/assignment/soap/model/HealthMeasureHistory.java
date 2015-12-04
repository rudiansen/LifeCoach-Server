package introsde.assignment.soap.model;

import introsde.assignment.soap.dao.LifeCoachDao;
import introsde.assignment.soap.model.Person;
import introsde.assignment.soap.model.HealthMeasureHistory;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

/**
 * The persistent class for the "HealthMeasureHistory" database table.
 * 
 */
@Entity
@Table(name="HealthMeasureHistory")
@NamedQueries({
	@NamedQuery(name="HealthMeasureHistory.findAll", query="SELECT h FROM HealthMeasureHistory h"),
	@NamedQuery(name="HealthMeasureHistory.getMeasureByPersonId", query="SELECT h FROM HealthMeasureHistory h "
			+ "where h.person.idPerson = :idPerson and h.measureDefinition.idMeasureDef = :idMeasureDef"),
	@NamedQuery(name="HealthMeasureHistory.getMeasureByMid",query="SELECT h FROM HealthMeasureHistory h "
			+ "where h.person.idPerson = :idPerson and h.measureDefinition.idMeasureDef = :idMeasureDef and "
			+ "h.idMeasureHistory = :mid")
})
public class HealthMeasureHistory implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator="sqlite_mhistory")
	@TableGenerator(name="sqlite_mhistory", table="sqlite_sequence",
	    pkColumnName="name", valueColumnName="seq",
	    pkColumnValue="HealthMeasureHistory", allocationSize=1)
	@Column(name="idMeasureHistory")
	private int idMeasureHistory;
	
	@Column(name="timestamp")	
	private String timestamp;

	@Column(name="value")
	private String value;

	@ManyToOne
	@JoinColumn(name = "idMeasureDef", referencedColumnName = "idMeasureDef")
	private MeasureDefinition measureDefinition;

	// notice that we haven't included a reference to the history in Person
	// this means that we don't have to make this attribute XmlTransient
	@ManyToOne
	@JoinColumn(name = "idPerson", referencedColumnName = "idPerson")
	private Person person;

	public HealthMeasureHistory() {
	}

	public int getIdMeasureHistory() {
		return this.idMeasureHistory;
	}

	public void setIdMeasureHistory(int idMeasureHistory) {
		this.idMeasureHistory = idMeasureHistory;
	}

	public String getTimestamp() {
		return this.timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public String getValue() {
		return this.value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public MeasureDefinition getMeasureDefinition() {
	    return measureDefinition;
	}

	public void setMeasureDefinition(MeasureDefinition param) {
	    this.measureDefinition = param;
	}

	public Person getPerson() {
	    return person;
	}

	public void setPerson(Person param) {
	    this.person = param;
	}

	// database operations
	public static HealthMeasureHistory getHealthMeasureHistoryById(int id) {
		EntityManager em = LifeCoachDao.instance.createEntityManager();
		//Refresh the entity manager
        em.getEntityManagerFactory().getCache().evictAll();
        
		HealthMeasureHistory p = em.find(HealthMeasureHistory.class, id);
		LifeCoachDao.instance.closeConnections(em);
		return p;
	}
	
	public static List<HealthMeasureHistory> getAll() {
		EntityManager em = LifeCoachDao.instance.createEntityManager();
		//Refresh the entity manager
        em.getEntityManagerFactory().getCache().evictAll();
        
	    List<HealthMeasureHistory> list = em.createNamedQuery("HealthMeasureHistory.findAll", HealthMeasureHistory.class).getResultList();
	    LifeCoachDao.instance.closeConnections(em);
	    return list;
	}
	
	public static List<HealthMeasureHistory> getMeasureByPersonId(int personId, int idMeasureDef) {
		List<HealthMeasureHistory> list = null;
		EntityManager em = null;
		
		try{
			em = LifeCoachDao.instance.createEntityManager();
			//Refresh the entity manager
	        em.getEntityManagerFactory().getCache().evictAll();
	        
		    list = em.createNamedQuery("HealthMeasureHistory.getMeasureByPersonId", HealthMeasureHistory.class)
		    		.setParameter("idPerson", personId)
		    		.setParameter("idMeasureDef", idMeasureDef)
		    		.getResultList();
		}
		catch(Exception e){e.printStackTrace();}
		finally{
			LifeCoachDao.instance.closeConnections(em);
		}
				  
	    return list;
	}
	
	public static HealthMeasureHistory getMeasureByMid(int personId, int idMeasureDef, int mid) {
		HealthMeasureHistory list = null;
		EntityManager em = null;
		
		try{
			em = LifeCoachDao.instance.createEntityManager();
			//Refresh the entity manager
	        em.getEntityManagerFactory().getCache().evictAll();
	        
			list = em.createNamedQuery("HealthMeasureHistory.getMeasureByMid", HealthMeasureHistory.class)
		    		.setParameter("idPerson", personId)
		    		.setParameter("idMeasureDef", idMeasureDef)
		    		.setParameter("mid", mid)
		    		.getSingleResult();
		    
		}
		catch(Exception e){e.printStackTrace();}
		finally{
			LifeCoachDao.instance.closeConnections(em);
		}
		
	    return list;
	}
	
	public static HealthMeasureHistory saveHealthMeasureHistory(HealthMeasureHistory p) {
		EntityManager em = LifeCoachDao.instance.createEntityManager();
		EntityTransaction tx = em.getTransaction();
		tx.begin();
		em.persist(p);
		tx.commit();
	    LifeCoachDao.instance.closeConnections(em);
	    return p;
	}
	
	public static HealthMeasureHistory updateHealthMeasureHistory(HealthMeasureHistory p) {
		EntityManager em = LifeCoachDao.instance.createEntityManager();
		EntityTransaction tx = em.getTransaction();
		tx.begin();
		p=em.merge(p);
		tx.commit();
	    LifeCoachDao.instance.closeConnections(em);
	    return p;
	}
	
	public static void removeHealthMeasureHistory(HealthMeasureHistory p) {
		EntityManager em = LifeCoachDao.instance.createEntityManager();
		EntityTransaction tx = em.getTransaction();
		tx.begin();
	    p=em.merge(p);
	    em.remove(p);
	    tx.commit();
	    LifeCoachDao.instance.closeConnections(em);
	}
}

