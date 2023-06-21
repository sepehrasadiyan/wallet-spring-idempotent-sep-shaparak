package me.sepehrasadiyan.model.IPG;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SepTxn {

    /**
     * TODO: Every created token only expire 20MIN
     */

    //TODO: For get token we set this parameter to Token.(inCaseSensitive).
    private String action;
    //TODO: This number always the same and get it from the IPG in Contract.
    private String TerminalId;
    //TODO:
    private String RedirectUrl;
    //TODO: we should create a Unique number for transaction and set it BillId.
    private String ResNum;
    //TODO: Amount.
    private long Amount;

    //TODO:fee of transaction
    private long Wage;

    //Discount or someThing like this.
    private long AffectiveAmount;

    //TODO:Phone Number.
    private long CellNumber;


}
