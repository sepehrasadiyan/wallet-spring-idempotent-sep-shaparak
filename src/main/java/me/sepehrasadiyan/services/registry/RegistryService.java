package me.sepehrasadiyan.services.registry;

import me.sepehrasadiyan.exception.ResourceNotFound;
import me.sepehrasadiyan.model.wallet.Wallet;
import me.sepehrasadiyan.model.wallet.bill.Bill;
import me.sepehrasadiyan.repository.registry.RegistryPreUpdateRepository;
import me.sepehrasadiyan.repository.registry.RegistryRepository;
import me.sepehrasadiyan.model.registry.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class RegistryService {

    @Autowired
    private RegistryRepository registryRepository;

    @Autowired
    private RegistryPreUpdateRepository registryPreUpdateRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Transactional(propagation = Propagation.MANDATORY)
    public RegistryPreUpdate updateWalletRegistry(Wallet walletEdited,
                                                  Long amount,
                                                  @Nullable UUID paymentInfoId,
                                                  @Nullable Bill bill) throws Exception {
        Registry registry = registryRepository.findByBID(walletEdited.getBID());
        RegistryPreUpdate registryPreUpdate = new RegistryPreUpdate();
        registryPreUpdate.setRegistry(registry);
        registryPreUpdate.setUpdatedBalance(walletEdited.getCurrentBalance());
        registryPreUpdate.setModifyTime(walletEdited.getLastAccessTime());
        registryPreUpdate.setUsername(walletEdited.getLastUserAccess());
        registryPreUpdate.setAmount(amount);
        registryPreUpdate.setBID(walletEdited.getBID());
        //operation is depositMoney
        if (bill == null) {
            registryPreUpdate.setLastBalance(walletEdited.getCurrentBalance() + amount);
            registryPreUpdate.setType(RegistryType.WITHDRAW);
            registryPreUpdate.setAmountChange(amount *= -1);
            registryPreUpdate.setPaymentInfoId(paymentInfoId);
            return registryPreUpdateRepository.save(registryPreUpdate);
        } else {
            //operation is addMoney
            registryPreUpdate.setBillId(bill.getId());
            registryPreUpdate.setLastBalance(walletEdited.getCurrentBalance() - amount);
            registryPreUpdate.setType(RegistryType.ADD);
            registryPreUpdate.setAmountChange(amount);
            return registryPreUpdateRepository.save(registryPreUpdate);
        }
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public Registry addRegistry(Registry registry) throws Exception {
        Optional<Registry> registryOptional = Optional.ofNullable(registryRepository.findByBID(registry.getBID()));
        if (registryOptional.isPresent()) {
            throw new Exception("Registry Not found.");
        }
        return registryRepository.save(registry);
    }

    public RegistryPreUpdate getRegistryPreUpdate(UUID registryPreUpdate) throws Exception {
        return registryPreUpdateRepository.findById(registryPreUpdate).orElseThrow(Exception::new);
    }

    public RegistryDto getActivities(String BID, Pageable pageable) throws Exception{
        Page<RegistryPreUpdate> registryPreUpdatePage = registryPreUpdateRepository.findByBID(BID, pageable);
        if(registryPreUpdatePage.getSize() == 0){
            throw new ResourceNotFound("There is no Transaction.");
        }
        RegistryDto registryDto = new RegistryDto();
        List<RegistryPreUpdateDto> registryPreUpdateDtos = new ArrayList<>();
        for(RegistryPreUpdate registryPreUpdate : registryPreUpdatePage.getContent()){
            RegistryPreUpdateDto registryPreUpdateDto = RegistryPreUpdateDto.builder()
                    .lastBalance(registryPreUpdate.getLastBalance())
                    .type(registryPreUpdate.getType())
                    .modifyTime(registryPreUpdate.getModifyTime())
                    .username(registryPreUpdate.getUsername())
                    .updatedBalance(registryPreUpdate.getUpdatedBalance())
                    .build();
            registryPreUpdateDtos.add(registryPreUpdateDto);
        }
        registryDto.setRegistryPreUpdateDtos(registryPreUpdateDtos);
        return registryDto;
    }


}
