package com.unilib.api.service;

import com.unilib.api.domain.book.Book;
import com.unilib.api.domain.review.Review;
import com.unilib.api.domain.review.ReviewRequestDTO;
import com.unilib.api.domain.user.User;
import com.unilib.api.repositories.BooksRepository;
import com.unilib.api.repositories.ReviewsRepository;
import com.unilib.api.repositories.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
public class ReviewsService {
    @Autowired
    private AuthService authService;

    @Autowired
    private ReviewsRepository reviewsRepository;

    @Autowired
    private BooksRepository booksRepository;

    @Autowired
    private UsersRepository usersRepository;

    public Review create(ReviewRequestDTO request){
        Optional<User> user = this.authService.getCurrentUser();

        if(user.isEmpty()){
            throw new UsernameNotFoundException("User not found");
        }

        Optional<Book> book = this.booksRepository.findById(request.book_id());

        if(book.isEmpty()){
            throw new IllegalArgumentException("Book not found");
        }

        Review newReview = new Review();

        newReview.setUser(user.get());
        newReview.setBook(book.get());
        newReview.setRating(request.rating());
        newReview.setCreatedAt(new Date());

        this.reviewsRepository.save(newReview);

        return newReview;
    }
}
