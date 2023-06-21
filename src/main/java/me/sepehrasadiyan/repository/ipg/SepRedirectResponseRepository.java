package me.sepehrasadiyan.repository.ipg;

import me.sepehrasadiyan.model.IPG.SepRedirectResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SepRedirectResponseRepository extends JpaRepository<SepRedirectResponse, String> {


}
