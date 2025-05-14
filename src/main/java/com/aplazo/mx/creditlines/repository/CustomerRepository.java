package com.aplazo.mx.creditlines.repository;

import com.aplazo.mx.creditlines.repository.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CustomerRepository extends JpaRepository<Customer, UUID> {
}
