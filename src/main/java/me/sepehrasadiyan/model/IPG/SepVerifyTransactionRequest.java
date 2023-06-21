package me.sepehrasadiyan.model.IPG;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SepVerifyTransactionRequest {

    private String RefNum;

    private long TerminalNumber;


}
