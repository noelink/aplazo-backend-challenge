package com.aplazo.mx.creditlines.util.mapper;

import com.aplazo.mx.creditlines.controller.dto.request.CustomerRequest;
import com.aplazo.mx.creditlines.controller.dto.response.CustomerResponse;
import com.aplazo.mx.creditlines.repository.entity.Customer;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CustomerMapper {

    Customer toCustomer(CustomerRequest customerRequest);

    CustomerResponse toCustomerResponse(Customer customer);
}
