package ru.airiva.service.da.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.airiva.entities.TlgBotEntity;

import java.util.List;

/**
 * @author Ivan
 */
@Repository
public interface TlgBotRepo extends JpaRepository<TlgBotEntity, Long> {


    TlgBotEntity findByToken(String token);

}
