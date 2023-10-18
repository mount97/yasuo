package com.yasuo.services.common.impl;

import com.yasuo.models.Event;
import com.yasuo.repository.EventRepository;
import com.yasuo.services.common.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {
    private final EventRepository eventRepository;

    @Override
    public List<Event> findAll() {
        return eventRepository.findAll();
    }
}
