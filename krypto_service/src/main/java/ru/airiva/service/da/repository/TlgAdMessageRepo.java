package ru.airiva.service.da.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.airiva.entities.TlgAdMessageEntity;

/**
 * @author Ivan
 */
@Repository
public interface TlgAdMessageRepo extends JpaRepository<TlgAdMessageEntity, Long> {
}
