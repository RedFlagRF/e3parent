package com.e3.search.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Author RedFlag
 * @Description 全局异常处理器
 * @Date 19:39 2019/2/13
 */
public class GlobalExceptionResolver implements HandlerExceptionResolver {
    public static final Logger logger = LoggerFactory.getLogger(GlobalExceptionResolver.class);

    @Override
    public ModelAndView resolveException(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) {
        //打印控制台
        e.printStackTrace();
        //写入日志
//        logger.debug("测试输出日志");
//        logger.info("系统发生异常");
        logger.error("系统发生异常", e);
        //发邮件 jmail工具包,短信 第三方webservice
        //显示错误页面
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("error/exception");
        return modelAndView;
    }
}
