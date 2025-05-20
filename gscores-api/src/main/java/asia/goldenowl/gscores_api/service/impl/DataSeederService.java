package asia.goldenowl.gscores_api.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import asia.goldenowl.gscores_api.entity.Score;
import asia.goldenowl.gscores_api.entity.ScoreId;
import asia.goldenowl.gscores_api.entity.Student;
import asia.goldenowl.gscores_api.entity.Subject;
import asia.goldenowl.gscores_api.service.IDataSeederService;

@Service
public class DataSeederService implements IDataSeederService {

    private static final Logger logger = LoggerFactory.getLogger(DataSeederService.class);
    private static final int BATCH_SIZE = 1000;

    @PersistenceContext
    private EntityManager entityManager;

    // Define subject mappings based on CSV headers and desired subject codes/names
    private static final Map<String, String> SUBJECT_HEADER_TO_CODE_MAP = new HashMap<>();
    private static final Map<String, String> SUBJECT_CODE_TO_NAME_MAP = new HashMap<>();

    static {
        // CSV Header -> Subject Code
        SUBJECT_HEADER_TO_CODE_MAP.put("toan", "TOAN");
        SUBJECT_HEADER_TO_CODE_MAP.put("ngu_van", "VAN");
        SUBJECT_HEADER_TO_CODE_MAP.put("ngoai_ngu", "NGOAINGU");
        SUBJECT_HEADER_TO_CODE_MAP.put("vat_li", "LY");
        SUBJECT_HEADER_TO_CODE_MAP.put("hoa_hoc", "HOA");
        SUBJECT_HEADER_TO_CODE_MAP.put("sinh_hoc", "SINH");
        SUBJECT_HEADER_TO_CODE_MAP.put("lich_su", "SU");
        SUBJECT_HEADER_TO_CODE_MAP.put("dia_li", "DIA");
        SUBJECT_HEADER_TO_CODE_MAP.put("gdcd", "GDCD");

        // Subject Code -> Subject Name (Display Name)
        SUBJECT_CODE_TO_NAME_MAP.put("TOAN", "Toán");
        SUBJECT_CODE_TO_NAME_MAP.put("VAN", "Ngữ Văn");
        SUBJECT_CODE_TO_NAME_MAP.put("NGOAINGU", "Ngoại Ngữ");
        SUBJECT_CODE_TO_NAME_MAP.put("LY", "Vật Lý");
        SUBJECT_CODE_TO_NAME_MAP.put("HOA", "Hóa Học");
        SUBJECT_CODE_TO_NAME_MAP.put("SINH", "Sinh Học");
        SUBJECT_CODE_TO_NAME_MAP.put("SU", "Lịch Sử");
        SUBJECT_CODE_TO_NAME_MAP.put("DIA", "Địa Lý");
        SUBJECT_CODE_TO_NAME_MAP.put("GDCD", "Giáo dục công dân");
    }

