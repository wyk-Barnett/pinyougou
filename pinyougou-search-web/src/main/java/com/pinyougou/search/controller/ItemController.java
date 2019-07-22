package com.pinyougou.search.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.search.service.ItemSearchService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author wangyangkun
 * @date 2019/7/21 0021 20:21
 */
@RestController
@RequestMapping("/itemSearch")
public class ItemController {

    @Reference
    private ItemSearchService itemSearchService;

    @RequestMapping("/search.do")
    public Map search(@RequestBody Map searchMap){
        return itemSearchService.search(searchMap);
    }
}
