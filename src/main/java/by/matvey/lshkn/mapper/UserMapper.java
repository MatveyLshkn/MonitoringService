package by.matvey.lshkn.mapper;

import by.matvey.lshkn.dto.UserDto;
import by.matvey.lshkn.entity.User;
import org.mapstruct.*;

/**
 * Mapper for User and UserDto
 */
@Mapper
public interface UserMapper {
    /**
     * Maps User to UserDto
     *
     * @param user user to map
     * @return UserDto in whichUser was mapped
     */
    UserDto userToUserDto(User user);

    /**
     * Maps UserDto to User
     *
     * @param userDto userDto to map
     * @return User in which UserDto was mapped
     */
    @Mapping(target = "meters", ignore = true)
    User userDtoToUser(UserDto userDto);
}
