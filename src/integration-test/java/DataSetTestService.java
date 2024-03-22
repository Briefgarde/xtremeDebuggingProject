import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DataSetTestService {
    private static final Logger logger = LoggerFactory.getLogger(DataSetTestService.class);

    public static IDataSet loadDataSet(String name){
        try {
            logger.debug("Loading dataset {}", name);
            return new FlatXmlDataSetBuilder().build(DataSetTestService.class.getClassLoader().getResourceAsStream(name));
        } catch (DataSetException e) {
            logger.error("Error loading dataset", e);
            throw new RuntimeException(e);
        }
    }
}
