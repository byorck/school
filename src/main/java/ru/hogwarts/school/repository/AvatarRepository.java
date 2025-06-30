package ru.hogwarts.school.repository;

import org.springframework.data.repository.CrudRepository;
import ru.hogwarts.school.model.Avatar;

import java.util.Optional;

public interface AvatarRepository extends CrudRepository<Avatar, Long> {
    Optional<Avatar> findByStudent_Id(Long id);
}
