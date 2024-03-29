package com.example.ratingservice;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.example.serviceapis.Rating;

@RestController
public class RatingServiceController {
	private List<Rating> ratingList = Arrays.asList(
        new Rating(1L, 1L, 2),
        new Rating(2L, 1L, 3),
        new Rating(3L, 2L, 4),
        new Rating(4L, 2L, 5)
    );

    @GetMapping("/")
    public String healthCheck(){
        return "rating service ok";
    }

    @GetMapping("/ratings")
    public List<Rating> findRatingsByBookId(@RequestParam Long bookid) {
        return bookid == null || bookid.equals(0L) 
            ? Collections.<Rating>emptyList()
            : ratingList.stream().filter(r -> 
                r.getBookId().equals(bookid)).collect(Collectors.toList()
                );
    }

    @GetMapping("/ratings/all")
    public List<Rating> findAllRatings() {
        return ratingList;
    }
    
}
