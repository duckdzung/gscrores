package asia.goldenowl.gscores_api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentGroupScoreDto {

    private String registrationNumber;

    private Float totalScore;

    private Map<String, Float> individualScores;

}
