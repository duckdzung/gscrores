package asia.goldenowl.gscores_api.service;

import java.io.IOException;

public interface IDataSeederService {

    void loadDataFromCsv(String filePath) throws IOException;

    boolean isDatabaseEmpty();

}
