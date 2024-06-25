package com.tpe.service;

import com.tpe.entity.concretes.business.Publisher;
import com.tpe.exception.ConflictException;
import com.tpe.payload.mappers.PublisherMapper;
import com.tpe.payload.messages.ErrorMessages;
import com.tpe.payload.messages.SuccessMessages;
import com.tpe.payload.request.PublisherRequest;
import com.tpe.payload.response.PublisherResponse;
import com.tpe.payload.response.ResponseMessage;
import com.tpe.repository.PublisherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PublisherService
{
    private final PublisherRepository publisherRepository;
    private final PublisherMapper publisherMapper;
    //private final

    public Optional<Publisher> findPublisherById(Long id)
    {
        return publisherRepository.findById(id);
    }




    private boolean isPublisherExistByName(String publisherName)
    {
       boolean existsPublisherName= publisherRepository.existsPublisherByNameEqualsIgnoreCase(publisherName);

       if (existsPublisherName)
       {
           throw new ConflictException(String.format(ErrorMessages.PUBLISHER_ALREADY_EXIST_WITH_NAME));
       }else return false;
    }

    public ResponseMessage<PublisherResponse> savePublisher(PublisherRequest request)
    {
        isPublisherExistByName(request.getName());

        Publisher savedPublisher = publisherRepository.save(publisherMapper.mapPublisherRequestToLesson(request));

        return ResponseMessage.<PublisherResponse>builder()
                .object(publisherMapper.mapPublisherToPublisherResponse(savedPublisher))
                .message(SuccessMessages.PUBLISHER_CREATED)
                .httpStatus(HttpStatus.CREATED)
                .build();
    }
}
