package ru.hogwarts.school.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.repository.CrudRepository;
import ru.hogwarts.school.model.Avatar;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface AvatarRepository extends CrudRepository<Avatar, Long> {
    Optional<Avatar> findByStudent_Id(Long id);

    Page<Avatar> findAll(Pageable pageable);
}
