package ru.wxw.stepan.bankproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.wxw.stepan.bankproject.model.Client;
@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {
}