    @Override
    @Transactional
    public void loadDataFromCsv(String filePath) throws IOException {
        logger.info("Starting data load from CSV: {}. Path should be classpath-relative.", filePath);

        Map<String, Subject> subjectsCache = preloadSubjects();
        logger.info("Subjects preloaded. Cache size: {}", subjectsCache.size());

        CSVFormat csvFormat = CSVFormat.DEFAULT.builder()
                .setHeader()
                .setSkipHeaderRecord(true)
                .setTrim(true)
                .setIgnoreHeaderCase(true)
                .build();

        long totalStudentsProcessed = 0;
        long totalScoresProcessed = 0;
        long currentBatchStudentCount = 0;
        long currentBatchScoreCount = 0;

        try (InputStream inputStream = new ClassPathResource(filePath).getInputStream();
                Reader reader = new InputStreamReader(inputStream);
                CSVParser csvParser = new CSVParser(reader, csvFormat)) {

            List<Student> studentBatch = new ArrayList<>(BATCH_SIZE);
            List<Score> scoreBatch = new ArrayList<>(BATCH_SIZE * SUBJECT_HEADER_TO_CODE_MAP.size());

            for (CSVRecord csvRecord : csvParser) {
                String registrationNumber = csvRecord.get("sbd");
                if (registrationNumber == null || registrationNumber.trim().isEmpty()) {
                    logger.warn("Skipping record number {} due to missing or empty SBD.", csvRecord.getRecordNumber());
                    continue;
                }
                String foreignLanguageCode = csvRecord.isMapped("ma_ngoai_ngu") ? csvRecord.get("ma_ngoai_ngu") : null;

                Student student = new Student(registrationNumber, foreignLanguageCode, new ArrayList<>());
                studentBatch.add(student);
                currentBatchStudentCount++;

                for (Map.Entry<String, String> entry : SUBJECT_HEADER_TO_CODE_MAP.entrySet()) {
                    String headerName = entry.getKey();
                    String subjectCode = entry.getValue();

                    if (csvRecord.isMapped(headerName) && csvRecord.get(headerName) != null
                            && !csvRecord.get(headerName).trim().isEmpty()) {
                        try {
                            float scoreValue = Float.parseFloat(csvRecord.get(headerName).trim());
                            Subject subject = subjectsCache.get(subjectCode);
                            if (subject != null) {
                                ScoreId scoreId = new ScoreId(registrationNumber, subjectCode);
                                Score score = new Score(scoreId, student, subject, scoreValue);
                                scoreBatch.add(score);
                                currentBatchScoreCount++;
                            } else {
                                logger.warn(
                                        "Subject with code '{}' not found in cache. Score for SBD '{}' for this subject will not be saved.",
                                        subjectCode, registrationNumber);
                            }
                        } catch (NumberFormatException e) {
                            logger.warn("Invalid score format for SBD: {}, Subject Header: {}, Value: '{}'",
                                    registrationNumber,
                                    headerName, csvRecord.get(headerName));
                        }
                    }
                }

                if (studentBatch.size() >= BATCH_SIZE) {
                    persistBatchData(studentBatch, scoreBatch);
                    totalStudentsProcessed += currentBatchStudentCount;
                    totalScoresProcessed += currentBatchScoreCount;
                    logger.info("Processed {} students and {} scores so far...", totalStudentsProcessed,
                            totalScoresProcessed);
                    currentBatchStudentCount = 0;
                    currentBatchScoreCount = 0;
                }
            }

            // Persist any remaining data in the last batch
            if (!studentBatch.isEmpty() || !scoreBatch.isEmpty()) {
                persistBatchData(studentBatch, scoreBatch);
                totalStudentsProcessed += currentBatchStudentCount;
                totalScoresProcessed += currentBatchScoreCount;
            }
        }
        logger.info("Finished data load from CSV. Total students processed: {}, Total scores processed: {}",
                totalStudentsProcessed, totalScoresProcessed);
    }

    @Override
    public boolean isDatabaseEmpty() {
        Long studentCount = entityManager
                .createQuery("SELECT COUNT(s) FROM Student s", Long.class)
                .getSingleResult();
        return studentCount == 0L;
    }

    private void persistBatchData(List<Student> studentsToPersist, List<Score> scoresToPersist) {
        if (!studentsToPersist.isEmpty()) {
            logger.info("Persisting batch of {} students", studentsToPersist.size());
            for (Student student : studentsToPersist) {
                entityManager.persist(student);
            }
        }
        if (!scoresToPersist.isEmpty()) {
            logger.info("Persisting batch of {} scores", scoresToPersist.size());
            for (Score score : scoresToPersist) {
                entityManager.persist(score);
            }
        }
        logger.debug("Flushing EntityManager after batch persist.");
        entityManager.flush();

        for (Student student : studentsToPersist) {
            entityManager.detach(student);
        }
        for (Score score : scoresToPersist) {
            entityManager.detach(score);
        }

        studentsToPersist.clear();
        scoresToPersist.clear();
    }

    private Map<String, Subject> preloadSubjects() {
        List<Subject> existingSubjects = entityManager
                .createQuery("SELECT s FROM Subject s", Subject.class)
                .getResultList();

        Map<String, Subject> subjectsMap = existingSubjects.stream()
                .collect(Collectors.toMap(Subject::getSubjectCode, subject -> subject));

        List<Subject> newSubjectsToPersist = new ArrayList<>();
        for (Map.Entry<String, String> entry : SUBJECT_CODE_TO_NAME_MAP.entrySet()) {
            String code = entry.getKey();
            String name = entry.getValue();
            if (!subjectsMap.containsKey(code)) {
                Subject newSubject = new Subject(code, name, new ArrayList<>());
                newSubjectsToPersist.add(newSubject);
                subjectsMap.put(code, newSubject);
            }
        }

        if (!newSubjectsToPersist.isEmpty()) {
            logger.info("Persisting {} new subjects.", newSubjectsToPersist.size());
            for (Subject subjectToPersist : newSubjectsToPersist) {
                entityManager.persist(subjectToPersist);
            }
        }
        return subjectsMap;
    }

}
