package com.unilib.api.schedulers;

import com.unilib.api.books.Borrow;
import com.unilib.api.books.BorrowStatus;
import com.unilib.api.books.repositories.BorrowedBooksRepository;
import com.unilib.api.config.jpa.specifications.BorrowedBooksSpecification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Period;
import java.util.List;

@Component
public class BorrowScheduler {
    private final BorrowedBooksRepository borrowedBooksRepository;
    private final long RATE = 60 * 60 * 1000;
    Logger logger = LoggerFactory.getLogger(BorrowScheduler.class);

    public BorrowScheduler(BorrowedBooksRepository borrowedBooksRepository){
        this.borrowedBooksRepository = borrowedBooksRepository;
    }

    @Scheduled(fixedRate = RATE)
    public void process(){
        logger.info("Finding expired borrows...");
        List<Borrow> expiredBorrows = this.borrowedBooksRepository
                .findAll(BorrowedBooksSpecification.isExpired());

        logger.info("Found {} expired borrows", expiredBorrows.size());

        try {
            borrowedBooksRepository.deleteAllInBatch(expiredBorrows);
        } catch (Exception e) {
            logger.error("Error occurred while deleting expired borrows: {}" ,e.getMessage());
        }

        logger.info("Finding completed borrows...");
        List<Borrow> completed = this.borrowedBooksRepository
                .findAll(BorrowedBooksSpecification.isCompleted());

        logger.info("Found {} completed borrows", completed.size());

        try {
            completed.forEach(b -> b.setStatus(BorrowStatus.COMPLETED));
            borrowedBooksRepository.saveAll(completed);
        } catch (Exception e) {
            logger.error("Error occurred while completing borrows: {}" ,e.getMessage());
        }

    }
}
