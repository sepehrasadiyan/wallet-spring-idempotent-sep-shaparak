package me.sepehrasadiyan.services.wallet;


import lombok.extern.slf4j.Slf4j;
import me.sepehrasadiyan.repository.registry.RegistryRepository;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.PostPersist;

@Slf4j
public class WalletListener {
    //TODO: complete as your wish write the great logging system and recovery service.
    //      for example after a success add money call try to logging correctly.
    //      you can use AOP too :).

    @Autowired
    private RegistryRepository registryRepository;

    @PostPersist
    private void createNewWallet(){
        //TODO:Log properly creating new wallet.
    }





}
