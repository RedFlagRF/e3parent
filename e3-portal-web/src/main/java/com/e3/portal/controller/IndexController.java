package com.e3.portal.controller;

import com.e3.pojo.TbContent;
import com.e3.service.ContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * @Author RedFlag
 * @Description 首页Controller
 * @Date 17:21 2019/1/26
 */
@Controller
public class IndexController {
    @Value("${HOME_PAGE_ROTATIONCHART}")
    private Long HOME_PAGE_ROTATIONCHART;
    @Autowired
    private ContentService contentService;

    @RequestMapping("/index")
    public String shoIndex(Model model) {
        List<TbContent> adList = contentService.listConTentByCid(HOME_PAGE_ROTATIONCHART);
        model.addAttribute("ad1List", adList);
        return "index";
    }
}
