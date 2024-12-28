package com.shepherd.sheps_project.services.userService;

import com.shepherd.sheps_project.data.dtos.responses.PaginatedResponse;
import com.shepherd.sheps_project.data.dtos.responses.UserResponse;
import com.shepherd.sheps_project.data.models.User;
import com.shepherd.sheps_project.data.repository.UserRepository;
import com.shepherd.sheps_project.exceptions.ResourceNotFoundException;
import com.shepherd.sheps_project.utils.AppUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService{
    private final UserRepository userRepository;
    private static final int NUMBER_OF_ITEMS_PER_PAGE = 10;

    @Override
    public UserResponse getAuthenticatedUser() {
        return mapToUserResponse(AppUtils.getCurrentUser());
    }

    @Override
    public UserResponse getUserById(String id) {
        log.info("::::: Fetching a user :::::");
        return userRepository.findById(id)
                .map(this::mapToUserResponse)
                .orElseThrow(()-> new ResourceNotFoundException("User with the provided id not found"));
    }

    @Override
    public PaginatedResponse<UserResponse> getAllUsers(int pageNumber) {
        log.info("::::: Fetching all users :::::");
        Pageable pageable = findAllUsersPageRequest(pageNumber);
        Page<User> users = userRepository.findAll(pageable);
        return buildPaginatedResponse(users);
    }

    private Pageable findAllUsersPageRequest(int pageNumber){
        return AppUtils.createPageRequest(pageNumber, NUMBER_OF_ITEMS_PER_PAGE, "createdAt", Sort.Direction.DESC);
    }


    @Override
    public PaginatedResponse<UserResponse> getAllEnabledUsers(int pageNumber) {
        log.info("::::: Fetching all enabled users :::::");
        return getUserByStatus(true, pageNumber);
    }

    @Override
    public PaginatedResponse<UserResponse> getAllDisabledUsers(int pageNumber) {
        log.info("::::: Fetching all disabled users :::::");
        return getUserByStatus(false, pageNumber);
    }

    private PaginatedResponse<UserResponse> getUserByStatus(boolean status, int pageNumber){
        Pageable pageable = findAllUsersPageRequest(pageNumber);
        Page<User> users = userRepository.findByEnabled(status, pageable);
        return buildPaginatedResponse(users);
    }

    private PaginatedResponse<UserResponse> buildPaginatedResponse(Page<User> users) {
        List<UserResponse> content = users.isEmpty() ? Collections.emptyList() :
            users.stream().map(this::mapToUserResponse).toList();

        return PaginatedResponse.<UserResponse>builder()
            .content(content)
            .totalPages(users.getTotalPages())
            .totalElements(users.getTotalElements())
            .last(users.isLast())
            .build();
    }

    private UserResponse mapToUserResponse(User user){
        return UserResponse.builder()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .roles(user.getRoles())
                .gender(user.getGender())
                .isEnabled(user.isEnabled())
                .build();
    }
}
