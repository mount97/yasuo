package com.yasuo.repository;

import com.yasuo.models.Group;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GroupRepository extends MongoRepository<Group, String> {
    @Query(value = "{'name':{ '$regex' : '?0' , '$options' : 'i'}}")
    List<Group> searchGroupByName(String name);
}
