package me.sepehrasadiyan.services.bill;

import me.sepehrasadiyan.model.IPG.SepRedirectResponse;
import me.sepehrasadiyan.model.IPG.TransactionInfo;
import me.sepehrasadiyan.model.wallet.bill.BillDto;
import me.sepehrasadiyan.model.enums.BillState;
import me.sepehrasadiyan.model.wallet.bill.Bill;
import me.sepehrasadiyan.model.wallet.bill.BillCreate;
import me.sepehrasadiyan.repository.wallet.BillRepository;
import me.sepehrasadiyan.services.ipg.IPGService;
import me.sepehrasadiyan.services.wallet.WalletService;
import me.sepehrasadiyan.util.UserUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Timestamp;
import java.time.Clock;
import java.time.Instant;
import java.util.*;

@Service
public class BillService {

    private static final int quart_hour_sec = (30 * 30);

    @Autowired
    private BillRepository billRepository;

    @Autowired
    private WalletService walletService;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private IPGService ipgService;


    public Bill getBill(String billId) throws Exception {
        Optional<Bill> bill = billRepository.findById(UUID.fromString(billId));
        if (bill.isPresent()) {
            return bill.get();
        }
        throw new Exception("bill Not found");
    }

    public List<BillDto> getAllBills(String BID, @Nullable String username) {
        Sort sort = Sort.by(Sort.Direction.ASC, "createTime");
        List<Bill> bills;
        List<BillDto> billDtos = new ArrayList<>();
        if (username != null) {
            bills = billRepository.findAllByBIDAndUsername(BID, username, sort);
            for (Bill bill : bills) {
                billDtos.add(modelMapper.map(bill, BillDto.class));
            }
            return billDtos;
        } else {
            bills = billRepository.findAllByBID(BID, sort);
            for (Bill bill : bills) {
                billDtos.add(modelMapper.map(bill, BillDto.class));
            }
            return billDtos;
        }
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public BillDto createBill(BillCreate billCreate, String BID, String username) throws Exception {
        Bill bill = new Bill();
        bill.setBillState(BillState.CREATE);
        bill.setCreateTime(Timestamp.from(Instant.now(Clock.systemDefaultZone())));
        bill.setExpTime(Timestamp.from(Instant.now(Clock.systemDefaultZone()).plusSeconds(quart_hour_sec)));
        long taxPlusAmount = (billCreate.getAmount() * 9)/100;
        bill.setAmount(billCreate.getAmount());
        bill.setFee(taxPlusAmount);
        bill.setAmountWithFee(taxPlusAmount + billCreate.getAmount());
        bill.setDescription("Waiting for payment.");
        bill.setBID(BID);
        bill.setUsername(username);
        Bill billCreated = billRepository.save(bill);
        return modelMapper.map(billCreated, BillDto.class);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public boolean cancelBill(String BID, String username) throws Exception {
        long deletedItem = billRepository.deleteUserBillsActive(BID, username
                , BillState.CREATE.getBillState()
                , BillState.WAITING.getBillState());
        return deletedItem > 0L;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public synchronized Bill changeBillState(Bill bill,
                                             @Nullable SepRedirectResponse sepRedirectResponse) throws Exception {
        if (sepRedirectResponse == null) {
            bill.setBillState(BillState.WAITING);
            bill.setUpdateTime(Timestamp.from(Instant.now(Clock.systemDefaultZone())));
            return billRepository.save(bill);
        } else {
            switch (sepRedirectResponse.getStatus()) {
                case 2:
                    TransactionInfo transactionInfo = null;
                    if ((transactionInfo = ipgService.verifyTransaction(bill, sepRedirectResponse)) != null &&
                            !transactionInfo.isNeedReject()) {
                        bill.setBillState(BillState.SUCCESS);
                        bill.setRefNum(transactionInfo.getRefNum());
                        bill.setTerminalId(transactionInfo.getTerminalNumber());
                        bill.setTraceNo(sepRedirectResponse.getTraceNo());
                        bill.setDescription(transactionInfo.getResultDescription());
                        bill.setTraceNo(sepRedirectResponse.getTraceNo());
                        bill.setSecurePan(sepRedirectResponse.getSecurePan());
                        Bill response = billRepository.save(bill);
                        walletService.addMoney(response.getAmount(), bill.getBID(), bill.getUsername(), bill);
                        return response;
                    } else if (transactionInfo != null && transactionInfo.isRejectSuccess()) {
                        bill.setBillState(BillState.ERROR_REVERSE_MONEY);
                        bill.setRefNum(sepRedirectResponse.getRefNum());
                        bill.setDescription("The return of the money was successful.");
                        bill.setTerminalId(sepRedirectResponse.getTerminalId());
                        bill.setTraceNo(sepRedirectResponse.getTraceNo());
                        bill.setRejectionSucceed(transactionInfo.isRejectSuccess());
                        return billRepository.save(bill);
                    } else if (transactionInfo != null) {
                        bill.setBillState(BillState.ERROR_REVERSE_MONEY);
                        bill.setRefNum(sepRedirectResponse.getRefNum());
                        bill.setTerminalId(sepRedirectResponse.getTerminalId());
                        bill.setTraceNo(sepRedirectResponse.getTraceNo());
                        bill.setRejectionSucceed(transactionInfo.isRejectSuccess());
                        bill.setDescription("The return of the money was unsuccessful.");
                        return billRepository.save(bill);
                    }
                    bill.setBillState(BillState.ERROR);
                    bill.setErrorCode(sepRedirectResponse.getStatus());
                    bill.setDescription("Unsuccessful payment.");
                    bill.setRefNum(sepRedirectResponse.getRefNum());
                    bill.setTerminalId(sepRedirectResponse.getTerminalId());
                    bill.setTraceNo(sepRedirectResponse.getTraceNo());
                    return billRepository.save(bill);
                case 1:
                case 3:
                case 4:
                case 5:
                case 8:
                case 10:
                case 11:
                case 12:
                    bill.setBillState(BillState.ERROR);
                    bill.setErrorCode(sepRedirectResponse.getStatus());
                    bill.setDescription("There was an issue with the payment process.");
                    bill.setRefNum(sepRedirectResponse.getRefNum());
                    bill.setTerminalId(sepRedirectResponse.getTerminalId());
                    bill.setTraceNo(sepRedirectResponse.getTraceNo());
                    bill.setErrorDescription(sepRedirectResponse.getState());
                    return billRepository.save(bill);
            }
            throw new Exception("Unknown Exception.");
        }
    }

    public Cookie getActiveCookieValue() {
        return getCookieFromDB();
    }

    public boolean validateIncomingRequestForBillCreated(HttpServletRequest request){
        List<Bill> bills = billRepository.findByBIDAndBillState(UserUtils.getProfile().getGroup(),
                BillState.CREATE.getBillState(),
                BillState.WAITING.getBillState());
        if(bills.isEmpty()){
            return true;
        }
        for(Bill bill : bills){
            long expTime = Timestamp.from(Instant.now(Clock.systemDefaultZone())).getTime();
            if(bill.getExpTime().getTime() < expTime){
                bill.setBillState(BillState.ERROR);
                bill.setDescription("Expired.");
                bill.setErrorDescription("Expired.");
                bill.setUpdateTime(Timestamp.from(Instant.now(Clock.systemDefaultZone())));
                billRepository.save(bill);
            }
        }
        return false;
    }

    public boolean validateIncomingRequestForIdempotentEffect(HttpServletRequest request) {
        String eTag = null;
        if (request.getCookies() == null || request.getCookies().length == 0) return false;
        for (Cookie cookie : request.getCookies()) {
            if (cookie.getName().equals("eTag")) {
                eTag = cookie.getValue();
            }
        }
        if (eTag != null) {
            UUID uuid = UUID.fromString(eTag);
            Optional<Bill> bill = billRepository.findById(uuid);
            if (bill.isPresent()) {
                if (bill.get().getBillState().getBillState() == 1) {
                    return false;
                }
            } else {
                return false;
            }
        } else {
            return false;
        }
        return true;
    }

    //TODO:For Check Every request in filters
    public Cookie addCookieCreateStatus(HttpServletRequest request, HttpServletResponse response) {
        String eTag = null;
        if (request.getCookies() == null || request.getCookies().length < 1) {
            return getCookieFromDB();
        } else {
            for (Cookie cookie : request.getCookies()) {
                if (cookie.getName().equals("eTag")) return null;
            }
            return getCookieFromDB();
        }
    }

    private Cookie getCookieFromDB() {
        Bill bill = billRepository.findByBIDAndUsernameAndBillState(
                UserUtils.getProfile().getGroup(),
                UserUtils.getProfile().getUsername(),
                BillState.CREATE.getBillState()
        );
        if (bill == null) return null;
        String eTag = bill.getId().toString();
        Cookie cookie = new Cookie("eTag", bill.getId().toString());
        cookie.setHttpOnly(true);
        cookie.setMaxAge(quart_hour_sec);
        cookie.setPath("/api/bill");
        //TODO: cookie.setDomain();
        //TODO: cookie.setSecure();
        return cookie;
    }


}
