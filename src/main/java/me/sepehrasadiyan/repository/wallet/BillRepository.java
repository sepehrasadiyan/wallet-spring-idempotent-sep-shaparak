package me.sepehrasadiyan.repository.wallet;

import me.sepehrasadiyan.model.wallet.bill.Bill;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.LockModeType;
import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

@Repository
public interface BillRepository extends JpaRepository<Bill, UUID> {

    List<Bill> findAllByBID(String BID, Sort sort);


    @Query(value = "SELECT * FROM bill as b WHERE b.BID =:bid And b.bill_state =:billState1 Or b.bill_state =:billState2", nativeQuery = true)
    List<Bill> findByBIDAndBillState(@Param("bid") String BID, @Param("billState1") int billState1,
                                        @Param("billState2") int billState2);


    List<Bill> findAllByBIDAndUsername(String BID, String username, Sort sort);

    @Query(value = "SELECT * FROM bill as b WHERE b.BID =:bid And b.username =:username And b.billState =:billState",
            nativeQuery = true)
    @Lock(LockModeType.OPTIMISTIC_FORCE_INCREMENT)
    Bill findByBIDAndUsernameAndBillState(@Param("bid") String BID, @Param("username") String username,
                                          @Param("billState") int billStateCreate);

    @Modifying
    @Transactional(propagation = Propagation.MANDATORY)
    @Query(value = "DELETE FROM bill WHERE bill.createTime < :expiryDate And bill.billState =:billStateCreate ", nativeQuery = true)
    int deleteCreateTimeExpiry(@Param("expiryDate") Timestamp expiryDate, @Param("billStateCreate") int billStateCreate);

    @Modifying
    @Transactional(propagation = Propagation.MANDATORY)
    @Query(value = "DELETE FROM bill WHERE bill.BID =:BID And bill.username =:username And bill.bill_state =:billStateCreate Or bill.bill_state =:" +
            "billStateWaiting", nativeQuery = true)
    int deleteUserBillsActive(@Param("BID") String BID, @Param("username") String username,
                              @Param("billStateCreate") int billStateCreate,
                              @Param("billStateWaiting") int billStateWaiting);



}
