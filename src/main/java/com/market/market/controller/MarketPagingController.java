package com.market.market.controller;

import com.market.market.service.MarketPagingService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 가게 커서 기반 페이지네이션 API의 controller 입니다.
 */
@RestController
@RequestMapping("/market")
@RequiredArgsConstructor
public class MarketPagingController {

    private final MarketPagingService marketPagingService;

    @GetMapping("/paging")
    public void findMarketByCursorId(@RequestParam("cursorId") Long cursorId,
                                     @RequestParam("size") Integer size) {
        marketPagingService.findMarketByCursorId(cursorId, size);
    }
}
