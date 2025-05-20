
package asia.goldenowl.gscores_api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubjectStatsDto {

    private String subjectCode;

    private String subjectName;

    private Map<String, Long> scoreDistribution;

}
