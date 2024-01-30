package com.yasuo.datafetchers;

import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsMutation;
import com.netflix.graphql.dgs.DgsQuery;
import com.netflix.graphql.dgs.InputArgument;
import com.yasuo.dtos.common.CreateOrUpdateGroupRequest;
import com.yasuo.dtos.common.GroupDto;
import com.yasuo.dtos.common.ResponseData;
import com.yasuo.services.common.GroupService;
import graphql.schema.DataFetchingEnvironment;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@DgsComponent
@RequiredArgsConstructor
public class GroupDataFetcher {
    private final GroupService groupService;

    @DgsMutation
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public ResponseData<GroupDto> createOrUpdateGroup(@InputArgument CreateOrUpdateGroupRequest request, DataFetchingEnvironment dfe) {
        MultipartFile file = dfe.getArgument("avatarFile");
        return groupService.createOrUpdateGroup(request, file);
    }

    @DgsMutation
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public ResponseData<GroupDto> uploadAvatarGroup(@InputArgument String groupId, DataFetchingEnvironment dfe) {
        MultipartFile avatarFile = dfe.getArgument("imageFile");
        return groupService.uploadGroupAvatar(groupId, avatarFile);
    }

    @DgsMutation
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public ResponseData<GroupDto> uploadBackGroundGroup(@InputArgument String groupId, DataFetchingEnvironment dfe) {
        MultipartFile backgroundFile = dfe.getArgument("backgroundFile");
        return groupService.uploadGroupBackground(groupId, backgroundFile);
    }

    @DgsQuery(field = "groups")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public ResponseData<List<GroupDto>> fetchAllGroup() {
        return groupService.fetchAllGroup();
    }

    @DgsQuery
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public ResponseData<List<GroupDto>> searchGroupByName(@InputArgument String name) {
        return groupService.searchGroupByName(name);
    }

    @DgsMutation
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseData<Void> deleteGroup(@InputArgument String groupId) {
        return groupService.deleteGroup(groupId);
    }
}
