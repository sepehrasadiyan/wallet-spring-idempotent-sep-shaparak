package me.sepehrasadiyan.repository.registry;

import me.sepehrasadiyan.model.registry.Registry;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface RegistryRepository extends JpaRepository<Registry, UUID> {

    Registry findByBID(String BID);

    Page<Registry> findByBID(String BID, Pageable pageable);

}
