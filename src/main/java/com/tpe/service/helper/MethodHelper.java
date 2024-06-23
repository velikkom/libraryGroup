package com.tpe.service.helper;

import com.tpe.entity.concretes.user.User;
import com.tpe.exception.BadRequestException;
import com.tpe.exception.ResourceNotFoundException;
import com.tpe.payload.messages.ErrorMessages;
import com.tpe.repository.LoanRepository;
import com.tpe.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;


@Component
@RequiredArgsConstructor
public class MethodHelper {


    private final UserRepository userRepository;
    private final LoanRepository loanRepository;




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

    public boolean userHasOverdueBooks(Long userId) {
        return loanRepository.findOverdueLoansByUserId(userId).stream()
                .anyMatch(loan -> loan.getReturnDate().isBefore(LocalDate.now().atStartOfDay()));
    }

    public boolean canUserBorrowMoreBooks(User user) {
        int currentLoanCount = loanRepository.countByUserId(user.getId());
        int maxBooksAllowed = calculateMaxBooks(user.getScore());
        return currentLoanCount < maxBooksAllowed;
    }

    public int calculateMaxBooks(int score) {
        if (score >= 2) return 5;
        if (score == 1) return 4;
        if (score == 0) return 3;
        if (score == -1) return 2;
        return 1; // Default to 1 for score -2 and below
    }

    public long calculateLoanDuration(int score) {
        if (score >= 2) return 20;
        if (score == 1) return 15;
        if (score == 0) return 10;
        if (score == -1) return 6;
        return 3; // Default to 3 days for score -2 and below
    }


    public void updateUserScore(User user, boolean isOnTime) {
        int currentScore = user.getScore();
        user.setScore(isOnTime ? currentScore + 1 : currentScore - 1);
        userRepository.save(user);
    }

}