package asia.goldenowl.gscores_api.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import asia.goldenowl.gscores_api.entity.Student;

@Repository
public interface StudentRepository extends JpaRepository<Student, String> {

        Optional<Student> findByRegistrationNumber(String registrationNumber);

        @Query("SELECT s, SUM(sc.score) as totalScore " +
                        "FROM Student s JOIN s.scores sc JOIN sc.subject subj " +
                        "WHERE subj.subjectCode IN :subjectCodes " +
                        "GROUP BY s.registrationNumber " +
                        "HAVING COUNT(DISTINCT subj.subjectCode) = :numSubjects " +
                        "ORDER BY totalScore DESC")
        List<Object[]> findTopStudentsBySubjectGroupSumWithScores(@Param("subjectCodes") List<String> subjectCodes,
                        @Param("numSubjects") int numSubjects,
                        Pageable pageable);

        @Query("SELECT s.registrationNumber as regNum, sc.subject.subjectCode as subjectCode, sc.score as score " +
                        "FROM Student s JOIN s.scores sc " +
                        "WHERE s.registrationNumber IN :regNumbers AND sc.subject.subjectCode IN :subjectCodes")
        List<Object[]> findScoresForStudentsAndSubjects(
                        @Param("regNumbers") List<String> registrationNumbers,
                        @Param("subjectCodes") List<String> subjectCodes);
}
