package com.example.elasticsearch.search;

import lombok.Data;
import org.elasticsearch.search.sort.SortOrder;

import java.util.List;

public @Data class SearchRequestDTO extends PagedRequestDTO {
    private List<String> fields;
    private String searchTerm;
    private String sortBy;
    private SortOrder order;


}