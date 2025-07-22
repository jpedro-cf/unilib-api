package com.unilib.api.shared;

public interface Validator<T,K> {
     K validate(T request);
}
