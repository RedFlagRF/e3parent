package com.e3.search.controller;

import com.e3.commons.pojo.SearchResult;
import com.e3.search.service.SearchService;
import org.apache.solr.client.solrj.SolrServerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;

/**
 * @Author RedFlag
 * @Description 商品搜索Controller
 * @Date 22:44 2019/2/11
 */
@Controller
public class SearchController {
    @Value("${SEARCH_RESULT_ROWS}")
    private Integer SEARCH_RESULT_ROWS;

    @Autowired
    private SearchService searchService;

    @RequestMapping("/search")
    public String searchItemList(String keyword, @RequestParam(defaultValue = "1") Integer page, Model model) throws IOException, SolrServerException {
//        keyword = new String(keyword.getBytes("iso-8859-1"), "utf-8");
      //  int i=5/0;
        SearchResult result = searchService.search(keyword, page, SEARCH_RESULT_ROWS);
        model.addAttribute("query", keyword);
        model.addAttribute("totalPages", result.getTotalPages());
        model.addAttribute("page", page);
        model.addAttribute("recourdCount", result.getRecordCount());
        model.addAttribute("itemList", result.getItemList());
        return "search";
    }
}
