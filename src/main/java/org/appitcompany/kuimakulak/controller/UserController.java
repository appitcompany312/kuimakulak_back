package org.appitcompany.kuimakulak.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.appitcompany.kuimakulak.dto.UserDto.UserProfileDTO;
import org.appitcompany.kuimakulak.dto.UserDto.UserProfileUpdateRequest;
import org.appitcompany.kuimakulak.service.UserService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    @GetMapping("/profile")
    @Operation(summary = "Get user profile", description = "Fetches the current authenticated user's profile information.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved user profile"),
            @ApiResponse(responseCode = "401", description = "Unauthorized access"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    public ResponseEntity<UserProfileDTO> getProfile(Authentication currentUser) {
        return userService.getUserProfile(currentUser);
    }

    @PutMapping("/profile")
    @Operation(summary = "Update user profile", description = "Updates the authenticated user's profile with the provided data.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully updated user profile"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "401", description = "Unauthorized access"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    public ResponseEntity<UserProfileDTO> updateProfile(Authentication currentUser,
                                                        @Valid @RequestBody UserProfileUpdateRequest request) {
        return userService.updateUserProfile(currentUser, request);
    }

    @PostMapping(value = "/upload-avatar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Upload avatar", description = "Uploads a new avatar image for the authenticated user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully uploaded avatar"),
            @ApiResponse(responseCode = "400", description = "Invalid file format or size"),
            @ApiResponse(responseCode = "401", description = "Unauthorized access"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    public ResponseEntity<Void> uploadAvatar(
            Authentication currentUser,
            @RequestParam("avatar") MultipartFile file) {
        return userService.uploadAvatar(currentUser, file);
    }

    @DeleteMapping("/avatar")
    @Operation(summary = "Delete avatar", description = "Deletes the current avatar of the authenticated user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully deleted avatar"),
            @ApiResponse(responseCode = "401", description = "Unauthorized access"),
            @ApiResponse(responseCode = "404", description = "Avatar not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    public ResponseEntity<Void> deleteAvatar(Authentication currentUser) {
        return userService.deleteAvatar(currentUser);
    }

}
