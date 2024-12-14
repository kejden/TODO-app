package io.ndk.backend.Mappers.impl;

import io.ndk.backend.Mappers.Mapper;
import io.ndk.backend.dto.request.AccountSignUp;
import io.ndk.backend.entity.User;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class UserMapperImpl implements Mapper<User, AccountSignUp> {

    private ModelMapper modelMapper;

    @Override
    public AccountSignUp mapTo(User userEntity) {
        return modelMapper.map(userEntity, AccountSignUp.class);
    }

    @Override
    public User mapFrom(AccountSignUp accountSignUp) {
        return modelMapper.map(accountSignUp, User.class);
    }
}
