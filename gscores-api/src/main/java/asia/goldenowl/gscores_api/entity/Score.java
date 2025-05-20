package asia.goldenowl.gscores_api.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "scores")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Score {

    @EmbeddedId
    private ScoreId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("registrationNumber")
    @JoinColumn(name = "registration_number")
    private Student student;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("subjectCode")
    @JoinColumn(name = "subject_code")
    private Subject subject;

    @Column(name = "score")
    private Float score;

}
