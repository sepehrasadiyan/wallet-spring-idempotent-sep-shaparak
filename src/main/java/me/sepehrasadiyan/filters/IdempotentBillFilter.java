package me.sepehrasadiyan.filters;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.sepehrasadiyan.exception.ErrorMessage;
import me.sepehrasadiyan.services.bill.BillService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.Clock;
import java.time.Instant;

@Component
public class IdempotentBillFilter implements Filter {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private BillService billService;

    public IdempotentBillFilter(BillService billService) {
        this.billService = billService;
    }

    @Override
    public void doFilter(ServletRequest servletRequest,
                         ServletResponse servletResponse,
                         FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest req = null;
        HttpServletResponse res = null;
        try {
            req = (HttpServletRequest) servletRequest;
            res = (HttpServletResponse) servletResponse;
            if (!billService.validateIncomingRequestForIdempotentEffect(req)) {
                res.sendError(HttpStatus.CONFLICT.value(), "You Have one Request in process payment.");
                return;
            }
            filterChain.doFilter(servletRequest, servletResponse);
        }catch (Exception e){
            ErrorMessage errorMessage = new ErrorMessage();
            errorMessage.setMessage("INTERNAL SERVER ERROR.");
            errorMessage.setTimestamp(Timestamp.from(Instant.now(Clock.systemDefaultZone())));
            errorMessage.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            res.setStatus(errorMessage.getStatusCode());
            res.getWriter().write(MAPPER.writeValueAsString(errorMessage));
        }
    }
}
