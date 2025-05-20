package asia.goldenowl.gscores_api.service;

import asia.goldenowl.gscores_api.dto.StudentDetailsDto;

public interface IScoreService {

    StudentDetailsDto getScoresByRegistrationNumber(String registrationNumber);

}
