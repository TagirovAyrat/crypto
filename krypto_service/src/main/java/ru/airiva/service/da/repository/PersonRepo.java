package ru.airiva.service.da.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.airiva.entities.PersonEntity;

import java.util.Optional;

/**
 * @author Ivan
 */
@Repository
public interface PersonRepo extends JpaRepository<PersonEntity, Long> {
}
