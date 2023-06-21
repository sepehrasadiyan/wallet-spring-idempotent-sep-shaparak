package me.sepehrasadiyan.model.IPG;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TransactionDetail {

    @JsonProperty("RRN")
    private String RRN;

    @JsonProperty("RefNum")
    private String refNum;

    @JsonProperty("MaskedPan")
    private String maskedPan;

    @JsonProperty("HashedPan")
    private String hashedPan;

    @JsonProperty("TerminalNumber")
    private int terminalNumber;

    @JsonProperty("OriginalAmount")
    private long originalAmount;

    @JsonProperty("AffectiveAmount")
    private long affectiveAmount;

    @JsonProperty("StraceDate")
    private String straceDate;

    @JsonProperty("StraceNo")
    private String straceNo;
}
