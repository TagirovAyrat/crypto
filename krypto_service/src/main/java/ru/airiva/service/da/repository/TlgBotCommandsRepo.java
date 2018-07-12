package ru.airiva.service.da.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.airiva.entities.TlgBotCommandsEntity;

public interface TlgBotCommandsRepo extends JpaRepository<TlgBotCommandsEntity, Long> {
}
