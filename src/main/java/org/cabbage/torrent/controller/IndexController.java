package org.cabbage.torrent.controller;

import org.cabbage.torrent.dto.DataDTO;
import org.cabbage.torrent.dto.SearchDTO;
import org.cabbage.torrent.entity.BaseResult;
import org.cabbage.torrent.service.SearchService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
public class IndexController {

    @Resource(name = "zhongziso")
    //@Resource(name = "sobt")
    private SearchService searchService;

    @GetMapping("")
    public ModelAndView baseType(HttpServletRequest request) {
        return new ModelAndView("index");
    }

    @GetMapping(value = "/mobile")
    public ModelAndView mobile(HttpServletRequest request) {
        return new ModelAndView("mobileIndex.html");
    }

    @GetMapping(value = "/data")
    public Object data(SearchDTO searchDTO) {
        List<DataDTO> list = null;
        try {
            list = searchService.data(searchDTO);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new BaseResult(list);
    }

    @GetMapping(value = "/torrent")
    @ResponseBody
    public Object torrent(String url, String type) {
        BaseResult br = null;
        try {
            br = searchService.torrent(url, type);
        } catch (Exception e) {
            e.printStackTrace();
            br = new BaseResult(0, "系统异常");
        }
        return br;
    }
}
