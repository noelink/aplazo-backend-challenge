package com.aplazo.mx.creditlines.service;

import com.aplazo.mx.creditlines.controller.dto.request.CustomerRequest;
import com.aplazo.mx.creditlines.controller.dto.response.CustomerResponse;

public interface CustomerService {
    CustomerResponse createCustomer(CustomerRequest customerRequest);
}
