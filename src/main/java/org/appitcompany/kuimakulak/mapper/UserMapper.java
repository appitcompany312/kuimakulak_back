package org.appitcompany.kuimakulak.mapper;

import org.appitcompany.kuimakulak.dto.userDto.UserProfileDTO;
import org.appitcompany.kuimakulak.entity.User;

public class UserMapper {
    public UserProfileDTO toUserProfileDto(User user, String photoUrl) {
        String fullName = (user.getFirstName() != null ? user.getFirstName() : "") +
                (user.getLastName() != null ? " " + user.getLastName() : "");
        return new UserProfileDTO(
                fullName,
                user.getEmail(),
                user.getImageUrl() != null ? user.getImageUrl() : photoUrl
        );
    }
}
