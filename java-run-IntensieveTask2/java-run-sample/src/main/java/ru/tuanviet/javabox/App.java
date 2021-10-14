package ru.tuanviet.javabox;

import com.fasterxml.jackson.core.type.TypeReference;

import java.util.ArrayList;
import java.util.List;

public class App {
    private final String url;
    private final String startNewsUrl;
    private final int countNews;

    public App(String url, int countNews, String startNewsUrl) {
        this.url = url;
        this.countNews = countNews;
        this.startNewsUrl = startNewsUrl;
    }

    public static void main(String[] args) {
        List<String> formattedList = new App(
                "https://hacker-news.firebaseio.com/v0/topstories.json",
                10,
                "https://hacker-news.firebaseio.com/v0/item/")
                .getFormattedNewsFromTopNews();

        for (String news : formattedList) {
            System.out.println(news);
        }
    }

    public List<String> getFormattedNewsFromTopNews() {
        List<String> listIds = getTopNewsId();
        List<NewsModel> newsModels = getNewsFromIds(listIds);
        return new NewsService(newsModels).formNewsOutput();
    }

    public List<String> getTopNewsId() {
        HttpClient client = new HttpClient();
        List<String> ids = client.fetch(url, new TypeReference<List<String>>(){});
        return ids;
    }

    public List<NewsModel> getNewsFromIds(List<String> ids) {
        HttpClient client = new HttpClient();
        List<NewsModel> news = new ArrayList<>(countNews);
        final String endUrl = ".json";
        for (int i = 0; i < countNews; i++) {
            String url = startNewsUrl + ids.get(i) + endUrl;
            news.add(client.fetch(url, NewsModel.class));
            formUrl(news.get(i), url);
        }
        return news;
    }

    public void formUrl(NewsModel model, String url) {
        if (model.getUrl() == null) {
            model.setUrl(url);
        }
    }
}
