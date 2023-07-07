package me.sepehrasadiyan.model.wallet.bill;

import me.sepehrasadiyan.model.enums.BillState;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.sepehrasadiyan.model.wallet.Wallet;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.util.Objects;
import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "bill")
public class Bill {

  @Id
  @GeneratedValue(generator = "uuid2")
  @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
  @Column(columnDefinition = "uuid_binary", name = "id", updatable = false, nullable = false)
  private UUID id;

  @NotNull(message = "billState should not be null.")
  @Enumerated(EnumType.ORDINAL)
  @Column(name = "billState")
  private BillState billState;

  @NotNull(message = "Amount should not be null.")
  private Double amount;

  @NotNull(message = "AmountWithFee Should not be null.")
  private Double amountWithFee;

  @NotNull(message = "fee should not be null.")
  private Double fee;

  @NotBlank
  @Column(updatable = false, nullable = false)
  private String BID;

  @Column(updatable = false, nullable = false)
  private String username;

  private String description;

  @NotNull
  @Column(updatable = false, nullable = false)
  private Timestamp createTime;

  private Timestamp updateTime;

  private Integer errorCode;

  private String errorDescription;

  private Integer terminalId;

  private String traceNo;

  private String securePan;

  private String refNum;

  private Boolean rejectionSucceed;

  private Timestamp expTime;

  @Version
  private Integer Bill;


  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Bill bill = (Bill) o;
    return Objects.equals(BID, bill.BID) &&
            Objects.equals(id, bill.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(BID, id);
  }

  @Override
  protected void finalize() throws Throwable {
    try {
      //clone all bills alive in heap to not remove
    } finally {
      {
        super.finalize();
      }
    }
  }

}
