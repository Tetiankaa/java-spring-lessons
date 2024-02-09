package com.example.javaspringlessons.service;

import com.example.javaspringlessons.entity.Review;
import com.example.javaspringlessons.repository.ReviewDAO;
import com.example.javaspringlessons.repository.WebSocketSessionRepository;
import org.springframework.stereotype.Service;

@Service
public class ReviewService {

    private final ReviewDAO reviewDAO;
    private final WebSocketSessionRepository webSocketSessionRepository;


    public ReviewService(ReviewDAO reviewDAO, WebSocketSessionRepository webSocketSessionRepository) {
        this.reviewDAO = reviewDAO;
        this.webSocketSessionRepository = webSocketSessionRepository;
    }

    public void saveReview(int productId, String text){

        Review review = new Review();
        review.setText(text);
        review.setProductId(productId);
        reviewDAO.save(review);

        webSocketSessionRepository.sendToAll("New Review: %s".formatted(text));
    }

}
