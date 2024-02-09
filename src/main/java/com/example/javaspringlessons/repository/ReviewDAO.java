package com.example.javaspringlessons.repository;

import com.example.javaspringlessons.entity.Review;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewDAO extends MongoRepository<Review, ObjectId> {
}
