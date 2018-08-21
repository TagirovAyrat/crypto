package ru.airiva.service.da.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.airiva.entities.TlgClientEntity;

/**
 * @author Ivan
 */
@Repository
public interface TlgClientRepo extends JpaRepository<TlgClientEntity, Long> {

    TlgClientEntity findByPhone(String phone);

    void deleteByPhone(String phone);

    boolean existsByPhone(String phone);
//    @Modifying
//    @Query("update User u set u.firstname = ?1, u.lastname = ?2 where u.id = ?3")
//    void setUserInfoById(String firstname, String lastname, Integer userId);
}
