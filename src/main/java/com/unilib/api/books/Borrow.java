package com.unilib.api.books;

import com.unilib.api.shared.exceptions.ApplicationException;
import com.unilib.api.shared.exceptions.InvalidArgumentException;
import com.unilib.api.users.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Entity @Table(name = "book_borrows")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class Borrow {
    @Id @GeneratedValue
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book book;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Enumerated(EnumType.STRING)
    private BorrowStatus status;

    @Column(name = "release_at")
    private Instant releaseAt;

    @Column(name = "expires_at")
    private Instant expiresAt;

    public void accept(){
        if(!this.status.equals(BorrowStatus.WAITING)){
            throw new InvalidArgumentException("Invalid borrow state.");
        }

        this.status = BorrowStatus.IN_PROGRESS;
    }

    public void complete(){
        if(!this.status.equals(BorrowStatus.IN_PROGRESS)){
            throw new InvalidArgumentException("Invalid borrow state.");
        }
        this.status = BorrowStatus.COMPLETED;
    }
}
