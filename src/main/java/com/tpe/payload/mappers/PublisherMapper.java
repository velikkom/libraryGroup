package com.tpe.payload.mappers;

import com.tpe.entity.concretes.business.Publisher;
import com.tpe.payload.request.PublisherRequest;
import com.tpe.payload.response.PublisherResponse;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Data
@Component
@RequiredArgsConstructor
public class PublisherMapper
{
    public Publisher mapPublisherRequestToLesson(PublisherRequest publisherRequest)
    {
        return Publisher.builder()
                .name(publisherRequest.getName())
                .build();
    }
    public PublisherResponse mapPublisherToPublisherResponse(Publisher publisher)
    {
        return PublisherResponse.builder()
                .id(publisher.getId())
                .name(publisher.getName())
                .builtIn(publisher.getBuiltIn())
                .build();
    }
}
