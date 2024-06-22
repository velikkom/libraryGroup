package com.tpe.controller;

import com.tpe.entity.concretes.business.Book;
import com.tpe.payload.messages.SuccessMessages;
import com.tpe.payload.request.BookRequest;
import com.tpe.payload.response.BookResponse;
import com.tpe.payload.response.ResponseMessage;
import com.tpe.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

import static org.springframework.data.jpa.domain.AbstractPersistable_.id;

@RestController
@RequiredArgsConstructor
@RequestMapping("/books")

public class BookController {

    private final BookService bookService;



    //1.ENDPOINT GET
    @GetMapping("/books")  //http://localhost/8080/books?q=sefiller&cat=4&author=34&publisher=42&page=1&size=10&sort=name&type=asc
    @PreAuthorize("hasAnyAuthority( 'ADMIN','STAFF','MEMBER')")
    public Page<BookResponse> getAllBookByPage(

            @RequestParam(required = false) String q,
            @RequestParam(required = false) Long catId,
            @RequestParam(required = false) Long authorId,
            @RequestParam(required = false) Long publisherID,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size",defaultValue = "20") int size,
            @RequestParam(value = "sort",defaultValue = "name") String sort,
            @RequestParam(value = "type",defaultValue = "asc") String type){

        // return bookService.getAllBookByPage(category_id,author_id, publisher_id,page,size,sort,type);
        return bookService.getAllBookByPage(q, catId, authorId, publisherID, page, size, sort, type);
    }


    //2. ENDPOINT GET
    @GetMapping("/{bookId}") //http://localhost/8080/books/5
    @PreAuthorize("hasAnyAuthority( 'ADMIN','STAFF','MEMBER')")
    public ResponseEntity<BookResponse> getAll(@PathVariable Long bookId) {
       BookResponse book = bookService.findById( bookId);
       if (book !=null){
           return ResponseEntity.ok(book);
       }else return ResponseEntity.notFound().build();
    }


    //http://localhost/8080/books
    @PostMapping ("/books")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseMessage<BookResponse> createBook(@RequestBody @Valid BookRequest bookRequest){

        return bookService.createBook(bookRequest);

    }

    //http://localhost/8080/books/5 +UPTADE+JSON

    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @PutMapping("/books/{id}") //http://localhost:8080//books/5
    public ResponseEntity<ResponseMessage<BookResponse>> updateBookById(@PathVariable Long id,
                                                       @RequestBody BookRequest bookRequest){
        ResponseMessage<BookResponse> response = bookService.updateBook(id, bookRequest);
        return ResponseEntity.ok(response);

    }
    //todo delete will continue

    //5.ENDPOINT DELETE

    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @DeleteMapping("/books/{id}") // http://localhost:8080/meet/2
    public ResponseMessage deleteBook(@PathVariable Long id, HttpServletRequest httpServletRequest){
        return bookService.deleteBook(id, httpServletRequest);
    }






}
