package com.aplazo.mx.creditlines.util.mapper;

import com.aplazo.mx.creditlines.controller.dto.request.CustomerRequest;
import com.aplazo.mx.creditlines.controller.dto.response.CustomerResponse;
import com.aplazo.mx.creditlines.repository.entity.Customer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CustomerMapper {
    CustomerMapper INSTANCE = Mappers.getMapper(CustomerMapper.class);

    Customer toCustomer(CustomerRequest customerRequest);

    @Mapping(source = "idClient", target = "idCliente")
    @Mapping(source = "creationDate", target = "creationDate")
    @Mapping(source = "creditLineAmount", target = "creditLineAmount")
    @Mapping(target = "availableCreditLineAmount", source = "creditLineAmount")
    CustomerResponse toCustomerResponse(Customer customer);
}
