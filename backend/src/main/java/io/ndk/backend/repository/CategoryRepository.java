package io.ndk.backend.repository;

import io.ndk.backend.entity.CategoryEntity;
import io.ndk.backend.entity.UserEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryRepository extends CrudRepository<CategoryEntity, Long> {
    boolean existsByNameAndUser(String name, UserEntity user);
    Optional<CategoryEntity> findByName(String name);
}
