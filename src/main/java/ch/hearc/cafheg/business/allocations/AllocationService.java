package ch.hearc.cafheg.business.allocations;

import ch.hearc.cafheg.business.versements.Enfant;
import ch.hearc.cafheg.infrastructure.persistance.AllocataireMapper;
import ch.hearc.cafheg.infrastructure.persistance.AllocationMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public class AllocationService {

  private static final String PARENT_1 = "Parent1";
  private static final String PARENT_2 = "Parent2";

  private final AllocataireMapper allocataireMapper;
  private final AllocationMapper allocationMapper;

  static Logger logger = LoggerFactory.getLogger(AllocationService.class);

  public AllocationService(
      AllocataireMapper allocataireMapper,
      AllocationMapper allocationMapper) {
    this.allocataireMapper = allocataireMapper;
    this.allocationMapper = allocationMapper;
  }

  public String deleteAllocataireById(int id) {
    logger.debug("Deleting allocataire with id {}", id);
    return allocataireMapper.deleteAllocataireById(id);
  }

  public String updateAllocataireName(int id, String nom, String prenom) {
    logger.debug("Updating allocataire with id {}", id);
    return allocataireMapper.updateAllocataireName(nom, prenom, id);
  }



  public List<Allocataire> findAllAllocataires(String likeNom) {
    System.out.println("Rechercher tous les allocataires");
    return allocataireMapper.findAll(likeNom);
  }

  public List<Allocation> findAllocationsActuelles() {
    return allocationMapper.findAll();
  }

  /*
  * List of all parameters defined in the tests :
  * parent1ActiviteLucrative
  * parent2ActiviteLucrative => allocataire.isActive
  * parent1WithParentalAuthority allocataire.get
  * parent2WithParentalAuthority
  * parentsEnsemble => famille.parentsTogether
  * enfantResidence => enfant.canton
  * P1WorkInChildCanton => parent1WorkInChildCanton
  * P2WorkInChildCanton => famille.parent2WorkInChildCanton
  * P1Salaried allocataire.isSalaried
  * P2Salaried
  * parent1Salaire allocataire.salaire
  * parent2Salaire
  *
  * Test a : test on Allocataire.isActive
  * test b : test on Allocataire.parentalAuthority
  * test c : test on Famille.parentsTogether=false AND which parent is the kid with
  * test d = Famille.parentsTogether=true AND Famille.parentXWorkInChildCanton
  * test e = Famille.parentsTogether=true AND one Allocataire.isSalaried (or both) THEN Allocataire with big salary
  * test f = Famille.parentsTogether=true AND parents.isSalaried = false THEN parent with big salary.
  * */

  public String getParentDroitAllocation(Famille famille) throws IllegalArgumentException{
    logger.debug("Starting getParentDroitAllocation");

    Allocataire parent1 = famille.parent1;
    logger.debug("Parent 1 : {}", parent1);
    Allocataire parent2 = famille.parent2;
    logger.debug("Parent 2 : {}", parent2);
    Enfant enfant = famille.enfant;
    logger.debug("Enfant : {}", enfant);

    if (!parent1.isActive() && !parent2.isActive()){
      IllegalArgumentException e = new IllegalArgumentException("Can't have two parents that don't work !");
      logger.error("Incorrect, both parent can't work", e);
      throw e;
      // test for exception
    } else if (parent1.isActive() && !parent2.isActive()){
      logger.debug("returned PARENT1");
      return PARENT_1;
    } else if (parent2.isActive() && !parent1.isActive()) {
      logger.debug("returned PARENT2");
      return PARENT_2; // test a done
    } else if (parent1.getParentalAuthority() && !parent2.getParentalAuthority()) {
      logger.debug("returned PARENT1");
      return PARENT_1;
    } else if (parent2.getParentalAuthority() && !parent1.getParentalAuthority()) {
      logger.debug("returned PARENT2");
      return PARENT_2; // test b done
    } else if (!famille.isParentsTogether()) {
      if (parent1.getLifeCanton() != enfant.getCanton() && parent2.getLifeCanton()==enfant.getCanton()){
        logger.debug("returned PARENT2");
        return PARENT_2;// this is test c
      } else if(parent2.getLifeCanton() != enfant.getCanton() && parent1.getLifeCanton()==enfant.getCanton()){
        logger.debug("returned PARENT1");
        return PARENT_1;
      } else {
        IllegalArgumentException e = new IllegalArgumentException("Can't have two parents that don't work !");
        logger.error("Incorrect, both parent can't work", e);
        throw e;
      }
    } else {
      if (famille.isParent1WorkInChildCanton() && !famille.isParent2WorkInChildCanton()){
        logger.debug("returned PARENT1");
        return PARENT_1;
      } else if (famille.isParent2WorkInChildCanton() && !famille.isParent1WorkInChildCanton()) {
        logger.debug("returned PARENT2");
        return PARENT_2; // test d done
      } else if (parent1.isSalaried() && parent2.isSalaried()){
        String parent =  (parent1.getSalaire().doubleValue() > parent2.getSalaire().doubleValue()) ? PARENT_1 : PARENT_2;
        logger.debug("returned {}", parent);
        return parent;
        // ^^ this is test e, both salaired, so test on salaire value
      } else if (parent1.isSalaried() && !parent2.isSalaried()) {
        logger.debug("returned PARENT1");
        return PARENT_1;
      } else if (parent2.isSalaried() && !parent1.isSalaried()){
        logger.debug("returned PARENT2");
        return PARENT_2; // test e done
      } else if (!parent1.isSalaried() && !parent2.isSalaried()){
        String parent =  (parent1.getSalaire().doubleValue() > parent2.getSalaire().doubleValue()) ? PARENT_1 : PARENT_2;
        logger.debug("returned {}", parent);
        return parent;
        // this is test f done, both parents are independant so test on salaire value
      } else {
        IllegalArgumentException e = new IllegalArgumentException("Incorrect argument");
        logger.error("Incorrect arguments", e);
        throw e;
      }
    }
  }
}
