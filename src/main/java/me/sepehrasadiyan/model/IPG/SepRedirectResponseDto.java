package me.sepehrasadiyan.model.IPG;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SepRedirectResponseDto {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    //You Reference Number
    @JsonProperty("RefNum")
    private String refNum;

    //Purchase Number we create (billId)
    @JsonProperty("ResNum")
    private String resNum;

    //Terminal Number
    @JsonProperty("MID")
    private String MID;

    //Transaction State String
    @JsonProperty("State")
    private String state;

    //Transaction State Number
    @JsonProperty("RefNum")
    private Integer status;

    //Reference No
    @JsonProperty("Rrn")
    private String RRN;

    //Terminal Number
    @JsonProperty("TerminalId")
    private Integer terminalId;

    //TraceNumber
    @JsonProperty("TraceNo")
    private String traceNo;

    @JsonProperty("Amount")
    private Long amount;

    @JsonProperty("Wage")
    private Long wage;

    //Card Number that pay the bill
    @JsonProperty("SecurePan")
    private String securePan;

    @JsonProperty("Token")
    private String token;

    @JsonProperty("HashedCardNumber")
    private String hashedCardNumber;


    @JsonCreator
    public static SepRedirectResponseDto fromJson(String json) {
        try {
            return OBJECT_MAPPER.readValue(json, SepRedirectResponseDto.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}