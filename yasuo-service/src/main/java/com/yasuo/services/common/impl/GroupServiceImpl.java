package com.yasuo.services.common.impl;

import com.yasuo.dtos.common.CreateOrUpdateGroupRequest;
import com.yasuo.dtos.common.GroupDto;
import com.yasuo.dtos.common.GroupPropertiesDto;
import com.yasuo.dtos.common.ResponseData;
import com.yasuo.enums.GroupStatus;
import com.yasuo.enums.ImageType;
import com.yasuo.exceptions.YasuoException;
import com.yasuo.models.Group;
import com.yasuo.models.Image;
import com.yasuo.models.User;
import com.yasuo.pojo.GroupProperties;
import com.yasuo.repository.GroupRepository;
import com.yasuo.repository.ImageRepository;
import com.yasuo.services.auth.AuthenticationService;
import com.yasuo.services.common.GroupService;
import com.yasuo.services.mappers.GroupMapper;
import com.yasuo.services.utils.ImageUtils;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GroupServiceImpl implements GroupService {
    private static final Logger logger = LoggerFactory.getLogger(GroupServiceImpl.class);
    private final GroupRepository groupRepository;
    private final ImageRepository imageRepository;
    private final AuthenticationService authenticationService;
    private final GroupMapper groupMapper;

    @Override
    public ResponseData<GroupDto> createOrUpdateGroup(CreateOrUpdateGroupRequest request, MultipartFile file) {
        try {
            User currentUser = authenticationService.getCurrentUser();
            Group group = groupRepository.findById(request.getId()).orElse(null);
            Group savedGroup;
            boolean isInsertAction = false;
            if (StringUtils.isEmpty(request.getId()) || Objects.isNull(group)) {
                isInsertAction = true;
                group = new Group();
                group.setAdminIds(Collections.singletonList(currentUser.getId()));
                group.setCreatedBy(currentUser.getId());
                mergeGroup(group, request, file);
//                imageRepository.insert(group.getGroupProperties().getAvatar());
//                savedGroup = groupRepository.save(group);
//                return ResponseData.<GroupDto>builder()
//                        .code(201)
//                        .message("Created group successfully")
//                        .responseData(groupMapper.toDto(savedGroup))
//                        .build();
            }
            mergeGroup(group, request, file);
            imageRepository.save(group.getGroupProperties().getAvatar());
            savedGroup = groupRepository.save(group);
            return ResponseData.<GroupDto>builder()
                    .code(200)
                    .message(isInsertAction ? "Created group successfully" : "Updated group successfully")
                    .responseData(groupMapper.toDto(savedGroup))
                    .build();
        } catch (Exception ex) {
            logger.error("Failed to create or update group: {}", ex.getMessage());
            throw ex;
        }
    }

    @Override
    public ResponseData<List<GroupDto>> searchGroupByName(String name) {
        return new ResponseData<>(200, "Filtered group", groupRepository.searchGroupByName(name).stream().map(groupMapper::toDto).collect(Collectors.toList()));
    }

    @Override
    public ResponseData<List<GroupDto>> fetchAllGroup() {
        return new ResponseData<>(200, "Get all groups successfully", groupRepository.findAll().stream().map(groupMapper::toDto).collect(Collectors.toList()));
    }

    @Override
    public ResponseData<Void> deleteGroup(String groupId) {
        Group group = groupRepository.findById(groupId).orElse(null);
        if (group == null) {
            return new ResponseData<>(404, "Group not found with id: " + groupId, null);
        }
        groupRepository.delete(group);
        return new ResponseData<>(200, "Deleted group successfully", null);
    }

    @Override
    public ResponseData<GroupDto> uploadGroupAvatar(String groupId, MultipartFile file) {
        Group group = groupRepository.findById(groupId).orElseThrow(() -> new YasuoException("Group not found with id: " + groupId));
        validateImage(file);
        Image image = imageRepository.findByName(file.getOriginalFilename());
        if (Objects.isNull(image)) {
            image = saveImage(file);
        }
        group.getGroupProperties().setAvatar(image);
        return new ResponseData<>(200, "Upload avatar successfully", groupMapper.toDto(groupRepository.save(group)));
    }

    @Override
    public ResponseData<GroupDto> uploadGroupBackground(String groupId, MultipartFile file) {
        Group group = groupRepository.findById(groupId).orElseThrow(() -> new YasuoException("Group not found with id: " + groupId));
        validateImage(file);
        Image image = imageRepository.findByName(file.getOriginalFilename());
        if (Objects.isNull(image)) {
            image = saveImage(file);
        }
        group.getGroupProperties().setBackground(image);
        return new ResponseData<>(200, "Upload background successfully", groupMapper.toDto(groupRepository.save(group)));
    }

    private Image saveImage(MultipartFile file) {
        Image image = imageRepository.findByName(file.getOriginalFilename());
        if (Objects.isNull(image)) {
            image = new Image();
            image.setName(file.getOriginalFilename());
            ImageType imageType = ImageType.getByType(file.getContentType());
            image.setType(imageType);
            imageRepository.insert(image);
        }
        return image;
    }

    private void mergeGroup(Group group, CreateOrUpdateGroupRequest request, MultipartFile file) {
        group.setName(request.getName());
        mergeGroupProperties(group, group.getGroupProperties(), request.getGroupProperties(), file);
    }

    private void mergeGroupProperties(Group group, GroupProperties groupProperties, GroupPropertiesDto request, MultipartFile file) {
        Image image = new Image();
        image.setName(file.getOriginalFilename());
        ImageType imageType = ImageType.getByType(file.getContentType());
        image.setType(imageType);
        groupProperties.setAvatar(image);
        if (StringUtils.isNotEmpty(request.getStatus())) {
            GroupStatus groupStatus = groupProperties.getStatus();
            try {
                groupStatus = GroupStatus.valueOf(request.getStatus());
            } catch (IllegalArgumentException ex) {
                groupStatus = Objects.nonNull(groupStatus) ? groupStatus : GroupStatus.PUBLIC;
            }
            groupProperties.setStatus(groupStatus);
        }
        group.setGroupProperties(groupProperties);
    }

    private void validateImage(MultipartFile file) {
        if (Objects.isNull(file) || ImageUtils.isNotImage(file)) {
            throw new YasuoException("File must be image type, only PNG/JPG/JPEG accepted");
        }
    }
}
