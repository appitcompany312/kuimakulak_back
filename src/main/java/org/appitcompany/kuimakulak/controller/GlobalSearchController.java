package org.appitcompany.kuimakulak.controller;

import lombok.RequiredArgsConstructor;
import org.appitcompany.kuimakulak.dto.GlobalSearchResponse;
import org.appitcompany.kuimakulak.dto.PaginationResponse;
import org.appitcompany.kuimakulak.service.GlobalSearchService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
}
