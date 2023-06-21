package me.sepehrasadiyan.controller.wallet;

import me.sepehrasadiyan.model.IPG.SepTokenResponse;
import me.sepehrasadiyan.model.wallet.Wallet;
import me.sepehrasadiyan.model.wallet.bill.Bill;
import me.sepehrasadiyan.model.wallet.bill.BillCreate;
import me.sepehrasadiyan.model.wallet.bill.BillDto;
import me.sepehrasadiyan.services.bill.BillService;
import me.sepehrasadiyan.services.ipg.IPGService;
import me.sepehrasadiyan.services.wallet.WalletService;
import me.sepehrasadiyan.util.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = "/api/bill")
public class BillController {

    private static final int half_hour = (30 * 60);

    private static String uuid_ges = null;

    @Autowired
    private BillService billService;

    @Autowired
    private WalletService walletService;

    @Autowired
    private IPGService ipgService;


    @GetMapping
    public ResponseEntity<?> getBills(@RequestHeader(value = "Authorization") String authorization) {
        List<BillDto> billDtoList = this.billService.getAllBills(UserUtils.getProfile().getGroup(), UserUtils.getProfile().getUsername());
        return ResponseEntity.ok().body(billDtoList);
    }

    @PostMapping(value = "/create")
    public ResponseEntity<?> createBill(@Valid @RequestBody BillCreate billCreate,
                                        @RequestHeader(value = "Authorization") String authorization,
                                        HttpServletResponse response) throws Exception {
        BillDto billDto = billService.createBill(billCreate, UserUtils.getProfile().getGroup(), UserUtils.getProfile().getUsername());
        Cookie responseCookie = new Cookie("eTag", billDto.getId().toString());
        responseCookie.isHttpOnly();
        responseCookie.setPath("/api/bill");
        //TODO:responseCookie.setDomain("sepehrasadiyan.me");
        responseCookie.setMaxAge(half_hour);
        //TODO: .secure(true)
        response.addCookie(responseCookie);
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, responseCookie.toString()).body(billDto);
    }

    @PostMapping(value = "/payment")
    public ResponseEntity<?> payBill(@CookieValue(name = "eTag") String eTag,
                                     @RequestHeader(value = "Authorization") String authorization
            , @Valid @RequestBody(required = true) BillCreate billCreate) throws Exception {
        // do login Payment and redirect link for script
        Bill billStatusCreate = this.billService.getBill(eTag);
        if (billStatusCreate.getBillState().getBillState() == 0) {
            Wallet wallet = this.walletService.checkWalletBeforePayment(UserUtils.getProfile().getGroup(),
                    UserUtils.getProfile().getUsername());
            SepTokenResponse sepTokenResponse = ipgService.getTokenFromIPG(billStatusCreate);
            Bill bill = billService.changeBillState(billStatusCreate, null);
            return ResponseEntity.ok().body(sepTokenResponse);
        }
        throw new Exception("Error in find right bill.");
        //success
    }

    @PutMapping(value = "/cancel")
    public ResponseEntity<?> cancelBill(@CookieValue(name = "eTag", required = true) String eTag,
                                        @RequestHeader(value = "Authorization") String authorization,
                                        HttpServletResponse response) throws Exception {
        if (billService.cancelBill(UserUtils.getProfile().getGroup(), UserUtils.getProfile().getUsername())) {
            Cookie cookie = new Cookie("eTag", null);
            cookie.setMaxAge(0);
            //TODO:cookie.setSecure(true);
            cookie.setHttpOnly(true);
            response.addCookie(cookie);
            return ResponseEntity.status(HttpStatus.OK).body("{\"success\" : true}");
        } else {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("{\"success\" : false");
        }
    }

    @GetMapping(value = "/needetag")
    public ResponseEntity<?> needETag(@RequestHeader(value = "Authorization") String authorization,
                                      HttpServletResponse httpServletResponse) throws Exception {
        Cookie cookie = billService.getActiveCookieValue();
        if (cookie != null) {
            httpServletResponse.addCookie(cookie);
            return ResponseEntity.status(HttpStatus.OK).body("{\"success\" : true}");
        }
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("{\"success\" : false}");
    }


}
