package asia.goldenowl.gscores_api.entity;

import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ScoreId implements Serializable {

    @Column(name = "registration_number")
    private String registrationNumber;

    @Column(name = "subject_code")
    private String subjectCode;

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        ScoreId scoreId = (ScoreId) o;
        return Objects.equals(registrationNumber, scoreId.registrationNumber) &&
                Objects.equals(subjectCode, scoreId.subjectCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(registrationNumber, subjectCode);
    }

}
