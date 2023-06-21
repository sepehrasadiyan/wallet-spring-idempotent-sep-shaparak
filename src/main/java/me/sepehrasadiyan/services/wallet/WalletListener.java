package me.sepehrasadiyan.services.wallet;

import me.sepehrasadiyan.repository.registry.RegistryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.PostPersist;

public class WalletListener {
    private static Logger LOGGER = LoggerFactory.getLogger(WalletListener.class);

    @Autowired
    private RegistryRepository registryRepository;

    @PostPersist
    private void createNewWallet(){

    }





}
