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
import org.dbunit.operation.DatabaseOperation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;


public class MyTestsIT {

    private IDatabaseConnection connection;
    IDataSet dataSet;
    Database database;
    Logger logger = LoggerFactory.getLogger(MyTestsIT.class);

    ITable expectedTable;
    ITable actualData;

    AllocationService allocationService;

    @BeforeEach
    public void setup() {
        logger.debug("Setting up");
        // obtain DB connection
        try {
            // start the DB
            database = new Database();
            Migrations migrations = new Migrations(database, true);

            database.start();
            migrations.start();

            // populate the DB with data from xml file
            connection = new DatabaseConnection(Database.activeJDBCConnection());
            dataSet = DataSetTestService.loadDataSet("mydataset.xml");
            logger.info("Content of dataSet : {}", dataSet);
            DatabaseOperation.CLEAN_INSERT.execute(connection, dataSet);

            allocationService = new AllocationService(new AllocataireMapper(), new AllocationMapper());

            logger.info("Set up done");
        } catch (DatabaseUnitException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // ... tests
    @Test
    @DisplayName("Test Delete Allocataire by ID")
    public void deleteAllocataireById_GivenID3And2_ShouldBe2RowsLeft() {
        try {
            // this should remove allocataire 3
            allocationService.deleteAllocataireById(3);
            //this should NOT remove allocataire 2, because it has versement linked to it
            allocationService.deleteAllocataireById(2);
            actualData = connection.createQueryTable("ALLOCATAIRES", "SELECT * FROM ALLOCATAIRES");
            dataSet = DataSetTestService.loadDataSet("testDataDelete.xml"); // test on testDataDelete dataset
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

    @Test
    @DisplayName("Test Update Allocataire's name and surname with ID")
    public void updateAllocataireName_GivenID3_ShouldBe2RowsLeft() {
        try {
            //this should update the name
            allocationService.updateAllocataireName(3, "Name", "New");
            //this shouldn't do anything
            allocationService.updateAllocataireName(2, "Smith", "Jane");
            actualData = connection.createQueryTable("ALLOCATAIRES", "SELECT * FROM ALLOCATAIRES");
            dataSet = DataSetTestService.loadDataSet("testDataUpdate.xml"); // test on testDataDelete dataset
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
