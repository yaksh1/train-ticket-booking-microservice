package com.yaksh.trainms.train.repository;

import com.yaksh.trainms.train.model.Train;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * TrainRepositoryV2 interface acts as a repository for Train entities.
 * It extends the MongoRepository interface provided by Spring Data MongoDB.
 * This interface provides CRUD operations and additional query methods for Train objects.
 */
public interface TrainRepositoryV2 extends MongoRepository<Train, String> {
    // No custom methods are defined here. The repository inherits basic CRUD and query operations from MongoRepository.
}