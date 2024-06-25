package com.tpe.repository;

import com.tpe.entity.concretes.business.Publisher;
import com.tpe.payload.response.PublisherResponse;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PublisherRepository extends JpaRepository<Publisher ,Long>
{

    PublisherResponse findPublisherById(Long id);

    //boolean existsPublisherName(String publisherName);

    boolean existsPublisherByNameEqualsIgnoreCase(String publisherName );
}
