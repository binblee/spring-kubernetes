package com.example.bookservice;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import com.example.serviceapis.Rating;

@RestController
public class BookServiceController {

    @Autowired
    RatingClient ratingClient;

    private List<Book> bookList = Arrays.asList(
        new Book(1L, "Spring Cloud Book", "John Doe"),
        new Book(2L, "Kubernetes Book", "Zhang San")
    );

    @GetMapping("/")
    public String healthCheck(){
        return "book service ok";
    }

    @GetMapping("/books")
    public List<Book> findAllBooks() {
        return bookList;
    }

    @GetMapping("/books/{bookId}")
    public Book findBook(@PathVariable Long bookId) {
        return bookList.stream().filter(b -> 
                b.getId().equals(bookId)
            ).findFirst().orElse(null);
        
    }

    @GetMapping("/books/{bookId}/ratings")
    public List<Rating> findBookRatings(@PathVariable long bookId) {
        return ratingClient.rating(bookId);
    }

    @GetMapping("/pingratingsvc")
    public String pingRatingService(){
        return ratingClient.pingRatingService();
    }
    
}
