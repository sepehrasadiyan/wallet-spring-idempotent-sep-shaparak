package me.sepehrasadiyan.model.IPG;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SepRedirectResponse {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    //You Reference Number
    @Id
    @Column(name = "ref_num" , updatable = false)
    private String refNum;

    //Purchase Number we create (billId)
    @Column(name = "res_num" , updatable = false)
    private String resNum;

    //Terminal Number
    @Column(name = "mid" , updatable = false)
    private String MID;

    //Transaction State String
    @Column(name = "state" , updatable = false)
    private String state;

    //Transaction State Number
    @Column(name = "status" , updatable = false)
    private Integer status;

    //Reference No
    @Column(name = "rrn" , updatable = false)
    private String RRN;

    //Terminal Number
    @Column(name = "terminal_id" , updatable = false)
    private Integer terminalId;

    //TraceNumber
    @Column(name = "trace_no" , updatable = false)
    private String traceNo;

    @Column(name = "amount" , updatable = false)
    private Long amount;

    @Column(name = "wage" , updatable = false)
    private Long wage;

    //Card Number that pay the bill
    @Column(name = "secure_pan" , updatable = false)
    private String securePan;

    @Column(name = "token" , updatable = false)
    private String token;

    @Column(name = "hashed_card_number" , updatable = false)
    private String hashedCardNumber;


    @JsonCreator
    public static SepRedirectResponse fromJson(String json) {
        try {
            return OBJECT_MAPPER.readValue(json, SepRedirectResponse.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
