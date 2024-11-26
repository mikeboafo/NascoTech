package com.example.project.Repository;

import com.example.project.Models.FinancialTransaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface FinancialTransactionRepository extends JpaRepository<FinancialTransaction, Long> {

    @Query("SELECT ft FROM FinancialTransaction ft WHERE " +
            "(:dateFrom IS NULL OR ft.date >= :dateFrom) AND " +
            "(:dateTo IS NULL OR ft.date <= :dateTo) AND " +
            "(:userId IS NULL OR ft.userId = :userId) AND " +
            "(:service IS NULL OR ft.service = :service) AND " +
            "(:status IS NULL OR ft.status = :status) AND " +
            "(:reference IS NULL OR ft.reference = :reference)")
    Page<FinancialTransaction> findFilteredTransactions(@Param("dateFrom") String dateFrom,
                                                        @Param("dateTo") String dateTo,
                                                        @Param("userId") String userId,
                                                        @Param("service") String service,
                                                        @Param("status") String status,
                                                        @Param("reference") String reference,
                                                        Pageable pageable);
}
