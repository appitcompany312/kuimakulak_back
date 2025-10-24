package org.appitcompany.kuimakulak.controller;

import lombok.RequiredArgsConstructor;
import org.appitcompany.kuimakulak.dto.ClientRequest;
import org.appitcompany.kuimakulak.dto.GlobalSearchResponse;
import org.appitcompany.kuimakulak.dto.PaginationResponse;
import org.appitcompany.kuimakulak.enums.Filed;
import org.appitcompany.kuimakulak.service.GlobalSearchService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/search")
public class GlobalSearchController {

    private final GlobalSearchService globalSearchService;

    @GetMapping("global")
    public PaginationResponse<GlobalSearchResponse> globalSearch(
            @RequestParam(defaultValue = "1") int pageNumber,
            @RequestParam(defaultValue = "12") int pageSize,
            @RequestParam String keyword){
        return globalSearchService.globalSearch(keyword,pageSize,pageNumber);
    }
    @PostMapping("global_filter")
    public PaginationResponse<GlobalSearchResponse> globalSearchFilter(
            @RequestParam(defaultValue = "1") int pageNumber,
            @RequestParam(defaultValue = "12") int pageSize,
            @RequestBody ClientRequest clientRequest){
        return globalSearchService.globalSearchFilter(clientRequest,pageSize,pageNumber);
    }
}
