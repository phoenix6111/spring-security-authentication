package com.phoenix.security.core.social;

import org.springframework.web.servlet.view.AbstractView;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * User: sheng
 * Date: 2018-04-04 1:39
 * Description: 社交绑定与解除绑定结果View
 */
public class PhoenixConnectView extends AbstractView {
    @Override
    protected void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setContentType("text/html;charset=UTF-8");
        if (model.get("connection") == null) {
            response.getWriter().write("<h3>解绑成功</h3>");
        } else {
            response.getWriter().write("<h3>绑定成功</h3>");
        }
    }
}
