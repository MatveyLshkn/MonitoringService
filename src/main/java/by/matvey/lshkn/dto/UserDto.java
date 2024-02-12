package by.matvey.lshkn.dto;

import by.matvey.lshkn.entity.Role;
import lombok.*;

/**
 * User dto
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {
    /**
     * Unique user identifier
     */
    private Long id;
    /**
     * Username of user
     */
    private String username;
    /**
     * user's password
     */
    private String password;
    /**
     * Role of user
     *
     * @see Role
     */
    private String role;
}
