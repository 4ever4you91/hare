package com.example.ever.hare.mapper;

import com.example.ever.hare.entity.Users;
import com.example.ever.hare.entity.dto.UsersDTO;
import org.mapstruct.Mapper;

import java.util.Set;

@Mapper(componentModel = "spring")
public interface UsersMapper {
    UsersDTO usersToUsersDTO(Users users);

    Users usersDTOToUsers(UsersDTO usersDTO);

    Set<UsersDTO> toUsersDTOSet(Set<Users> usersSet);
}
