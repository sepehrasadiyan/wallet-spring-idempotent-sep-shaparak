package me.sepehrasadiyan.util;


import me.sepehrasadiyan.model.IPG.SepRedirectResponse;
import me.sepehrasadiyan.model.IPG.SepRedirectResponseDto;

public class IPGUtils {

    public static SepRedirectResponse dtoToAp(SepRedirectResponseDto sepRedirectResponseDto) throws Exception{
        SepRedirectResponse sepRedirectResponse = new SepRedirectResponse();
        sepRedirectResponse.setRRN(sepRedirectResponseDto.getRRN());
        sepRedirectResponse.setMID(sepRedirectResponseDto.getMID());
        sepRedirectResponse.setAmount(sepRedirectResponseDto.getAmount());
        sepRedirectResponse.setState(sepRedirectResponseDto.getState());
        sepRedirectResponse.setStatus(sepRedirectResponseDto.getStatus());
        sepRedirectResponse.setRefNum(sepRedirectResponseDto.getRefNum());
        sepRedirectResponse.setWage(sepRedirectResponseDto.getWage());
        sepRedirectResponse.setResNum(sepRedirectResponseDto.getResNum());
        sepRedirectResponse.setTraceNo(sepRedirectResponseDto.getTraceNo());
        sepRedirectResponse.setSecurePan(sepRedirectResponseDto.getSecurePan());
        sepRedirectResponse.setTerminalId(sepRedirectResponseDto.getTerminalId());
        return sepRedirectResponse;
    }


}