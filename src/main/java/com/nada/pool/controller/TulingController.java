package com.nada.pool.controller;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.nada.pool.service.TulingTask;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @ClassName nada
 * @Author lihai
 * @Date 2020/10/27 9:13 上午
 * @Version 1.0
 */
@RestController
public class TulingController {

    public static String initTulingCookie;

    @RequestMapping("/initTuling")
    public ModelAndView index(HttpServletRequest request, HttpServletResponse httpResponse){
        ModelAndView view = new ModelAndView();
        String cookie = request.getParameter("cookie");
        if(StringUtils.isEmpty(cookie)){
            view.setViewName("index");
            return view;
        }
        initTulingCookie = cookie;
        Boolean isOk = TulingTask.checkLogin();
        view.setViewName("index");
        view.addObject("checkLogin",isOk);
        view.addObject("initTulingCookie",initTulingCookie);
        return view;
    }
}
