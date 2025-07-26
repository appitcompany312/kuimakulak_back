package org.appitcompany.kuimakulak.service.impl;

import lombok.RequiredArgsConstructor;
import org.appitcompany.kuimakulak.dto.userDto.UserProfileDTO;
import org.appitcompany.kuimakulak.dto.userDto.UserProfileUpdateRequest;
import org.appitcompany.kuimakulak.entity.User;
import org.appitcompany.kuimakulak.exceptions.UnauthorizedException;
import org.appitcompany.kuimakulak.mapper.UserMapper;
import org.appitcompany.kuimakulak.repository.UserRepository;
import org.appitcompany.kuimakulak.service.AmazonS3Service;
import org.appitcompany.kuimakulak.service.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;

    private final UserRepository userRepository;

    private final AmazonS3Service amazonS3Service;

    @Value("${user.default.avatar}")
    private String userDefaultAvatarUrl;

    @Override
    public ResponseEntity<UserProfileDTO> getUserProfile(Authentication currentUser) {
        User user = getUserFromAuthentication(currentUser);
        return ResponseEntity.ok(userMapper.toUserProfileDto(user, userDefaultAvatarUrl));
    }


    @Override
    public ResponseEntity<UserProfileDTO> updateUserProfile(Authentication currentUser,
                                                            UserProfileUpdateRequest request) {
        User user = getUserFromAuthentication(currentUser);

        if (request.fullName() == null || request.fullName().trim().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        String fullName = request.fullName().trim();
        String[] nameParts = fullName.split("\\s+", 2);

        user.setFirstName(nameParts[0]);
        user.setLastName(nameParts.length > 1 ? nameParts[1] : "");

        User updatedUser = userRepository.save(user);

        UserProfileDTO userProfileDTO = userMapper.toUserProfileDto(updatedUser, user.getImageUrl());

        return ResponseEntity.ok(userProfileDTO);
    }


    @Override
    public ResponseEntity<Void> uploadAvatar(Authentication currentUser, MultipartFile file) {

        validateImageFile(file);

        User user = getUserFromAuthentication(currentUser);
        String photoUrl = amazonS3Service.uploadFile(file);
        user.setImageUrl(photoUrl);
        userRepository.save(user);
        return ResponseEntity.ok().build();

    }


    @Override
    public ResponseEntity<Void> deleteAvatar(Authentication currentUser) {
        User user = getUserFromAuthentication(currentUser);
        if (user.getImageUrl() != null && !user.getImageUrl().isEmpty() && !user.getImageUrl().equals(userDefaultAvatarUrl)) {
            amazonS3Service.deleteFile(user.getImageUrl());
        }
        user.setImageUrl(userDefaultAvatarUrl);
        userRepository.save(user);

        return ResponseEntity.ok().build();
    }

    public User getUserFromAuthentication(Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();

            if (principal instanceof User appUser) {
                return appUser;
            } else {
                throw new IllegalArgumentException("Principal is not an instance of AppUser");
            }
        }
        throw new UnauthorizedException("Authentication required!");
    }

    private void validateImageFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("File is empty. Please select a file to upload.");
        }

        List<String> allowedContentTypes = Arrays.asList("image/jpeg", "image/png", "image/bmp", "image/webp", "image/avif");
        String contentType = file.getContentType();
        if (!allowedContentTypes.contains(contentType.toLowerCase())) {
            throw new IllegalArgumentException("Invalid file type. Only JPG, PNG, GIF, and BMP images are allowed.");
        }
        try {
            BufferedImage bufferedImage = ImageIO.read(file.getInputStream());
            if (bufferedImage == null) {
                throw new IllegalArgumentException("Invalid image content. The file could not be read as an image.");
            }
        } catch (IOException e) {
            throw new IllegalArgumentException("Could not process file. Please ensure it is a valid image.", e);
        }
    }
}
