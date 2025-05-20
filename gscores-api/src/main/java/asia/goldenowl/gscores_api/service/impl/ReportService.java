package asia.goldenowl.gscores_api.service.impl;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import asia.goldenowl.gscores_api.dto.StudentGroupScoreDto;
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
        List<Subject> subjects = subjectRepository.findAll();
        return subjects.stream()
                .map(subject -> {
                    Map<String, Long> distribution = scoreRepository
                            .getScoreDistributionForSubject(subject.getSubjectCode());
                    return new SubjectStatsDto(subject.getSubjectCode(), subject.getSubjectName(),
                            distribution);
                })
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<StudentGroupScoreDto> getTopStudentsGroupA(int limit) {
        List<String> groupASubjectCodes = Arrays.asList("TOAN", "LY", "HOA");
        Pageable pageable = PageRequest.of(0, limit);

        List<Object[]> results = studentRepository.findTopStudentsBySubjectGroupSumWithScores(
                groupASubjectCodes,
                groupASubjectCodes.size(), pageable);

        return results.stream()
                .map(result -> {
                    Student student = (Student) result[0];
                    Double totalScore = (Double) result[1];

                    Map<String, Float> individualScores = student.getScores().stream()
                            .filter(score -> groupASubjectCodes
                                    .contains(score.getSubject().getSubjectCode()))
                            .collect(Collectors.toMap(
                                    score -> score.getSubject().getSubjectCode(),
                                    score -> score.getScore()));

                    return new StudentGroupScoreDto(student.getRegistrationNumber(),
                            totalScore.floatValue(),
                            individualScores);
                })
                .collect(Collectors.toList());
    }

}
