package com.WebLearning.WebLearning.Service;

import com.WebLearning.WebLearning.FormData.NewsFormDto;
import com.WebLearning.WebLearning.Models.News;
import com.WebLearning.WebLearning.Repository.NewsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class NewsService {

    @Autowired
    private NewsRepository newsRepository;

    public NewsFormDto getNewsById(Long id) {
        News news = newsRepository.findById(id).get();
        NewsFormDto newsDto = new NewsFormDto();
        newsDto.setTime(formatLocalDateTimeToString(news.getTime()));
        newsDto.setDescription(news.getDescription());
        newsDto.setTitle(news.getTitle());
        newsDto.setImagePage(news.getImagePage());
        newsDto.setDetail(news.getDetail());
        return newsDto;
    }

    public List<NewsFormDto> getAll()  {
        List<News> listNews = newsRepository.findByOrderByTimeDesc();
        List<NewsFormDto> listNewsDto = new ArrayList<>();
        for(News news: listNews){
            NewsFormDto newsDto = new NewsFormDto();
            newsDto.setTime(formatLocalDateTimeToString(news.getTime()));
            newsDto.setDescription(news.getDescription());
            newsDto.setTitle(news.getTitle());
            newsDto.setImagePage(news.getImagePage());
            newsDto.setId(news.getId());
            listNewsDto.add(newsDto);
        }
        return listNewsDto;
    }

    public List<NewsFormDto> getTopSixNews() {
        List<News> listNews = newsRepository.findTop6ByOrderByTimeDesc();
        List<NewsFormDto> listNewsDto = new ArrayList<>();
        for(News news: listNews){
            NewsFormDto newsDto = new NewsFormDto();
            newsDto.setTime(formatLocalDateTimeToString(news.getTime()));
            newsDto.setDescription(news.getDescription());
            newsDto.setTitle(news.getTitle());
            newsDto.setImagePage(news.getImagePage());
            newsDto.setId(news.getId());
            listNewsDto.add(newsDto);
        }
        return listNewsDto;
    }

    public void updateNews(Long id, NewsFormDto news) {
        News editNews = newsRepository.findById(id).get();
        editNews.setTitle(news.getTitle());
        editNews.setImagePage(news.getImagePage());
        editNews.setDescription(news.getDescription());
        editNews.setDetail(news.getDetail());
        newsRepository.save(editNews);
    }

    public void addNews(NewsFormDto newNews) {
        News news = new News();;
        news.setTitle(newNews.getTitle());
        news.setTime(LocalDateTime.now());
        news.setImagePage(newNews.getImagePage());
        news.setDescription(newNews.getDescription());
        news.setDetail(newNews.getDetail());
        newsRepository.saveAndFlush(news);
    }

    public void deleteNews(Long id) {
        newsRepository.deleteById(id);
    }

    public String formatLocalDateTimeToString(LocalDateTime time){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        LocalDateTime roundedTime = time.withNano(0);
        return roundedTime.format(formatter);
    }
}
