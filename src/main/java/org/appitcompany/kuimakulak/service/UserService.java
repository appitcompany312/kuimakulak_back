package org.appitcompany.kuimakulak.service;

import org.appitcompany.kuimakulak.dto.userDto.UserProfileDTO;
import org.appitcompany.kuimakulak.dto.userDto.UserProfileUpdateRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.multipart.MultipartFile;

public interface UserService {
    ResponseEntity<UserProfileDTO> getUserProfile(Authentication currentUser);

    ResponseEntity<UserProfileDTO> updateUserProfile(Authentication currentUser, UserProfileUpdateRequest request);

    ResponseEntity<Void> uploadAvatar(Authentication currentUser, MultipartFile file);

    ResponseEntity<Void> deleteAvatar(Authentication currentUser);
}
