package me.sepehrasadiyan.services.ipg;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.sepehrasadiyan.model.IPG.*;
import me.sepehrasadiyan.model.wallet.bill.Bill;
import me.sepehrasadiyan.model.wallet.bill.BillDto;
import me.sepehrasadiyan.repository.ipg.SepRedirectResponseRepository;
import me.sepehrasadiyan.repository.wallet.TransactionInfoRepository;
import me.sepehrasadiyan.services.bill.BillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.io.ByteArrayOutputStream;
import java.io.IOException;


@Service
public class IPGService {

  private static final ObjectMapper MAPPER = new ObjectMapper();


  @Value("${sep.shaparak-address}")
  private String sep_shaparak_address;

  @Value("${sep.terminal-id}")
  private String terminalId;

  @Value("${sep.redirect-url}")
  private String redirect_url;

  @Value("${sep.verify-transaction}")
  private String verity_transaction;

  @Value("${sep.reverse-transaction}")
  private String revers_transaction;

  @Autowired
  private RestTemplate restTemplate;

  @Autowired
  private SepRedirectResponseRepository sepRedirectResponseRepository;

  @Autowired
  private BillService billService;

  @Autowired
  private TransactionInfoRepository transactionInfoRepository;

  public String getToken() {
    return null;
  }

  @Transactional(propagation = Propagation.REQUIRED)
  public SepTokenResponse getTokenFromIPG(Bill bill) throws Exception {
    SepTxn sepTxn = new SepTxn();
    sepTxn.setAction("token");
    sepTxn.setRedirectUrl(this.redirect_url);
    sepTxn.setAmount(bill.getAmountWithFee());
    sepTxn.setResNum(bill.getId().toString());
    sepTxn.setTerminalId(this.terminalId);
    HttpHeaders headers = new HttpHeaders();
    headers.add("Content-Type", "application/json");
    ResponseEntity<SepTokenResponse> response = restTemplate.postForEntity(
            sep_shaparak_address,
            new HttpEntity<>(toJson(sepTxn), headers),
            SepTokenResponse.class
    );
    SepTokenResponse sepTokenResponse = response.getBody();
    assert sepTokenResponse != null;
    if (sepTokenResponse.getStatus() != 1) {
      throw new Exception(sepTokenResponse.getErrorDesc());
    }
    return sepTokenResponse;
  }

  public BillDto processResponseIPG(SepRedirectResponse sepRedirectResponse) throws Exception {
    sepRedirectResponseRepository.save(sepRedirectResponse);
    Bill bill;
    if ((bill = billService.getBill(sepRedirectResponse.getResNum())) != null) {
      Bill newBill = billService.changeBillState(bill, sepRedirectResponse);
      BillDto billDto = new BillDto();
      billDto.setBillState(newBill.getBillState());
      billDto.setAmount(newBill.getAmount());
      billDto.setDescription(newBill.getDescription());
      billDto.setCreateTime(newBill.getCreateTime());
      billDto.setRrn(sepRedirectResponse.getRRN());
      billDto.setTraceNo(sepRedirectResponse.getTraceNo());
      return billDto;
    }
    throw new Exception("Error in server");
  }

