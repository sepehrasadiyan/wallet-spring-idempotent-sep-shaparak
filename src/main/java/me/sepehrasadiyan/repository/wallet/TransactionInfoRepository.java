package me.sepehrasadiyan.repository.wallet;

import me.sepehrasadiyan.model.IPG.TransactionInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionInfoRepository extends JpaRepository<TransactionInfo, String> {



}
