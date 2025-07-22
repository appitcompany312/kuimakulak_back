package org.appitcompany.kuimakulak.service;

import org.appitcompany.kuimakulak.dto.GlobalSearchResponse;
import org.appitcompany.kuimakulak.dto.PaginationResponse;

public interface GlobalSearchService {
    PaginationResponse<GlobalSearchResponse> globalSearch(String keyword, int pageSize, int pageNumber);
}
