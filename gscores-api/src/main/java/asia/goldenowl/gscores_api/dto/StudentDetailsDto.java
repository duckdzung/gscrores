package asia.goldenowl.gscores_api.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentDetailsDto {

    private String registrationNumber;

    private String foreignLanguageCode;

    private List<ScoreDto> scores;

}
