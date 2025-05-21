package asia.goldenowl.gscores_api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import asia.goldenowl.gscores_api.dto.ApiResponse;
import asia.goldenowl.gscores_api.dto.StudentDetailsDto;
import asia.goldenowl.gscores_api.service.IScoreService;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/scores")
@CrossOrigin(origins = "*")
@Validated
@RequiredArgsConstructor
public class ScoreController {

    private final IScoreService scoreService;

    @GetMapping("/{registrationNumber}")
    public ResponseEntity<ApiResponse<StudentDetailsDto>> getScoresByRegistrationNumber(
            @PathVariable @NotBlank(message = "Registration number must be not blank") String registrationNumber) {
        StudentDetailsDto student = scoreService.getScoresByRegistrationNumber(registrationNumber);
        return ResponseEntity.ok(ApiResponse.success(student));
    }

}
