package asia.goldenowl.gscores_api.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import asia.goldenowl.gscores_api.entity.Subject;

@Repository
public interface SubjectRepository extends JpaRepository<Subject, String> {

    Optional<Subject> findBySubjectCode(String subjectCode);

}
