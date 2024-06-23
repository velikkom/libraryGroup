package com.tpe.payload.mappers;

import com.tpe.entity.concretes.business.Loan;
import com.tpe.payload.response.LoanResponse;
import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public class LoanMapper {
    public LoanResponse convertToLoanResponse(Loan loan) {
        LoanResponse response = new LoanResponse();
        response.setId(loan.getId());
        response.setUserId(loan.getUserId());
        response.setBookId(loan.getBookId());
        response.setLoanDate(loan.getLoanDate());
        response.setReturnDate(loan.getReturnDate());
        response.setExpireDate(loan.getExpireDate());
        response.setBook(loan.getBook());  // Assuming the book object is fully populated and fetched lazily.
        return response;
    }

    public LoanResponse convertToLoanResponseWithUser(Loan loan) {
        LoanResponse response = new LoanResponse();
        response.setId(loan.getId());
        response.setUserId(loan.getUserId());
        response.setBookId(loan.getBookId());
        response.setUser(loan.getUser()); // Assuming getUser() returns the User associated with the loan.
        response.setBook(loan.getBook()); // Assuming getBook() returns the Book associated with the loan.
        return response;
    }

    public LoanResponse convertToLoanResponseCreate(Loan loan) {
        LoanResponse response = new LoanResponse();
        response.setId(loan.getId());
        response.setUserId(loan.getUser().getId());
        response.setBookId(loan.getBook().getId());
        response.setBook(loan.getBook());
        return response;
    }
    public LoanResponse convertToLoanResponseUpdate(Loan loan) {
        LoanResponse response = new LoanResponse();
        response.setId(loan.getId());
        response.setUserId(loan.getUser().getId());
        response.setBookId(loan.getBook().getId());
        return response;
    }

}