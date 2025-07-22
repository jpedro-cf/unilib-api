package com.unilib.api.shared.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;

public class ConflictException extends ApplicationException{
    public ConflictException(String message) {
        super(message);
    }

    @Override
    public ProblemDetail toProblemDetail(){
        ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.CONFLICT);

        problem.setTitle("Conflict.");
        problem.setDetail(this.getMessage());

        return problem;
    }
}
