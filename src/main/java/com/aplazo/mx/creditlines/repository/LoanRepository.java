package com.aplazo.mx.creditlines.repository;

import com.aplazo.mx.creditlines.repository.entity.Loan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface LoanRepository extends JpaRepository<Loan, UUID> {

}
