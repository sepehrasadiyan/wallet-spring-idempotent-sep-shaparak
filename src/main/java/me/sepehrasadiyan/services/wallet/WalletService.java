package me.sepehrasadiyan.services.wallet;

import me.sepehrasadiyan.exception.ResourceNotFound;
import me.sepehrasadiyan.exception.WalletAmountNotEnoughException;
import me.sepehrasadiyan.exception.WalletDisableException;
import me.sepehrasadiyan.model.registry.Registry;
import me.sepehrasadiyan.model.registry.RegistryPreUpdate;
import me.sepehrasadiyan.model.registry.RegistryType;
import me.sepehrasadiyan.model.wallet.Wallet;
import me.sepehrasadiyan.model.wallet.bill.Bill;
import me.sepehrasadiyan.repository.wallet.WalletRepository;
import me.sepehrasadiyan.services.registry.RegistryService;
import me.sepehrasadiyan.util.UserUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.sql.Timestamp;
import java.time.Clock;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
public class WalletService {
  private static final Logger LOGGER = LoggerFactory.getLogger(WalletService.class);

  @Autowired
  private WalletRepository walletRepository;

  @Autowired
  private RegistryService registryService;

  @Value("${fee.amount}")
  private Long feeAmount;

  public ResponseEntity<?> getCurrentBalance() throws Exception {
    Wallet wallet = walletRepository.findByBID(UserUtils.getProfile().getGroup());
    if (wallet != null) {
      if (wallet.isEnable()) {
        return ResponseEntity.status(HttpStatus.OK).body(String.valueOf(wallet.getCurrentBalance()));
      } else {
        return ResponseEntity.status(HttpStatus.LOCKED).build();
      }
    } else {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
  }

  @Transactional(propagation = Propagation.REQUIRED)
  public Wallet checkWalletBeforePayment(String BID, String username) throws Exception {
    Wallet wallet = null;
    if ((wallet = walletRepository.findByBID(BID)) == null) {
      wallet = createNewWallet( BID, username);
      if (wallet == null) throw new RuntimeException("Some thing went wrong.");
      return walletRepository.save(wallet);
    }
    RegistryPreUpdate registryPreUpdate = registryService.getRegistryPreUpdate(wallet.getLastRegistryPreUpdateID());
    if (!wallet.isEnable() &&
            !wallet.getLastRegistryPreUpdateID().toString().equals(registryPreUpdate.getRegistryPreUpdateId().toString())
            && Objects.equals(registryPreUpdate.getUpdatedBalance(), wallet.getCurrentBalance())) {
      makeWalletDisable(BID);
      throw new WalletDisableException("wallet is disable call admin.");
    }
    return wallet;
  }

  public Wallet getWallet(String BID) throws Exception {
    Optional<Wallet> wallet = Optional.ofNullable(walletRepository.findByBID(BID));
    if (wallet.isPresent()) {
      if (!wallet.get().isEnable()) {
        throw new WalletDisableException("wallet is disable call admin.");
      }
      return wallet.get();
    } else {
      throw new ResourceNotFound("Wallet Not Found.");
    }
  }

  @Transactional(propagation = Propagation.REQUIRED)
  public Wallet addMoney(Double amount, String BID, String username, Bill bill) {
    Wallet wallet = null;
    try {
      wallet = walletRepository.findByBID(BID);
      if (wallet == null || !wallet.isEnable()) throw new WalletDisableException("Wallet have error call admin.");
      RegistryPreUpdate registryPreUpdateFix = registryService.getRegistryPreUpdate(wallet.getLastRegistryPreUpdateID());
      if (wallet.getLastRegistryPreUpdateID().toString().equals(registryPreUpdateFix.getRegistryPreUpdateId().toString()) &&
              Objects.equals(wallet.getCurrentBalance(), registryPreUpdateFix.getUpdatedBalance())) {
        RegistryPreUpdate registryPreUpdate = registryService.updateWalletRegistry(wallet, amount, null, bill);
        walletRepository.updateAddMoneyWalletByBid(wallet.getId(), BID, wallet.getCurrentBalance() + amount,
                Timestamp.from(Instant.now(Clock.systemUTC())), username, registryPreUpdate.getRegistryPreUpdateId());
        return wallet;
      }
      return wallet;
    } catch (Exception e) {
      //TODO: as i told before try not to return Exception its bad write you own Exception. i just write mose Used.
      TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
      LOGGER.error("try to log properly and create good recovery methods.", e);
      makeWalletDisable(BID);
      return null;
    }
  }


  @Transactional(propagation = Propagation.REQUIRED)
  public Wallet depositMoney(Double amount, String BID, UUID paymentInfoId, String username) throws Exception {
    Wallet wallet = walletRepository.findByBID(BID);
    synchronized (wallet) {
      RegistryPreUpdate registryPreUpdateFix = registryService.getRegistryPreUpdate(wallet.getLastRegistryPreUpdateID());
      if (wallet.getLastRegistryPreUpdateID().toString().equals(registryPreUpdateFix.getRegistryPreUpdateId().toString()) &&
              Objects.equals(wallet.getCurrentBalance(), registryPreUpdateFix.getUpdatedBalance())) {
        throw new WalletDisableException("Error in wallet.");
      }
      if (wallet.isEnable()) {
        double currentAmount = wallet.getCurrentBalance();
        if ((currentAmount - amount) < feeAmount) {
          throw new WalletAmountNotEnoughException("Amount is not enough.");
        }
        wallet.setCurrentBalance(wallet.getCurrentBalance() - amount);
        wallet.setLastUserAccess(username);
        wallet.setLastAccessTime(Timestamp.from(Instant.now(Clock.systemDefaultZone())));
        RegistryPreUpdate registryPreUpdate = registryService.updateWalletRegistry(wallet, amount, paymentInfoId, null);
        wallet.setLastRegistryPreUpdateID(registryPreUpdate.getRegistryPreUpdateId());
        Wallet walletEdited = walletRepository.save(wallet);
        if(Objects.equals(walletEdited.getCurrentBalance(), wallet.getCurrentBalance()) &&
            walletEdited.getLastRegistryPreUpdateID().equals(wallet.getLastRegistryPreUpdateID())){
          TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
          makeWalletDisable(BID);
        }
        return wallet;
      } else {
        throw new WalletDisableException("Wallet have error call admin.");
      }
    }
  }

  private Wallet createNewWallet( String BID, String username) {
    try {
      RegistryPreUpdate registryPreUpdate = new RegistryPreUpdate();
      Wallet wallet = new Wallet();
      wallet.setLastAccessTime(Timestamp.from(Instant.now(Clock.systemDefaultZone())));
      wallet.setLastUserAccess(username);
      wallet.setBID(BID);
      wallet.setCurrentBalance(0.0);
      wallet.setEnable(true);
      Registry registry = new Registry();
      registry.setRegistryPreUpdates(new ArrayList<>());
      registry.setBID(wallet.getBID());
      registryPreUpdate = new RegistryPreUpdate();
      registryPreUpdate.setRegistry(registry);
      registryPreUpdate.setUpdatedBalance(0.0);
      registryPreUpdate.setUsername(username);
      registryPreUpdate.setAmount(0.0);
      registryPreUpdate.setLastBalance(0.0);
      registryPreUpdate.setAmountChange(0.0);
      registryPreUpdate.setBillId(null);
      registryPreUpdate.setPaymentInfoId(null);
      registryPreUpdate.setModifyTime(Timestamp.from(Instant.now(Clock.systemDefaultZone())));
      registryPreUpdate.setBID(BID);
      registryPreUpdate.setType(RegistryType.CREATE);
      registry.getRegistryPreUpdates().add(registryPreUpdate);
      Registry registrySaved = registryService.addRegistry(registry);
      wallet.setLastRegistryPreUpdateID(registrySaved.getRegistryPreUpdates().get(0).getRegistryPreUpdateId());
      return wallet;
    }catch (Exception e){
      //TODO: as i told try not to return Exception its bad write you own Exception. i just write mose Used.
      LOGGER.error("Can not create new wallet for bid:{} and userName :{}", BID, username, e);
      return null;
    }
  }

  private void makeWalletDisable(String bid){
    walletRepository.makeWalletDisable(bid);
  }

}
