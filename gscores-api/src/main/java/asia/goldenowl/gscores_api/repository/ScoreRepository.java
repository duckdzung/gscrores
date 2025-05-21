package asia.goldenowl.gscores_api.repository;

import asia.goldenowl.gscores_api.entity.Score;
import asia.goldenowl.gscores_api.entity.ScoreId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface ScoreRepository extends JpaRepository<Score, ScoreId> {

    List<Score> findByStudentRegistrationNumber(String registrationNumber);

    @Query("SELECT " +
            "SUM(CASE WHEN sc.score >= 8 THEN 1 ELSE 0 END) as level1, " +
            "SUM(CASE WHEN sc.score < 8 AND sc.score >= 6 THEN 1 ELSE 0 END) as level2, " +
            "SUM(CASE WHEN sc.score < 6 AND sc.score >= 4 THEN 1 ELSE 0 END) as level3, " +
            "SUM(CASE WHEN sc.score < 4 THEN 1 ELSE 0 END) as level4 " +
            "FROM Score sc WHERE sc.subject.subjectCode = :subjectCode")
    Map<String, Long> getScoreDistributionForSubject(@Param("subjectCode") String subjectCode);

    @Query("SELECT sc.subject.subjectCode as subjectCode, " +
            "SUM(CASE WHEN sc.score >= 8 THEN 1 ELSE 0 END) as level1, " +
            "SUM(CASE WHEN sc.score < 8 AND sc.score >= 6 THEN 1 ELSE 0 END) as level2, " +
            "SUM(CASE WHEN sc.score < 6 AND sc.score >= 4 THEN 1 ELSE 0 END) as level3, " +
            "SUM(CASE WHEN sc.score < 4 THEN 1 ELSE 0 END) as level4 " +
            "FROM Score sc " +
            "GROUP BY sc.subject.subjectCode")
    List<Object[]> getScoreDistributionForAllSubjects();

}
