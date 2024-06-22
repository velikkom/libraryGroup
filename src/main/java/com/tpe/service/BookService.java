package com.tpe.service;

import com.tpe.entity.concretes.business.Book;
import com.tpe.exception.ConflictException;
import com.tpe.exception.ResourceNotFoundException;
import com.tpe.payload.mappers.BookMappers;
import com.tpe.payload.messages.ErrorMessages;
import com.tpe.payload.messages.SuccessMessages;
import com.tpe.payload.request.BookRequest;
import com.tpe.payload.response.BookResponse;
import com.tpe.payload.response.ResponseMessage;
import com.tpe.repository.BookRepository;
import com.tpe.service.helper.MethodHelper;
import com.tpe.service.helper.PageableHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.AbstractPersistable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.persistence.metamodel.SingularAttribute;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
    private final BookMappers bookMappers;
    private final PageableHelper pageableHelper;
    private final MethodHelper methodHelper;

    //GET 1
    public Page<BookResponse> getAllBookByPage(String q,
                                               Long catId,
                                               Long authorId,
                                               Long publisherID,
                                               int page,
                                               int size,
                                               String sort,
                                               String type) {

        Pageable pageable = pageableHelper.getPageableWithProperties(q, catId, authorId, publisherID, page, size, sort, type);
        return bookRepository.findAll(pageable).map(bookMappers::mapBookToBookResponse);
    }






   /* //GET 2
    public List<BookResponse> getAll() {
        return bookRepository.findAll()
                .stream()
                .map(bookMappers::mapBookToBookResponse)
                .collect(Collectors.toList());
    }*/


    //YARDIMCI METHOD
    private Book isBookExistById(Long id) {
        return bookRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException(String.format(ErrorMessages.BOOK_NOT_FOUND_MESSAGE, id)));
    }


    @Transactional
    public ResponseMessage<BookResponse> createBook(BookRequest bookRequest) {

        isBookExistByBookName(bookRequest.getName());
        if (validateISBN(bookRequest.getIsbn())) {
            return ResponseMessage.error("Invalid ISBN formant", null);

        }
        if (!validateShelfCode(bookRequest.getShelfCode())) {
            return ResponseMessage.error("Invalid shelf code format.", null);
        }

        Book book = Book.builder()
                .name(bookRequest.getName())
                .isbn(bookRequest.getIsbn())
                .pageCount(bookRequest.getPageCount())
                .authorId(bookRequest.getAuthorId())
                .publisherId(bookRequest.getPublisherId())
                .publishDate(bookRequest.getPublishDate())
                .categoryId(bookRequest.getCategoryId())
                .imageUrl(bookRequest.getImageUrl())
                .shelfCode(bookRequest.getShelfCode())
                .featured(bookRequest.isFeatured())
                .active(true)
                .builtIn(false)
                .createDate(LocalDateTime.now())
                .loanable(true)
                .build();

        book = bookRepository.save(book);
        BookResponse bookResponse = bookMappers.mapBookToBookResponse(book);
        return ResponseMessage.success("Book created successfully", bookResponse);
    }

        /*Book savedBook = bookRepository.save(bookMappers.mapBookRequestToBook(bookRequest));

        return ResponseMessage.<BookResponse>builder()
                .object(bookMappers.mapBookToBookResponse(savedBook))
                .message(SuccessMessages.BOOK_SAVE)
                .httpStatus(HttpStatus.CREATED)
                .build();*/


    private boolean validateISBN(String isbn) {
        return isbn != null && isbn.matches("\\d{3}-\\d{2}-\\d{5}-\\d{2}-\\d");
    }

    private boolean validateShelfCode(String shelfCode) {
        return shelfCode != null && shelfCode.matches("[A-Z]{2}-\\d{3}");
    }

    private boolean isBookExistByBookName(String name) {
        boolean bookExist = bookRepository.existsByNameIgnoreCase(name);

        if (bookExist) {
            throw new ConflictException(String.format(ErrorMessages.BOOK_ALREADY_EXIST_WITH_BOOK_NAME, name));
        } else return false;
    }


    //PUT
    public BookResponse updateBookById(Long id, BookRequest bookRequest) {
        Book book1 = isBookExistById(id);
        if (
                !(book1.getName().equals(bookRequest.getName())) &&
                        (bookRepository.existsByNameIgnoreCase(bookRequest.getName()))
        ) {
            throw new ConflictException(String.format(ErrorMessages.BOOK_ALREADY_EXIST_WITH_BOOK_NAME,
                    bookRequest.getName()));
        }
        Book updatedBook = bookMappers.mapBookRequestToUpdatedBook(id, bookRequest);
        updatedBook.setName(book1.getName());
        Book savedBook = bookRepository.save(updatedBook);

        return bookMappers.mapBookToBookResponse(savedBook);
    }


    //DELETE
    public ResponseMessage deleteBook(Long id, HttpServletRequest httpServletRequest) {

        isBookExistById(id);
        bookRepository.deleteById(id);   //??? repository bak?
        return ResponseMessage.builder()
                .message(SuccessMessages.BOOK_DELETE)
                .httpStatus(HttpStatus.OK)
                .build();

    }

    public BookResponse findById(Long id) {
        Book book = bookRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(ErrorMessages.BOOK_NOT_FOUND_MESSAGE));
        return book != null ? bookMappers.mapBookToBookResponse(book) : null;
    }

    public ResponseMessage<BookResponse> updateBook(Long id, BookRequest bookRequest) {
        Book book = bookRepository.findById(id).orElse(null);
        if (book == null) {
            return ResponseMessage.error("Book not found with ID: " + id, null);
        }

        updateBookDetails(book, bookRequest);
        book = bookRepository.save(book);
        return ResponseMessage.success("Book updated successfully", mapBookToBookResponse(book));
    }

    private void updateBookDetails(Book book, BookRequest request) {
        book.setName(request.getName());
        book.setIsbn(request.getIsbn());
        book.setPageCount(request.getPageCount());
        book.setAuthorId(request.getAuthorId());
        book.setPublisherId(request.getPublisherId());
        book.setPublishDate(request.getPublishDate());
        book.setCategoryId(request.getCategoryId());
        book.setImageUrl(request.getImageUrl());
        book.setShelfCode(request.getShelfCode());
        book.setFeatured(request.isFeatured());
        // More fields can be updated similarly
    }

    private BookResponse mapBookToBookResponse(Book book) {
        return BookResponse.builder()
                .bookId(book.getId())
                .name(book.getName())
                .isbn(book.getIsbn())
                .pageCount(book.getPageCount())
                .authorId(book.getAuthorId())
                .publisherId(book.getPublisherId())
                .publishDate(book.getPublishDate())
                .categoryId(book.getCategoryId())
                .imageUrl(book.getImageUrl())
                .shelfCode(book.getShelfCode())
                .active(book.isActive())
                .featured(book.isFeatured())
                .createDate(book.getCreateDate())
                .loanable(book.isLoanable())
                .build();
    }
}

