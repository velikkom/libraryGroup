package com.tpe.payload.response;

import com.tpe.entity.concretes.business.Book;
import com.tpe.entity.concretes.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
public class LoanResponse
{


    private Long id;
    private Long userId;
    private Long bookId;
    private LocalDateTime loanDate;
    private LocalDateTime returnDate;
    private LocalDateTime expireDate;
    private Book book;
    private User user;
    private String notes;
}
