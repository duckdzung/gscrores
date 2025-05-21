package asia.goldenowl.gscores_api.dto;

import java.util.HashMap;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SubjectDistributionResultDto {
    private String subjectCode;
    private Map<String, Long> distribution;

    public static SubjectDistributionResultDto fromQueryResult(Object[] result) {
        String subjectCode = (String) result[0];
        Map<String, Long> distribution = new HashMap<>();
        distribution.put("level1", (Long) result[1]); // >=8
        distribution.put("level2", (Long) result[2]); // >=6 and <8
        distribution.put("level3", (Long) result[3]); // >=4 and <6
        distribution.put("level4", (Long) result[4]); // <4
        return new SubjectDistributionResultDto(subjectCode, distribution);
    }
}
