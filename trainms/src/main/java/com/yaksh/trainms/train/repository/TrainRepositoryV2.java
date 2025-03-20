package com.yaksh.trainms.train.repository;

import com.yaksh.trainms.train.model.Train;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TrainRepositoryV2 extends MongoRepository<Train,String> {
}
