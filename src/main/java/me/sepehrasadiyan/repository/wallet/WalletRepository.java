package me.sepehrasadiyan.repository.wallet;

import me.sepehrasadiyan.model.wallet.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.persistence.LockModeType;
import java.sql.Timestamp;
import java.util.UUID;

@Repository
public interface WalletRepository extends JpaRepository<Wallet, UUID> {

  @Lock(LockModeType.OPTIMISTIC_FORCE_INCREMENT)
  Wallet findByBID(String BID);

  //TODO: as i mentioned before use Wallet Listener for log. OR(||) implement AOP i not Suggest to use it like this.
  @Lock(LockModeType.OPTIMISTIC_FORCE_INCREMENT)
  @Query("UPDATE Wallet w SET w.currentBalance = ?3, w.lastAccessTime = ?4, w.lastUserAccess = ?5, w.lastRegistryPreUpdate = ?6 " +
          "WHERE w.id = ?1 AND w.bid = ?2")
  void updateAddMoneyWalletByBid(UUID uuid, String bid, double currentBalance, Timestamp lastAccessTime,
                                 String lastUserAccess, UUID LastRegistryPreUpdate);

  @Lock(LockModeType.OPTIMISTIC_FORCE_INCREMENT)
  @Query("UPDATE Wallet w SET w.enable = FALSE " +
          "WHERE w.bid = ?1 ")
  void makeWalletDisable(String bid);

}
