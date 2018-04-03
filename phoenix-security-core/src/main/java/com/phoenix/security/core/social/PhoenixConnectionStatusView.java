package com.phoenix.security.core.social;


import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.connect.Connection;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.AbstractView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * User: sheng
 * Date: 2018-04-04 0:21
 * Description:查看用户所有社交登陆授权的View，具体查看org.springframework.social.connect.web.ConnectController.connectionStatus()方法
 */
@Component("connect/status")
public class PhoenixConnectionStatusView extends AbstractView {

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    protected void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) throws Exception {

        //获取所有的社交providerId-Connection组成的Map
        Map<String, List<Connection<?>>> connections = (Map<String, List<Connection<?>>>) model.get("connectionMap");
        //我们想要页面呈现的只是一组社交按钮，以及该用户在该社交平台上是否已经绑定过了，所以只需要providerId-boolean的数据即可
        Map<String,Boolean> result = new HashMap<>();
        for(String key : connections.keySet()) {
            result.put(key,CollectionUtils.isNotEmpty(connections.get(key)));
        }

        /**
         * 将数据以json形式返回给客户端
         */
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(objectMapper.writeValueAsString(result));
    }
}
