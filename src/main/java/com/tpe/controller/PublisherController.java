package com.tpe.controller;

import com.tpe.entity.concretes.business.Publisher;
import com.tpe.exception.ResourceNotFoundException;
import com.tpe.payload.messages.ErrorMessages;
import com.tpe.payload.messages.SuccessMessages;
import com.tpe.payload.request.PublisherRequest;
import com.tpe.payload.response.PublisherResponse;
import com.tpe.service.PublisherService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/publishers")
@RequiredArgsConstructor
public class PublisherController
{
    private final PublisherService publisherService;
    private final PublisherRequest publisherRequest;

    //http:localhost:8080/publishers/5
    @GetMapping("{id}")
    public PublisherResponse getPublisherById(@PathVariable Long id)
    {
        Publisher publisher = publisherService.findPublisherById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorMessages.PUBLISHER_NOT_FOUND_MESSAGE));
        return new PublisherResponse();
    }

    //http:localhost:8080/publishers/create
    @PostMapping("/create")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<PublisherResponse> createPublisher(@Valid @RequestBody PublisherRequest request) {
        PublisherResponse response= publisherService.createPublisher(request);
        return new  ResponseEntity<>(HttpStatus.CREATED);
    }



}
