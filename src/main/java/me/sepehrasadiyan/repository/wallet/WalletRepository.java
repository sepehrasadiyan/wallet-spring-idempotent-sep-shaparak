package me.sepehrasadiyan.repository.wallet;

import me.sepehrasadiyan.model.wallet.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;
@Repository
public interface WalletRepository extends JpaRepository<Wallet, UUID> {

    Wallet findByBID(String BID);



}
