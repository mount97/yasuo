package com.yasuo.services.common;

import com.yasuo.dtos.common.CreateOrUpdateGroupRequest;
import com.yasuo.dtos.common.GroupDto;
import com.yasuo.dtos.common.ResponseData;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Repository
public interface GroupService {
    ResponseData<GroupDto> createOrUpdateGroup(CreateOrUpdateGroupRequest request, MultipartFile file);
    ResponseData<List<GroupDto>> searchGroupByName(String name);
    ResponseData<List<GroupDto>> fetchAllGroup();
    ResponseData<Void> deleteGroup(String groupId);
    ResponseData<GroupDto> uploadGroupAvatar(String groupId, MultipartFile file);
    ResponseData<GroupDto> uploadGroupBackground(String groupId, MultipartFile file);
}
