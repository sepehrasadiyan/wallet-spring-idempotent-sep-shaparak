package me.sepehrasadiyan.controller.wallet;

import me.sepehrasadiyan.model.wallet.WalletDto;
import me.sepehrasadiyan.services.wallet.ValidationWalletErrorService;
import me.sepehrasadiyan.services.wallet.WalletService;
import me.sepehrasadiyan.model.wallet.Wallet;
import me.sepehrasadiyan.util.UserUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(value = "/api/wallet")
public class WalletController {

    @Autowired
    private WalletService walletService;

    @Autowired
    private ValidationWalletErrorService validationWalletErrorService;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping
    public ResponseEntity<?> getWallet(@RequestHeader(value = "Authorization") String authorization) throws Exception{
        Wallet wallet = walletService.getWallet(UserUtils.getProfile().getGroup());
        if (wallet == null) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.ok().body(modelMapper.map(wallet, WalletDto.class));
    }

    @PostMapping(value = "/balance")
    public ResponseEntity<?> balanceAccountWithInvoice(@RequestBody WalletMemDto walletMemDto,
                                                    @RequestHeader(value = "Authorization") String authorization) throws Exception{
        return (walletService.getAccountBalanceForInvoice(walletMemDto))? ResponseEntity.ok().build() : ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @GetMapping(value = "/currentbalance")
    public ResponseEntity<?> getCurrentBalance(@RequestHeader(value = "Authorization") String authorization) throws Exception{
        return walletService.getCurrentBalance();
    }


}
