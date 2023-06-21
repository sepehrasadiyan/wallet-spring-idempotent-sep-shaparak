package me.sepehrasadiyan.model.registry;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "registry")
public class Registry {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "registryId", updatable = false)
    private UUID registryID;

    @Column(unique = true, updatable = false, nullable = false)
    private String BID;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "registry", fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<RegistryPreUpdate> registryPreUpdates;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Registry registry = (Registry) o;
        return Objects.equals(BID, registry.BID) &&
                Objects.equals(registryID, registry.registryID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(BID, registryID);
    }


}
