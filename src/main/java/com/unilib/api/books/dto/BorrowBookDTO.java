package com.unilib.api.books.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.unilib.api.shared.serializers.InstantDeserializer;
import com.unilib.api.shared.serializers.InstantSerializer;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;

public record BorrowBookDTO(@JsonSerialize(using = InstantSerializer.class)
                            @JsonDeserialize(using = InstantDeserializer.class)
                            @Nullable Instant release,
                            @JsonSerialize(using = InstantSerializer.class)
                            @JsonDeserialize(using = InstantDeserializer.class)
                            @NotNull Instant expiration) {
}
