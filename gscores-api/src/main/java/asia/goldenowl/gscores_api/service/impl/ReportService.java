package asia.goldenowl.gscores_api.service.impl;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import asia.goldenowl.gscores_api.dto.StudentGroupScoreDto;
import asia.goldenowl.gscores_api.dto.SubjectDistributionResultDto;
import asia.goldenowl.gscores_api.dto.SubjectStatsDto;
import asia.goldenowl.gscores_api.entity.Student;
import asia.goldenowl.gscores_api.entity.Subject;
import asia.goldenowl.gscores_api.repository.ScoreRepository;
import asia.goldenowl.gscores_api.repository.StudentRepository;
import asia.goldenowl.gscores_api.repository.SubjectRepository;
import asia.goldenowl.gscores_api.service.IReportService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReportService implements IReportService {

        private final SubjectRepository subjectRepository;
        private final ScoreRepository scoreRepository;
        private final StudentRepository studentRepository;

        @Transactional(readOnly = true)
        public List<SubjectStatsDto> getScoreStatistics() {
                // Get all subjects to have a lookup for subject names
                Map<String, Subject> subjectMap = subjectRepository.findAll()
                                .stream()
                                .collect(Collectors.toMap(Subject::getSubjectCode, subject -> subject));

                // Get all score distributions in a single query
                List<Object[]> distributionResults = scoreRepository.getScoreDistributionForAllSubjects();

                // Convert results to DTOs
                return distributionResults.stream()
                                .map(result -> {
                                        SubjectDistributionResultDto distributionDto = SubjectDistributionResultDto
                                                        .fromQueryResult(result);
                                        String subjectCode = distributionDto.getSubjectCode();
                                        Subject subject = subjectMap.get(subjectCode);
                                        return new SubjectStatsDto(
                                                        subjectCode,
                                                        subject != null ? subject.getSubjectName() : "Unknown Subject",
                                                        distributionDto.getDistribution());
                                })
                                .collect(Collectors.toList());
        }

        @Transactional(readOnly = true)
        public List<StudentGroupScoreDto> getTopStudentsGroupA(int limit) {
                List<String> groupASubjectCodes = Arrays.asList("TOAN", "LY", "HOA");
                Pageable pageable = PageRequest.of(0, limit);

                // Get top students by total score
                List<Object[]> results = studentRepository.findTopStudentsBySubjectGroupSumWithScores(
                                groupASubjectCodes,
                                groupASubjectCodes.size(), pageable);

                if (results.isEmpty()) {
                        return List.of();
                }

                // Extract student registration numbers
                List<String> regNumbers = results.stream()
                                .map(result -> ((Student) result[0]).getRegistrationNumber())
                                .collect(Collectors.toList());

                // Get all scores for these students in a single query
                List<Object[]> scoreResults = studentRepository.findScoresForStudentsAndSubjects(
                                regNumbers, groupASubjectCodes);

                // Build a map of student registration number to their subject scores
                Map<String, Map<String, Float>> studentScoresMap = new HashMap<>();

                // Initialize score maps for each student
                for (String regNumber : regNumbers) {
                        studentScoresMap.put(regNumber, new HashMap<>());
                }

                // Populate the scores
                for (Object[] scoreResult : scoreResults) {
                        String regNumber = (String) scoreResult[0];
                        String subjectCode = (String) scoreResult[1];
                        Float score = (Float) scoreResult[2];

                        studentScoresMap.get(regNumber).put(subjectCode, score);
                }

                // Create the final DTOs
                return results.stream()
                                .map(result -> {
                                        Student student = (Student) result[0];
                                        Double totalScore = (Double) result[1];
                                        String regNumber = student.getRegistrationNumber();

                                        return new StudentGroupScoreDto(
                                                        regNumber,
                                                        totalScore.floatValue(),
                                                        studentScoresMap.get(regNumber));
                                })
                                .collect(Collectors.toList());
        }

}
