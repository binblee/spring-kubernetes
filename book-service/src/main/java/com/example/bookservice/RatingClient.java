package com.example.bookservice;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import com.example.serviceapis.Rating;

@FeignClient(value = "rating-service", url = "${rating_service_url}")
public interface RatingClient {
    @GetMapping(value = "/")
    public String pingRatingService();

    @GetMapping(value = "/ratings?bookid={bookid}")
    public List<Rating> rating(@PathVariable Long bookid);
}
