package com.nada.pool.controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

/**
 * @ClassName nada
 * @Author lihai
 * @Date 2020/10/27 9:13 上午
 * @Version 1.0
 */
@RestController
public class IndexController {

    public static String tulingCookie;
    public static String tulingHeader;

    @RequestMapping("/")
    public ModelAndView index(Model model){
        ModelAndView view = new ModelAndView();
        view.addObject("tulingCookie",tulingCookie);
        view.addObject("tulingHeader",tulingHeader);
        view.setViewName("index");
        return view;
    }
}
