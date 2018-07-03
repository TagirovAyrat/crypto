package ru.airiva.service.da.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.airiva.entities.TlgUserEntity;

/**
 * @author Ivan
 */
@Repository
public interface TlgUserRepo extends JpaRepository<TlgUserEntity, Long> {
}
