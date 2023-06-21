package me.sepehrasadiyan.controller.registry;


import me.sepehrasadiyan.services.bill.BillService;
import me.sepehrasadiyan.services.registry.RegistryService;
import me.sepehrasadiyan.util.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/registry")
public class RegistryController {

    @Autowired
    private RegistryService registryService;

    @Autowired
    private BillService billService;

    @GetMapping
    public ResponseEntity<?> getLastActivities(@RequestHeader(value = "Authorization") String authorization
                                                ,@RequestParam(value = "pageNumber") int pageNumber,
                                                @RequestParam(value = "pageSize") int pageSize) throws Exception{
        return ResponseEntity.ok(registryService.getActivities(UserUtils.getProfile().getGroup(),
                PageRequest.of(pageNumber, pageSize, Sort.Direction.DESC, "modifyTime")));
    }

}
