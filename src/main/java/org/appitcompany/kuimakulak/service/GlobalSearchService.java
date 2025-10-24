package org.appitcompany.kuimakulak.service;

import org.appitcompany.kuimakulak.dto.ClientRequest;
import org.appitcompany.kuimakulak.dto.GlobalSearchResponse;
import org.appitcompany.kuimakulak.dto.PaginationResponse;
import org.appitcompany.kuimakulak.enums.Filed;

import java.util.List;
import java.util.Map;

public interface GlobalSearchService {
    PaginationResponse<GlobalSearchResponse> globalSearch(String keyword, int pageSize, int pageNumber);

    PaginationResponse<GlobalSearchResponse> globalSearchFilter( ClientRequest clientRequest, int pageSize, int pageNumber);

}
