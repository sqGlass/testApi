package ru.tuanviet.javabox;


import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;
import static org.assertj.core.api.Assertions.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import org.junit.Rule;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AppTest {

    @Rule
    public WireMockRule wm = new WireMockRule(
            options().dynamicPort()
    );

    @Test
    public void shouldAnswerWithTrue() {
        assertThat(true).isTrue();
    }

    @Test
    public void shouldGetFormattedNewsFromTopNewsWithMock()  {
        final String testServiceUri = serviceWithResponse("/top-ids", "topnews-ids.json");
        serviceWithResponse("/item/28860713.json", "news1.json");
        serviceWithResponse("/item/28839573.json", "news2.json");
        serviceWithResponse("/item/28855065.json", "news3.json");

        List<String> formattedList = new App(
                wm.baseUrl() + testServiceUri,
                3,
                wm.baseUrl() + "/item/")
                .getFormattedNewsFromTopNews();


        List<String> requiredOutput = getRequiredOutput();
        assertThat(formattedList).isEqualTo(requiredOutput);

    }

    private String serviceWithResponse(String serviceUri, String responseFile) {
        ResponseDefinitionBuilder response = aResponse()
                .withBodyFile("get-topnews/" + responseFile);
        givenThat(
                get(urlEqualTo(serviceUri)).willReturn(response)
        );
        return serviceUri;
    }

    private List<String> getRequiredOutput() {
        List<String> outputs = new ArrayList<>();
        outputs.add(String.format("%s (%s points)\n%s\n"
                , "esbuild – An extremely fast JavaScript bundler"
                , "133"
                , "https://esbuild.github.io/"
        ));

        outputs.add(String.format("%s (%s points)\n%s\n"
                , "Ask HN: How do you memorize things you read?"
                ,  "105"
                , wm.baseUrl() + "/item/28839573.json"
        ));
        outputs.add(String.format("%s (%s points)\n%s\n"
                , "Dead-End SF Street Plagued with Confused Waymo Cars Trying to Turn Around"
                , "35"
                , "https://sanfrancisco.cbslocal.com/2021/10/13/dead-end-sf-street-plagued-with-confused-waymo-cars-trying-to-turn-around-every-5-minutes/"
        ));
        return outputs;
    }


}
