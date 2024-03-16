package ch.hearc.cafheg.infrastructure.persistance;

import ch.hearc.cafheg.business.allocations.Allocataire;
import ch.hearc.cafheg.business.allocations.NoAVS;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AllocataireMapper extends Mapper {

  private final Logger logger = LoggerFactory.getLogger(AllocataireMapper.class);

  private static final String QUERY_FIND_ALL = "SELECT NOM,PRENOM,NO_AVS FROM ALLOCATAIRES";
  private static final String QUERY_FIND_WHERE_NOM_LIKE = "SELECT NOM,PRENOM,NO_AVS FROM ALLOCATAIRES WHERE NOM LIKE ?";
  private static final String QUERY_FIND_WHERE_NUMERO = "SELECT NO_AVS, NOM, PRENOM FROM ALLOCATAIRES WHERE NUMERO=?";
  private static final String QUERY_DELETE_WHERE_NUMERO = "DELETE FROM ALLOCATAIRES WHERE NUMERO = ?";
  private static final String QUERY_SELECT_ALL_VERSEMENTS_WHERE_NUMERO = "SELECT * FROM VERSEMENTS WHERE FK_ALLOCATAIRES = ?";

  private static final String QUERY_UPDATE_WHERE_NUMERO = "UPDATE ALLOCATAIRES SET NOM = ?, PRENOM = ? WHERE NUMERO = ?";


  public List<Allocataire> findAll(String likeNom) {
    System.out.println("findAll() " + likeNom);
    Connection connection = activeJDBCConnection();
    try {
      PreparedStatement preparedStatement;
      if (likeNom == null) {
        System.out.println("SQL: " + QUERY_FIND_ALL);
        preparedStatement = connection
            .prepareStatement(QUERY_FIND_ALL);
      } else {

        System.out.println("SQL: " + QUERY_FIND_WHERE_NOM_LIKE);
        preparedStatement = connection
            .prepareStatement(QUERY_FIND_WHERE_NOM_LIKE);
        preparedStatement.setString(1, likeNom + "%");
      }
      System.out.println("Allocation d'un nouveau tableau");
      List<Allocataire> allocataires = new ArrayList<>();

      System.out.println("Exécution de la requête");
      try (ResultSet resultSet = preparedStatement.executeQuery()) {

        System.out.println("Allocataire mapping");
        while (resultSet.next()) {
          System.out.println("ResultSet#next");
          allocataires
              .add(new Allocataire(new NoAVS(resultSet.getString(3)), resultSet.getString(2),
                  resultSet.getString(1)));
        }
      }
      System.out.println("Allocataires trouvés " + allocataires.size());
      return allocataires;
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  public Allocataire findById(long id) {
    System.out.println("findById() " + id);
    Connection connection = activeJDBCConnection();
    try {
      System.out.println("SQL:" + QUERY_FIND_WHERE_NUMERO);
      PreparedStatement preparedStatement = connection.prepareStatement(QUERY_FIND_WHERE_NUMERO);
      preparedStatement.setLong(1, id);
      ResultSet resultSet = preparedStatement.executeQuery();
      System.out.println("ResultSet#next");
      resultSet.next();
      System.out.println("Allocataire mapping");
      return new Allocataire(new NoAVS(resultSet.getString(1)),
          resultSet.getString(2), resultSet.getString(3));
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  public String deleteAllocataireById(int id){
    logger.info("starting deleteAllocataireById : {}", id);
    Connection connection = activeJDBCConnection();
    try {
      // first check if Allocataire id has versement linked to it.
      logger.debug("Checking if Allocataire {} has versement linked to it", id);
      logger.info("SQL: {}", QUERY_SELECT_ALL_VERSEMENTS_WHERE_NUMERO);
      PreparedStatement checkVersementStatement = connection.prepareStatement(QUERY_SELECT_ALL_VERSEMENTS_WHERE_NUMERO);
      checkVersementStatement.setLong(1, id);
      ResultSet resultSetVersement = checkVersementStatement.executeQuery();

      if(resultSetVersement.next()){
        logger.warn("Allocataire {} has versement linked to it", id);
        return "Allocataire " + id + " has versement linked to it, can't delete it.";
      } else {
        logger.info("Allocataire {} has no versement linked to it", id);
        // second, if no versement on Allocataire, delete Allocataire
        logger.info("SQL: {}", QUERY_DELETE_WHERE_NUMERO);
        PreparedStatement deleteStatement = connection.prepareStatement(QUERY_DELETE_WHERE_NUMERO);
        deleteStatement.setLong(1, id);
        int check = deleteStatement.executeUpdate();
        if (check == 0) {
          logger.warn("Allocataire {} not found, error", id);
          return "Allocataire " + id + " not found";
        }
      }
      logger.info("Allocataire {} deleted", id);
      return "Allocataire " + id + " deleted";
    } catch (SQLException e) {
      logger.error("SQL exception", e);
      throw new RuntimeException(e);
    }
  }

  public String updateAllocataireName(String nom, String prenom, int id) {
    // first, check if either name or surname are really different
    // if not, do nothing
    logger.info("Starting updateAllocataire {}, with new nom and prenom", id, nom, prenom);
    Connection connection = activeJDBCConnection();
    try {
      PreparedStatement checkStatement = connection.prepareStatement(QUERY_FIND_WHERE_NUMERO);
      checkStatement.setLong(1, id);
      ResultSet resultSet = checkStatement.executeQuery();
      resultSet.next();
      if (resultSet.getString(2).equals(nom) && resultSet.getString(3).equals(prenom)) {
        logger.info("Found name {}, and surname {}", resultSet.getString(2), resultSet.getString(3));
        logger.info("Allocataire {} name and surname are the same, no need to update", id);
        return "Allocataire " + id + " name and surname are the same, no need to update";
      }
      logger.info("Allocataire {} name and surname are different, updating", id);
      PreparedStatement updateStatement = connection.prepareStatement(QUERY_UPDATE_WHERE_NUMERO);
      updateStatement.setString(1, nom);
      updateStatement.setString(2, prenom);
      updateStatement.setLong(3, id);
      int check = updateStatement.executeUpdate();
        if (check == 0) {
            logger.warn("Allocataire {} not found, error", id);
            return "Allocataire " + id + " not found";
        }
        logger.info("Allocataire {} updated", id);
        return "Allocataire " + id + " updated";
    }catch (SQLException e){
      logger.error("SQL exception", e);
      throw new RuntimeException(e);
    }

  }
}
