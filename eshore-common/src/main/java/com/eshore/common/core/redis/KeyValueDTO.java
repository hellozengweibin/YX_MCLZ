package com.eshore.common.core.redis;

import lombok.Data;

@Data
public class KeyValueDTO<R,T> {

    private R key;
    private T value;
}
