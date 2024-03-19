package ch.hearc.cafheg.infrastructure.persistance;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.function.Supplier;
import javax.sql.DataSource;

public class Database {
  /** Pool de connections JDBC */
  private static DataSource dataSource;

  static Logger logger = LoggerFactory.getLogger(Database.class);

  /** Connection JDBC active par utilisateur/thread (ThreadLocal) */
  private static final ThreadLocal<Connection> connection = new ThreadLocal<>();

  /**
   * Retourne la transaction active ou throw une Exception si pas de transaction
   * active.
   * @return Connection JDBC active
   */
  static Connection activeJDBCConnection() {
    if(connection.get() == null) {
      RuntimeException e = new RuntimeException("Pas de connection JDBC active");
      logger.error("Pas de connection JDBC active", e);
      throw e;
    }
    return connection.get();
  }

  /**
   * Exécution d'une fonction dans une transaction.
   * @param inTransaction La fonction a éxécuter au travers d'une transaction
   * @param <T> Le type du retour de la fonction
   * @return Le résultat de l'éxécution de la fonction
   */
  public static <T> T inTransaction(Supplier<T> inTransaction) {
    logger.info("Transaction start");
    try {
      logger.info("Transaction : getConnection");
      connection.set(dataSource.getConnection());
      return inTransaction.get();
    } catch (Exception e) {
      throw new RuntimeException(e);
    } finally {
      try {
        logger.info("Transaction, close connection");
        connection.get().close();
      } catch (SQLException e) {
        logger.error("SQL exception", e);
        throw new RuntimeException(e);
      }
      logger.info("Transaction end");
      connection.remove();
    }
  }

  DataSource dataSource() {
    return dataSource;
  }

  /**
   * Initialisation du pool de connections.
   */
  public void start() {
    System.out.println("Initializing datasource");
    HikariConfig config = new HikariConfig();
    config.setJdbcUrl("jdbc:h2:mem:sample");
    config.setMaximumPoolSize(20);
    config.setDriverClassName("org.h2.Driver");
    dataSource = new HikariDataSource(config);
    System.out.println("Datasource initialized");
  }
}
