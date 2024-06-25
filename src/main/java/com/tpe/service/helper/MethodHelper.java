package com.tpe.service.helper;

import javax.persistence.EntityManager;
import com.tpe.entity.concretes.business.Loan;
import com.tpe.entity.concretes.user.User;
import com.tpe.exception.BadRequestException;
import com.tpe.exception.ResourceNotFoundException;
import com.tpe.payload.messages.ErrorMessages;
import com.tpe.repository.LoanRepository;
import com.tpe.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.time.LocalDate;


@Component
@RequiredArgsConstructor
public class MethodHelper {


    private final UserRepository userRepository;
    //private final LoanRepository loanRepository;
    //private final EntityManager entityManager;




    // !!! isUserExist
    public User isUserExist(Long userId) {
        return userRepository.findById(userId).orElseThrow(() ->
                new ResourceNotFoundException(String.format(ErrorMessages.NOT_FOUND_USER_MESSAGE,
                        userId)));
    }



    // !!! checkBuiltIn
    public void checkBuiltIn(User user){
        if(Boolean.TRUE.equals(user.getBuiltIn())) {
            throw new BadRequestException(ErrorMessages.NOT_PERMITTED_METHOD_MESSAGE);
        }
    }
    //validateISBN
    public static boolean validateISBN(String isbn) {
        return isbn != null && isbn.matches("\\d{3}-\\d{2}-\\d{5}-\\d{2}-\\d");
    }

    //validate shelf code
    public static boolean validateShelfCode(String shelfCode) {
        return shelfCode != null && shelfCode.matches("[A-Z]{2}-\\d{3}");
    }

    /*public boolean userHasOverdueBooks(Long userId) {
        return loanRepository.findOverdueLoansByUserId(userId).stream()
                .anyMatch(loan -> loan.getReturnDate().isBefore(LocalDate.now().atStartOfDay()));
    }*/



}