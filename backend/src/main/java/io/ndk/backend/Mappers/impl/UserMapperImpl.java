package io.ndk.backend.Mappers.impl;

import io.ndk.backend.Mappers.Mapper;
import io.ndk.backend.dto.request.AccountSignUp;
import io.ndk.backend.entity.UserEntity;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class UserMapperImpl implements Mapper<UserEntity, AccountSignUp> {

    private ModelMapper modelMapper;

    @Override
    public AccountSignUp mapTo(UserEntity userEntity) {
        return modelMapper.map(userEntity, AccountSignUp.class);
    }

    @Override
    public UserEntity mapFrom(AccountSignUp accountSignUp) {
        return modelMapper.map(accountSignUp, UserEntity.class);
    }
}
