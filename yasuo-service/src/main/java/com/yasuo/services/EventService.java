package com.yasuo.services;

import com.yasuo.models.Event;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventService {
    List<Event> findAll();
}
