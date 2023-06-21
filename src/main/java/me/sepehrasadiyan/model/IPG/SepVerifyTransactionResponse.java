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
public class SepVerifyTransactionResponse {

    @JsonProperty("TransactionDetail")
    private TransactionDetail transactionDetail;

    @JsonProperty("ResultCode")
    private Integer resultCode;

    @JsonProperty("ResultDescription")
    private String resultDescription;

    @JsonProperty("Success")
    private boolean success;

}
