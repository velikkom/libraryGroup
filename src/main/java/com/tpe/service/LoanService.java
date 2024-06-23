package com.tpe.service;

import com.tpe.entity.concretes.business.Book;
import com.tpe.entity.concretes.business.Loan;
import com.tpe.entity.concretes.user.User;
import com.tpe.exception.ResourceNotFoundException;
import com.tpe.payload.mappers.LoanMapper;
import com.tpe.payload.messages.ErrorMessages;
import com.tpe.payload.request.LoanRequest;
import com.tpe.payload.response.LoanResponse;
import com.tpe.payload.response.ResponseMessage;
import com.tpe.repository.BookRepository;
import com.tpe.repository.LoanRepository;
import com.tpe.repository.UserRepository;
import com.tpe.service.helper.MethodHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.net.FileNameMap;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class LoanService {

    private final LoanRepository loanRepository;
    private final LoanMapper loanMapper;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;
    private final MethodHelper methodHelper;


    public Page<LoanResponse> findAllLoans(Pageable pageable) {
        return loanRepository.findAll(pageable).map(loanMapper::convertToLoanResponse);
    }


    public LoanResponse findById(Long loanId) {
        Loan loan = loanRepository.findById(loanId).orElseThrow(() -> new ResourceNotFoundException(String.format(ErrorMessages.NOT_FOUND_LOAN, loanId)));
        return loanMapper.convertToLoanResponse(loan);
    }

    //    public Page<LoanResponse> findLoansByUserId(Pageable pageable, Long userId)
//    {
//        return loanRepository.findByUserId(userId, pageable).map(loanMapper::convertToLoanResponse);
//    }
//
//    public Page<LoanResponse> findAllLoansByBookId(Pageable pageable, Long bookId)
//    {
//        return loanRepository.findLoansByBookId(bookId,pageable).map(loanMapper::convertToLoanResponse);
//    }
    public Page<LoanResponse> findLoansByCriteria(Pageable pageable, Long userId, Long bookId) {
        if (userId != null) {
            return loanRepository.findByUserId(userId, pageable).map(loanMapper::convertToLoanResponse);
        } else if (bookId != null) {
            return loanRepository.findLoansByBookId(bookId, pageable).map(loanMapper::convertToLoanResponse);
        } else {
            throw new IllegalArgumentException("Either userId or bookId must be provided");
        }
    }

    public LoanResponse findLoanById(Long loanId) {
        Loan loan = loanRepository.findById(loanId).orElseThrow(() -> new ResourceNotFoundException(ErrorMessages.NOT_FOUND_LOAN));
        return loanMapper.convertToLoanResponseWithUser(loan);

    }

    public LoanResponse createLoan(LoanRequest loanRequest) {
        User user = userRepository.findById(loanRequest.getUserId()).orElseThrow(() -> new ResourceNotFoundException(ErrorMessages.NOT_FOUND_USER_MESSAGE));

        Book book = bookRepository.findById(loanRequest.getBookId()).orElseThrow(() -> new ResourceNotFoundException(ErrorMessages.BOOK_NOT_FOUND_MESSAGE));

        //Check if the book is loanable
        if (!book.isLoanable()) {
            return null;
        }


        // Check user's current loan count and overdue status

        if (methodHelper.userHasOverdueBooks(user.getId()) || !methodHelper.userHasOverdueBooks(user.getId()))
        {
            throw new RuntimeException(ErrorMessages.USER_CAN_NOT_BORROW_BOOK);
        }
        //create loan
        LocalDateTime loanDate = LocalDateTime.now();
        LocalDateTime expireDate = loanDate.plusDays(methodHelper.calculateLoanDuration(user.getScore()));

        Loan loan = new Loan();
        loan.setUser(user);
        loan.setBook(book);
        loan.setLoanDate(loanDate);
        loan.setExpireDate(expireDate);
        loan.setNotes(loanRequest.getNotes());

        book.setLoanable(false);
        loanRepository.save(loan);

        return loanMapper.convertToLoanResponseCreate(loan);

    }

    public LoanResponse updateLoan(Long loanId, LoanRequest request)
    {
        Loan loan = loanRepository.findById(loanId).orElseThrow(() -> new ResourceNotFoundException(ErrorMessages.NOT_FOUND_LOAN));
        loan.setNotes(request.getNotes());
        loan.setExpireDate(request.getExpireDate());

        if (request.getReturnDate() != null) {
            loan.setReturnDate(request.getReturnDate());
            Book book = loan.getBook();
            book.setLoanable(true);
            bookRepository.save(book);
            methodHelper.updateUserScore(loan.getUser(), loan.getReturnDate().isBefore(loan.getExpireDate()));
        }
        loanRepository.save(loan);
        return loanMapper.convertToLoanResponseUpdate(loan);


    }
}
