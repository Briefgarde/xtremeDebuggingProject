import ch.hearc.cafheg.business.allocations.AllocationService;
import ch.hearc.cafheg.infrastructure.persistance.AllocataireMapper;
import ch.hearc.cafheg.infrastructure.persistance.AllocationMapper;
import ch.hearc.cafheg.infrastructure.persistance.Database;
import ch.hearc.cafheg.infrastructure.persistance.Migrations;
import org.dbunit.Assertion;
import org.dbunit.DatabaseUnitException;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ITable;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.DatabaseOperation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;


public class MyTestsIT {

    private IDatabaseConnection connection;
    IDataSet dataSet;

    Database database;
    Logger logger = LoggerFactory.getLogger(MyTestsIT.class);

    @BeforeEach
    public void setup() {
        logger.debug("Setting up");
        // obtain DB connection
        try {
            // start the DB
            database = new Database();
            Migrations migrations = new Migrations(database);
            migrations.setForTest(true);
            database.start();
            migrations.start();

            // populate the DB with data from xml file
            connection = new DatabaseConnection(Database.activeJDBCConnection());
            dataSet = new FlatXmlDataSetBuilder().build(getClass().getClassLoader().getResourceAsStream("mydataset.xml"));
            logger.info("Content of dataSet : {}", dataSet);
            DatabaseOperation.CLEAN_INSERT.execute(connection, dataSet);
            logger.info("Set up done");
        } catch (DatabaseUnitException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // ... tests
    @Test
    public void deleteAllocataireById_GivenID3_ShouldBe2RowsLeft() {
        ITable expectedTable;
        ITable actualData = null;
        AllocationService allocationService = new AllocationService(new AllocataireMapper(), new AllocationMapper());
        try {
            allocationService.deleteAllocataireById(3);
            actualData = connection.createQueryTable("ALLOCATAIRES", "SELECT * FROM ALLOCATAIRES");
            expectedTable = dataSet.getTable("ALLOCATAIRES");
        } catch (DataSetException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        try {
            Assertion.assertEquals(expectedTable, actualData);
        } catch (DatabaseUnitException e) {
            throw new RuntimeException(e);
        }
    }
}
