package me.sepehrasadiyan.model.wallet.bill;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import me.sepehrasadiyan.model.enums.BillState;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.TimeZone;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BillDto {
    private static final ObjectMapper MAPPER = new ObjectMapper();

    private UUID id;

    private Double amount;

    private String description;

    private BillState billState;

    private Timestamp createTime;

    private Timestamp updateTime;

    private String rrn;

    private String traceNo;

    @JsonCreator
    public static BillDto fromJson(String json) {
        try {
            MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            MAPPER.setTimeZone(TimeZone.getDefault());
            return MAPPER.readValue(json, BillDto.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
