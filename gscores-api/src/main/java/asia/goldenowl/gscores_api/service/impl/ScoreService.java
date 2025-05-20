package asia.goldenowl.gscores_api.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import asia.goldenowl.gscores_api.dto.ScoreDto;
import asia.goldenowl.gscores_api.dto.StudentDetailsDto;
import asia.goldenowl.gscores_api.entity.Student;
import asia.goldenowl.gscores_api.exception.ResourceNotFoundException;
import asia.goldenowl.gscores_api.repository.StudentRepository;
import asia.goldenowl.gscores_api.service.IScoreService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ScoreService implements IScoreService {

    private final StudentRepository studentRepository;

    @Transactional(readOnly = true)
    public StudentDetailsDto getScoresByRegistrationNumber(String registrationNumber) {
        Student student = studentRepository.findById(registrationNumber)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Student not found with registration number: " + registrationNumber));

        List<ScoreDto> scoreDtos = student.getScores().stream()
                .map(score -> new ScoreDto(
                        score.getSubject().getSubjectName(),
                        score.getSubject().getSubjectCode(),
                        score.getScore()))
                .collect(Collectors.toList());

        return new StudentDetailsDto(student.getRegistrationNumber(), student.getForeignLanguageCode(),
                scoreDtos);
    }

}
