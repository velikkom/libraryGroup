package com.tpe.service;

import com.tpe.entity.concretes.business.Publisher;
import com.tpe.exception.ConflictException;
import com.tpe.payload.mappers.PublisherMapper;
import com.tpe.payload.messages.ErrorMessages;
import com.tpe.payload.request.PublisherRequest;
import com.tpe.payload.response.PublisherResponse;
import com.tpe.repository.PublisherRepository;
import lombok.RequiredArgsConstructor;
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


    public PublisherResponse createPublisher(PublisherRequest request)
    {
        isPublisherExistByName(request.getName());
        publisherRepository.save(publisherMapper)
                //todo mapper klass create
    }

    private boolean isPublisherExistByName(String publisherName)
    {
       boolean existsPublisherName= publisherRepository.existsPublisherName(publisherName);

       if (existsPublisherName)
       {
           throw new ConflictException(String.format(ErrorMessages.PUBLISHER_ALREADY_EXIST_WITH_NAME));
       }else return false;
    }
}
