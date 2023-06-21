package me.sepehrasadiyan.model.wallet;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.sql.Timestamp;
import java.util.TimeZone;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WalletDto {
    private static final ObjectMapper MAPPER = new ObjectMapper();

    @NotBlank(message = "BID can not be blank.")
    private String BID;

    private Long currentBalance;

    private String lastUserAccess;

    private Timestamp lastAccessTime;

    @JsonCreator
    public static WalletDto fromJson(String json) {
        try {
            MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            MAPPER.setTimeZone(TimeZone.getDefault());
            return MAPPER.readValue(json, WalletDto.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
