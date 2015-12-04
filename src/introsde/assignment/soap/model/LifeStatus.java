package introsde.assignment.soap.model;

import introsde.assignment.soap.dao.LifeCoachDao;
import introsde.assignment.soap.model.MeasureDefinition;
import introsde.assignment.soap.model.LifeStatus;

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
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.persistence.OneToOne;

/**
 * The persistent class for the "LifeStatus" database table.
 * 
 */
@Entity
@Table(name = "LifeStatus")
@NamedQueries({
	@NamedQuery(name = "LifeStatus.findAll", query = "SELECT l FROM LifeStatus l"),
	@NamedQuery(name="LifeStatus.getByMeasureDef", query = "SELECT l FROM LifeStatus l where l.person.idPerson = :idPerson "
			+ "and l.measureDefinition.idMeasureDef = :idMeasureDef"),
	@NamedQuery(name="LifeStatus.getByMid", query = "SELECT l FROM LifeStatus l where l.person.idPerson = :idPerson "
			+ "and l.measureDefinition.idMeasureDef = :idMeasureDef and l.idMeasure = :idMeasure")
})
@XmlAccessorType(XmlAccessType.NONE)
public class LifeStatus implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator="sqlite_lifestatus")
	@TableGenerator(name="sqlite_lifestatus", table="sqlite_sequence",
	    pkColumnName="name", valueColumnName="seq",
	    pkColumnValue="LifeStatus", allocationSize=1)
	@Column(name = "idMeasure")
	@XmlElement(name="mid")
	private int idMeasure;

	@Column(name="dateRegistered")
	@XmlElement
	private String dateRegistered;
	
	@OneToOne
	@JoinColumn(name = "idMeasureDef", referencedColumnName = "idMeasureDef", insertable = true, updatable = true)	
	@XmlElement
	private MeasureDefinition measureDefinition;
	
	@Column(name = "value")
	@XmlElement(name="measureValue")
	private String value;

	@ManyToOne
	@JoinColumn(name="idPerson",referencedColumnName="idPerson")
	private Person person;

	public LifeStatus() {
	}

	public int getIdMeasure() {
		return this.idMeasure;
	}

	public void setIdMeasure(int idMeasure) {
		this.idMeasure = idMeasure;
	}
	
	public String getDateRegistered() {
		return dateRegistered;
	}

	public void setDateRegistered(String dateRegistered) {
		this.dateRegistered = dateRegistered;
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

	// we make this transient for JAXB to avoid and infinite loop on serialization
	@XmlTransient
	public Person getPerson() {
		return person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}
	
	// Database operations
	// Notice that, for this example, we create and destroy and entityManager on each operation. 
	// How would you change the DAO to not having to create the entity manager every time? 
	public static LifeStatus getLifeStatusById(int lifestatusId) {
		EntityManager em = LifeCoachDao.instance.createEntityManager();
		
		//Refresh the entity manager
        em.getEntityManagerFactory().getCache().evictAll();
        
		LifeStatus p = em.find(LifeStatus.class, lifestatusId);
		LifeCoachDao.instance.closeConnections(em);
		return p;
	}
	
	public static List<LifeStatus> getAll() {
		EntityManager em = LifeCoachDao.instance.createEntityManager();
		
		//Refresh the entity manager
        em.getEntityManagerFactory().getCache().evictAll();
        
	    List<LifeStatus> list = em.createNamedQuery("LifeStatus.findAll", LifeStatus.class).getResultList();
	    LifeCoachDao.instance.closeConnections(em);
	    return list;
	}
	
	public static LifeStatus getByMeasureDef(int idPerson, int idMeasureDef){
		EntityManager em = null;
		LifeStatus lifeStatus = null;
		
		try{
			em = LifeCoachDao.instance.createEntityManager();
			
			//Refresh the entity manager
	        em.getEntityManagerFactory().getCache().evictAll();
	        
		    lifeStatus = em.createNamedQuery("LifeStatus.getByMeasureDef", LifeStatus.class)
		    		.setParameter("idPerson", idPerson)
		    		.setParameter("idMeasureDef", idMeasureDef)
		    		.getSingleResult();
		}
		catch(Exception e){e.printStackTrace();}
		finally{
			LifeCoachDao.instance.closeConnections(em);
		}
	    return lifeStatus;
	}
	
	public static LifeStatus getByMid(int idPerson, int idMeasureDef, int mid){
		EntityManager em = null;
		LifeStatus lifeStatus = null;
		
		try{
			em = LifeCoachDao.instance.createEntityManager();
			
			//Refresh the entity manager
	        em.getEntityManagerFactory().getCache().evictAll();
	        
		    lifeStatus = em.createNamedQuery("LifeStatus.getByMeasureDef", LifeStatus.class)
		    		.setParameter("idPerson", idPerson)
		    		.setParameter("idMeasureDef", idMeasureDef)
		    		.setParameter("idMeasure", mid)
		    		.getSingleResult();
		}
		catch(Exception e){e.printStackTrace();}
		finally{
			LifeCoachDao.instance.closeConnections(em);
		}
	    return lifeStatus;
	}
	
	public static LifeStatus saveLifeStatus(LifeStatus p) {
		EntityManager em = LifeCoachDao.instance.createEntityManager();
		EntityTransaction tx = em.getTransaction();
		tx.begin();
		em.persist(p);
		tx.commit();
	    LifeCoachDao.instance.closeConnections(em);
	    return p;
	}
	
	public static LifeStatus updateLifeStatus(LifeStatus p) {
		EntityManager em = LifeCoachDao.instance.createEntityManager();
		EntityTransaction tx = em.getTransaction();
		tx.begin();
		p=em.merge(p);
		tx.commit();
	    LifeCoachDao.instance.closeConnections(em);
	    return p;
	}
	
	public static void removeLifeStatus(LifeStatus p) {
		EntityManager em = LifeCoachDao.instance.createEntityManager();
		EntityTransaction tx = em.getTransaction();
		tx.begin();
	    p=em.merge(p);
	    em.remove(p);
	    tx.commit();
	    LifeCoachDao.instance.closeConnections(em);
	}
}
