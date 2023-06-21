package me.sepehrasadiyan.model.IPG;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class TransactionInfo {

    @Id
    private String refNum;

    private Long affectiveAmount;

    private UUID billId;

    private String straceDate;

    private Integer resultCode;

    private String resultDescription;

    private Integer terminalNumber;

    private boolean needReject;

    private boolean rejectSuccess;

}
