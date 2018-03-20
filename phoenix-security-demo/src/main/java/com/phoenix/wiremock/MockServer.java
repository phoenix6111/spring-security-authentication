package com.phoenix.wiremock;

import com.github.tomakehurst.wiremock.client.WireMock;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;

public class MockServer {

    public static void main(String[] args) throws IOException {
        WireMock.configureFor(8062);
        WireMock.removeAllMappings();

        mock("/order/1","01");
        mock("/order/2","02");
    }

    private static void mock(String url,String fileName) throws IOException {
        ClassPathResource resource = new ClassPathResource("mock/response/"+fileName+".txt");
        Object[] datas = FileUtils.readLines(resource.getFile(),"UTF-8").toArray();
        String content = StringUtils.join(datas,"\n");

        WireMock.stubFor(
                WireMock.get(WireMock.urlPathEqualTo(url))
                        .willReturn(
                                WireMock.aResponse()
                                        .withBody(content)
                                        .withStatus(200)
                        )
        );
    }

}
