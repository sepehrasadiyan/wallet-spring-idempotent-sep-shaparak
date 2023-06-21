package me.sepehrasadiyan.model.registry;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegistryDto {
    private List<RegistryPreUpdateDto> registryPreUpdateDtos;

}
