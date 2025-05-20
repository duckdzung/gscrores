package asia.goldenowl.gscores_api.service;

import java.util.List;

import asia.goldenowl.gscores_api.dto.StudentGroupScoreDto;
import asia.goldenowl.gscores_api.dto.SubjectStatsDto;

public interface IReportService {

    List<SubjectStatsDto> getScoreStatistics();

    List<StudentGroupScoreDto> getTopStudentsGroupA(int limit);

}