  public TransactionInfo verifyTransaction(Bill bill, SepRedirectResponse sepRedirectResponse) {
    String resDataValid = null;
    try {
      SepVerifyTransactionRequest sepVerifyTransactionRequest = new SepVerifyTransactionRequest();
      sepVerifyTransactionRequest.setRefNum(sepRedirectResponse.getRefNum());
      sepVerifyTransactionRequest.setTerminalNumber(sepRedirectResponse.getTerminalId());
      HttpHeaders headers = new HttpHeaders();
      headers.add("Content-Type", "application/json");
      ResponseEntity<Object> response = restTemplate.postForEntity(
              verity_transaction,
              new HttpEntity<>(toJson(sepVerifyTransactionRequest), headers),
              Object.class
      );
      resDataValid = MAPPER.writeValueAsString(response.getBody());
      SepVerifyTransactionResponse sepVerifyTransactionResponse =
              MAPPER.readValue(resDataValid, SepVerifyTransactionResponse.class);
      assert sepVerifyTransactionResponse != null;
      TransactionInfo transactionInfo = new TransactionInfo();
      if (sepVerifyTransactionResponse.getResultCode() == 0 &&
              sepVerifyTransactionResponse.isSuccess()) {
        if (bill.getAmountWithFee() == sepVerifyTransactionResponse.getTransactionDetail().getAffectiveAmount()) {
          transactionInfo.setAffectiveAmount(sepVerifyTransactionResponse.getTransactionDetail().getAffectiveAmount());
          transactionInfo.setResultCode(sepVerifyTransactionResponse.getResultCode());
          transactionInfo.setResultDescription(sepVerifyTransactionResponse.getResultDescription());
          transactionInfo.setStraceDate(sepVerifyTransactionResponse.getTransactionDetail().getStraceDate());
          transactionInfo.setRefNum(sepVerifyTransactionResponse.getTransactionDetail().getRefNum());
          transactionInfo.setBillId(bill.getId());
          transactionInfo.setTerminalNumber(sepVerifyTransactionResponse.getTransactionDetail().getTerminalNumber());
          transactionInfo.setNeedReject(false);
          transactionInfoRepository.save(transactionInfo);
          return transactionInfo;
        } else {
          return reverseTransaction(bill, sepRedirectResponse);
        }
      } else {
        transactionInfo.setBillId(bill.getId());
        transactionInfo.setResultCode(sepVerifyTransactionResponse.getResultCode());
        transactionInfo.setResultDescription(sepVerifyTransactionResponse.getResultDescription());
        return transactionInfoRepository.save(transactionInfo);
      }
    } catch (Exception e) {
      e.printStackTrace();
      if (resDataValid != null) {
        return reverseTransaction(bill, sepRedirectResponse);
      }
      return null;
    }
  }

  public TransactionInfo reverseTransaction(Bill bill, SepRedirectResponse sepRedirectResponse) {
    String resData = null;
    try {
      SepVerifyTransactionRequest sepReverseRequest = new SepVerifyTransactionRequest();
      sepReverseRequest.setRefNum(sepRedirectResponse.getRefNum());
      sepReverseRequest.setTerminalNumber(sepRedirectResponse.getTerminalId());
      HttpHeaders headers = new HttpHeaders();
      headers.add("Content-Type", "application/json");
      ResponseEntity<Object> response = restTemplate.postForEntity(
              revers_transaction,
              new HttpEntity<>(toJson(sepReverseRequest), headers),
              Object.class
      );
      resData = MAPPER.writeValueAsString(response.getBody());
      SepVerifyTransactionResponse sepReverseTransaction =
              MAPPER.readValue(resData, SepVerifyTransactionResponse.class);
      assert sepReverseTransaction != null;
      TransactionInfo transactionInfo = new TransactionInfo();
      if (sepReverseTransaction.getResultCode() == 0 && sepReverseTransaction.isSuccess()) {
        transactionInfo.setRejectSuccess(true);
      } else {
        transactionInfo.setRejectSuccess(false);
      }
      transactionInfo.setNeedReject(true);
      transactionInfo.setBillId(bill.getId());
      transactionInfo.setAffectiveAmount(sepReverseTransaction.getTransactionDetail().getAffectiveAmount());
      transactionInfo.setTerminalNumber(sepReverseTransaction.getTransactionDetail().getTerminalNumber());
      transactionInfo.setResultDescription(sepReverseTransaction.getResultDescription());
      transactionInfo.setRefNum(sepReverseTransaction.getTransactionDetail().getRefNum());
      transactionInfo.setStraceDate(sepReverseTransaction.getTransactionDetail().getStraceDate());
      return transactionInfoRepository.save(transactionInfo);
    } catch (Exception e) {
      e.printStackTrace();
      if (resData != null) {
        System.out.println("REVERSE_TRANSACTION_RESPONSE => " + resData);
      }
      return null;
    }
  }


  private String toJson(SepVerifyTransactionRequest sepVerifyTransactionRequest) throws IOException {
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    ObjectMapper mapper = new ObjectMapper();
    mapper.writeValue(out, sepVerifyTransactionRequest);
    return out.toString();
  }

  private String toJson(SepTxn sepTxn) throws IOException {
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    ObjectMapper mapper = new ObjectMapper();
    mapper.writeValue(out, sepTxn);
    return out.toString();
  }

  private String toJson(BillDto billDto) throws IOException {
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    ObjectMapper mapper = new ObjectMapper();
    mapper.writeValue(out, billDto);
    return out.toString();
  }


}