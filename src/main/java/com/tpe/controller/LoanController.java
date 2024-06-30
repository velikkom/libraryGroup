package com.tpe.controller;

import com.tpe.payload.messages.SuccessMessages;
import com.tpe.payload.request.LoanRequest;
import com.tpe.payload.response.LoanResponse;
import com.tpe.payload.response.ResponseMessage;
import com.tpe.service.LoanService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

@RestController
@RequestMapping("/loans")
@RequiredArgsConstructor
//@AllArgsConstructor
public class LoanController {

    private final LoanService loanService;

    //http:localhost:8080/loans? page = size 10& sort loanDate& type desc
    @GetMapping
    @PreAuthorize("hasAnyAuthority('MEMBER')")
    public Page<LoanResponse> listLoans(
            @PageableDefault(size = 10, sort = "loanDate", direction = Sort.Direction.DESC)Pageable pageable)
    {
        return loanService.findAllLoans(pageable);
    }

    //http:localhost:8080/loans/5
    @GetMapping("/{loanId}")
    @PreAuthorize("hasAnyAuthority('MEMBER')")
    public ResponseMessage<LoanResponse> getLoansById(@PathVariable("loanId") @Valid Long loanId)
    {
       LoanResponse loanResponse= loanService.findById(loanId);
       return ResponseMessage.success(SuccessMessages.LOAN_VIEWED_SUCCESSFULLY,loanResponse);
    }

    //http:localhost:8080/loans/user/5?page=1&size=10&sort=loanDate&type=desc
    @GetMapping("/user/{userId}")
    @PreAuthorize("hasAnyAuthority('ADMIN','STAFF')")
    public Page<LoanResponse> getLoansByUSerId(
            @PathVariable Long userId,
            @PageableDefault(size = 10, page = 0, sort = "loanDate", direction = Sort.Direction.DESC)Pageable pageable)
    {
        return loanService.findLoansByCriteria(pageable,userId,null);
    }

    //http:localhost:8080/loans/book/5?page=1&size=10&sort=loanDate&type=desc
    @GetMapping("/book/{bookId}")
    @PreAuthorize("hasAnyAuthority('ADMIN','STAFF')")
    public Page<LoanResponse>getLoansByBookId(
            @PathVariable Long bookId,
            @PageableDefault(size = 10,page = 0,sort = "loanDate",direction = Sort.Direction.DESC)
            Pageable pageable)
    {
        return loanService.findLoansByCriteria(pageable,bookId,null);

    }


    //http:localhost:8080/loans/book/5
    @GetMapping("/auth/{loanId}")
    @PreAuthorize("hasAnyAuthority('ADMIN','STAFF')")
    public ResponseEntity<LoanResponse> getLoanByLoanId(
            @PathVariable Long loanId)
    {
      LoanResponse loanResponse =  loanService.findLoanById(loanId);
      return ResponseEntity.ok(loanResponse);
    }


    //http:localhost:8080/loans/create
    @PostMapping("/create")
    @PreAuthorize("hasAnyAuthority('ADMIN','STAFF')")
    public ResponseEntity<?> createLoans(@RequestBody LoanRequest loanRequest)
    {
      LoanResponse loanResponse =  loanService.createLoan(loanRequest);
      if (loanResponse==null)
      {
          return ResponseEntity.badRequest().body(Map.of("isAvailable",false));
      }
      return ResponseEntity.ok(loanResponse);
    }

    //http:localhost:8080/loans/update/5 + PUT + JSON
    @PostMapping("/update/{loanId}")
    @PreAuthorize("hasAnyAuthority('ADMIN','STAFF')")
    public ResponseEntity<LoanResponse> updateLoan(
            @PathVariable Long loanId,
            @RequestBody LoanRequest loanRequest)
    {
       LoanResponse updatedLoanResponse = loanService.updateLoan(loanId,loanRequest);
       return ResponseEntity.ok(updatedLoanResponse);
    }





}
