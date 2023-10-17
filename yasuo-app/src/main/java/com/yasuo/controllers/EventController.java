package com.yasuo.controllers;

import com.yasuo.models.Event;
import com.yasuo.services.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class EventController {
    @Autowired
    private EventService eventService;

    @QueryMapping(value = "findAll")
    public List<Event> getAll(){
        return eventService.findAll();
    }
}
