package me.sepehrasadiyan.repository.registry;

import me.sepehrasadiyan.model.registry.RegistryPreUpdate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface RegistryPreUpdateRepository extends JpaRepository<RegistryPreUpdate, UUID> {

    Page<RegistryPreUpdate> findByBID(String BID, Pageable pageable);
}
