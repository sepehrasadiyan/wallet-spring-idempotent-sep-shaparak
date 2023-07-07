package me.sepehrasadiyan.model.registry;


import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;
import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "registry_pre_update")
public class RegistryPreUpdate {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "registryPreUpdateId", updatable = false, nullable = false)
    private UUID registryPreUpdateId;

    @Column(updatable = false, nullable = false)
    private String username;

    @Column(updatable = false, nullable = false)
    private String BID;

    @Column(updatable = false, nullable = false)
    private Timestamp modifyTime;

    @Column(nullable = false)
    private Double updatedBalance;

    @Column(nullable = false)
    private Double lastBalance;

    @Column(nullable = false)
    private Double amount;

    @Column(updatable = false)
    private UUID billId;

    @Column(updatable = false)
    @Enumerated(EnumType.ORDINAL)
    private RegistryType type;

    @Column(updatable = false)
    private Double amountChange;

    @Column(unique = true)
    private UUID paymentInfoId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonBackReference
    private Registry registry;


    @Override
    public boolean equals(Object o){
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RegistryPreUpdate registryPreUpdate = (RegistryPreUpdate) o;
        return Objects.equals(BID, registryPreUpdate.BID) &&
                Objects.equals(registryPreUpdateId, registryPreUpdate.registryPreUpdateId);
    }

    @Override
    public int hashCode(){
        return Objects.hash(BID,registryPreUpdateId);
    }
}
