package com.yasuo.datafetchers;

import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsQuery;
import com.yasuo.services.common.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;

@DgsComponent
@RequiredArgsConstructor
public class EventDataFetcher {
    private final EventService eventService;

    @DgsQuery(field = "findAll")
    @PreAuthorize("hasAuthority('USER')")
    public Object getAll() {
        return eventService.findAll();
    }
}
