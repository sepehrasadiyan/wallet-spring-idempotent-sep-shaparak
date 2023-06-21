package me.sepehrasadiyan.config;


import me.sepehrasadiyan.filters.IdempotentBillFilter;
import me.sepehrasadiyan.filters.MultiBillFilter;
import me.sepehrasadiyan.services.bill.BillService;
import org.modelmapper.ModelMapper;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;



@Configuration
public class AppMainConfig {

    @Bean
    public FilterRegistrationBean<MultiBillFilter> billFilterFilterRegistrationBean(){
        FilterRegistrationBean<MultiBillFilter> registrationBean
                = new FilterRegistrationBean<>();
        registrationBean.setFilter(new MultiBillFilter(billServiceForMultiBillFilter()));
        registrationBean.addUrlPatterns("/api/bill/create");
        return registrationBean;
    }

    @Bean
    public FilterRegistrationBean<IdempotentBillFilter> idempotentBillFilterFilterRegistrationBean(){
        FilterRegistrationBean<IdempotentBillFilter> registrationBean
                = new FilterRegistrationBean<>();
        registrationBean.setFilter(new IdempotentBillFilter(billServiceForFilterIdempotent()));
        registrationBean.addUrlPatterns("/api/bill/payment");
        return registrationBean;
    }

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Bean("billServiceForFilterIdempotent")
    public BillService billServiceForFilterIdempotent(){
        return new BillService();
    }

    @Bean("billServiceForMultiBillFilter")
    public BillService billServiceForMultiBillFilter(){
        return new BillService();
    }

    @Bean("billServiceForAddCookie")
    public BillService billServiceForAddCookie(){
        return new BillService();
    }

/*    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jacksonObjectMapperCustomization() {
        return jacksonObjectMapperBuilder ->
                jacksonObjectMapperBuilder.timeZone(TimeZone.getDefault());
    }*/

    /*    @Bean
    public FilterRegistrationBean<AddRemovedCookie> addRemovedCookieFilterRegistrationBean(){
        FilterRegistrationBean<AddRemovedCookie> registrationBean
                = new FilterRegistrationBean<>();
        registrationBean.setFilter(new AddRemovedCookie(billServiceForAddCookie()));
        registrationBean.addUrlPatterns("/api/bill/payment");
        registrationBean.setOrder(200);
        return registrationBean;
    }*/


}
