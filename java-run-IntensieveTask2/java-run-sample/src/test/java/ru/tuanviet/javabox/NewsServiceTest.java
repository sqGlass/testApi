package ru.tuanviet.javabox;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;

public class NewsServiceTest {

    @Test
    public void shouldReturnFormattedNews() {
        List<NewsModel> models = getNews();

        List<String> output = new NewsService(models).formNewsOutput();
        List<String> requiredOutput = getRequiredOutput();

        assertThat(output).isEqualTo(requiredOutput);
    }

    public List<NewsModel> getNews() {
        ObjectMapper mapper = new ObjectMapper();
        List<NewsModel> models = new ArrayList<>();
        try {
            models.add(mapper.readValue(new File("src/test/resources/__files/get-topnews/news1.json"),
                                                            NewsModel.class));
            models.add(mapper.readValue(new File("src/test/resources/__files/get-topnews/news2.json"),
                                                            NewsModel.class));
            models.add(mapper.readValue(new File("src/test/resources/__files/get-topnews/news3.json"),
                                                            NewsModel.class));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return models;
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
                , "null"
        ));
        outputs.add(String.format("%s (%s points)\n%s\n"
                , "Dead-End SF Street Plagued with Confused Waymo Cars Trying to Turn Around"
                , "35"
                , "https://sanfrancisco.cbslocal.com/2021/10/13/dead-end-sf-street-plagued-with-confused-waymo-cars-trying-to-turn-around-every-5-minutes/"
        ));
        return outputs;
    }

}
