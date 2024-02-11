package by.matvey.lshkn.mapper;

import by.matvey.lshkn.dto.UserDto;
import by.matvey.lshkn.entity.User;
import org.mapstruct.*;

@Mapper
public interface UserMapper {
    UserDto userToUserDto(User user);

    @Mapping(target = "meters", ignore = true)
    User userDtoToUser(UserDto userDto);
}
