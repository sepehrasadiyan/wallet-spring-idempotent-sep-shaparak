package me.sepehrasadiyan.model.wallet.bill;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BillCreate {

    @NotNull(message = "amount is null.")
    @Min(value = 10000, message = "min value should be 10000")
    private Long amount;

}
