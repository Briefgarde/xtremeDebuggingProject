import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;

public class DataSetTestService {

    public static IDataSet loadDataSet(String name){
        try {
            return new FlatXmlDataSetBuilder().build(DataSetTestService.class.getClassLoader().getResourceAsStream(name));
        } catch (DataSetException e) {
            throw new RuntimeException(e);
        }
    }
}
