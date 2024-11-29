package com.example.project.repository;

import com.example.project.models.Transaction;
import com.example.project.spec.TransactionQuerySpec;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface FinancialTransactionRepository extends PagingAndSortingRepository<Transaction, Long>,JpaRepository<Transaction, Long>,
        JpaSpecificationExecutor<Transaction> {

//    @Query("SELECT ft FROM Transaction ft WHERE " +
//            "(:#{#querySpec.dateFrom} IS NULL OR ft.dateFrom >= :#{#querySpec.dateFrom}) AND " +
//            "(:#{#querySpec.dateTo} IS NULL OR ft.dateTo <= :#{#querySpec.dateTo}) AND " +
//            "(:#{#querySpec.userId} IS NULL OR ft.userId = :#{#querySpec.userId}) AND " +
//            "(:#{#querySpec.service} IS NULL OR ft.service = :#{#querySpec.service}) AND " +
//            "(:#{#querySpec.status} IS NULL OR ft.status = :#{#querySpec.status}) AND " +
//            "(:#{#querySpec.reference} IS NULL OR ft.reference = :#{#querySpec.reference})")
//    Page<Transaction> findFilteredTransactions(TransactionQuerySpec querySpec, Pageable pageable);
}
