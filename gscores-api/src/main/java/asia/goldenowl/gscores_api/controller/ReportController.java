package asia.goldenowl.gscores_api.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import asia.goldenowl.gscores_api.dto.ApiResponse;
import asia.goldenowl.gscores_api.dto.StudentGroupScoreDto;
import asia.goldenowl.gscores_api.dto.SubjectStatsDto;
import asia.goldenowl.gscores_api.service.IReportService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
public class ReportController {

    private final IReportService reportService;

    @GetMapping("/statistics")
    public ResponseEntity<ApiResponse<List<SubjectStatsDto>>> getScoreStatistics() {
        List<SubjectStatsDto> statistics = reportService.getScoreStatistics();
        return ResponseEntity.ok(ApiResponse.success(statistics));
    }

    @GetMapping("/top-A")
    public ResponseEntity<ApiResponse<List<StudentGroupScoreDto>>> getTopAGroupStudents(
            @RequestParam(defaultValue = "10") int limit) {
        List<StudentGroupScoreDto> topStudents = reportService.getTopStudentsGroupA(limit);
        return ResponseEntity.ok(ApiResponse.success(topStudents));
    }
}
