package asia.goldenowl.gscores_api.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import asia.goldenowl.gscores_api.service.IDataSeederService;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);

    private final IDataSeederService dataSeederService;

    @Value("${data.seeder.enabled:true}")
    private boolean seederEnabled;

    @Value("${data.seeder.filepath:dataset/diem_thi_thpt_2024.csv}")
    private String csvFilePath;

    @Override
    public void run(String... args) throws Exception {
        if (seederEnabled) {
            if (dataSeederService.isDatabaseEmpty()) {
                logger.info("Database appears to be empty. Running data seeder...");

                try {
                    dataSeederService.loadDataFromCsv(csvFilePath);
                    logger.info("Data seeder finished successfully.");
                } catch (Exception e) {
                    logger.error("Error during data seeding: ", e);
                }
            } else {
                logger.info("Data seeder: Database is not empty. Skipping seeding.");
            }
        } else {
            logger.info("Data seeder is disabled by configuration.");
        }
    }
}
