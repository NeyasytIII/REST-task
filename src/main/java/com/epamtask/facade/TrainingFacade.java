package com.epamtask.facade;


import com.epamtask.dto.trainingdto.TrainingCreateRequestDto;
import com.epamtask.model.Trainee;
import com.epamtask.model.Trainer;
import com.epamtask.model.Training;
import com.epamtask.model.TrainingType;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface TrainingFacade {
    void createTraining(Long traineeId, Long trainerId, String trainingName, TrainingType type, Date trainingDate, String trainingDuration);
    Optional<Training> getTrainingById(Long trainingId);
    Map<Long, Training> getTrainingsByTrainerId(Long trainerId);
    Map<Long, Training> getTrainingsByTraineeId(Long traineeId);
    List<Training> getAllTrainings();
    void createTrainingFromDto(TrainingCreateRequestDto dto);
    List<Training> getTrainingsByTraineeUsernameAndCriteria(String traineeUsername, Date fromDate, Date toDate, String trainerName, String trainingType);
    List<Training> getTrainingsByTrainerUsernameAndCriteria(String trainerUsername, Date fromDate, Date toDate, String traineeName);
}