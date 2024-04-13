package com.example.elasticsearch.search;

import lombok.Data;

public @Data class PagedRequestDTO {

    private static final int DEFAULT_SIZE = 100;

    private int page;
    private int size;

}
