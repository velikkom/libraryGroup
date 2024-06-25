package com.tpe.repository;

import com.tpe.entity.concretes.business.Loan;
import com.tpe.payload.response.LoanResponse;
import com.tpe.service.LoanService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface LoanRepository extends JpaRepository<Loan,Long> {

    Page<Loan> findByUserId(Long userId, Pageable pageable);

    Page<Loan> findLoansByBookId(Long bookId, Pageable pageable);

    @Override
    @EntityGraph(attributePaths = {"user", "book"})
    Optional<Loan> findById(Long loanId);

  /*  @Query("SELECT COUNT(l) FROM Loan l WHERE l.userId = :userId AND l.returnDate IS NULL")
    int countByUserId(Long id);*/

//    @Query("SELECT COUNT(l) FROM Loan l WHERE l.user.id = :userId AND l.returnDate IS NULL")
//    int countByUserId(@Param("userId") Long userId);

    /*@Query(value = "SELECT COUNT(*) FROM loans WHERE user_id = :userId AND return_date IS NULL", nativeQuery = true)
    int countByUserId(@Param("userId") Long userId);*/





//    @Query("SELECT l FROM Loan l WHERE l.userId = :userId AND l.returnDate < CURRENT_DATE")
//    List<Loan> findOverdueLoansByUserId(Long userId);

    @Query("SELECT l FROM Loan l JOIN FETCH l.user u WHERE u.id = :userId AND l.returnDate < CURRENT_DATE")
    List<Loan> findOverdueLoansByUserId(@Param("userId") Long userId);

}
