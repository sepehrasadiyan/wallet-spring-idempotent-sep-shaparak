package me.sepehrasadiyan.controller.ipg;


import com.fasterxml.jackson.databind.ObjectMapper;
import me.sepehrasadiyan.model.IPG.SepRedirectResponse;
import me.sepehrasadiyan.model.wallet.bill.BillDto;
import me.sepehrasadiyan.services.bill.BillService;
import me.sepehrasadiyan.services.ipg.IPGService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/ipg")
public class IPGController {

  private static final Logger LOGGER = LoggerFactory.getLogger(IPGController.class);

  private static final ObjectMapper MAPPER = new ObjectMapper();

  @Autowired
  private IPGService ipgService;

  @Autowired
  private BillService billService;




  @PostMapping(value = "/transaction-response")
  public ResponseEntity<BillDto> getResponseFromIPG(@RequestBody SepRedirectResponse sepRedirectResponse) throws Exception {
    LOGGER.info("receive redirect from sep.ir:{}", sepRedirectResponse);
    BillDto billDto = ipgService.processResponseIPG(sepRedirectResponse);
    return ResponseEntity.ok(billDto);
  }

}
