package me.sepehrasadiyan.model.wallet;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;
import java.util.UUID;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Wallet {

  @Id
  @GeneratedValue(generator = "uuid2")
  @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
  @Column(columnDefinition = "uuid_binary", name = "id", updatable = false, nullable = false)
  private UUID id;

  @Column(unique = true, updatable = false, nullable = false)
  private String BID;

  @Column(nullable = false)
  private String lastUserAccess;

  @Column(nullable = false)
  private Timestamp lastAccessTime;

  @Column(nullable = false)
  private Double currentBalance;

  @Column(nullable = false)
  private UUID lastRegistryPreUpdateID;

  private boolean enable;

  @Version
  private Long version;


  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Wallet wallet = (Wallet) o;
    return Objects.equals(BID, wallet.BID) &&
            Objects.equals(id, wallet.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(BID, id);
  }

  @Override
  protected void finalize() throws Throwable {
    try {
      // Perform cleanup actions here, such as closing connections or releasing resources
      // For example, if your wallet has a database connection, you can close it in the finalize() method
    } finally {
      {
        super.finalize();
      }
    }
  }
}
