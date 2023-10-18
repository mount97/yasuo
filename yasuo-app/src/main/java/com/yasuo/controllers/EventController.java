package com.yasuo.controllers;

import com.yasuo.models.Event;
import com.yasuo.services.common.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class EventController {
    private final EventService eventService;

    @QueryMapping(value = "findAll")
    @PreAuthorize("hasAuthority('USER')")
    public List<Event> getAll() {
        return eventService.findAll();
    }
}
