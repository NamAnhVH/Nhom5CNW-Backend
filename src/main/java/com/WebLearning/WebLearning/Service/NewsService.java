package com.WebLearning.WebLearning.Service;

import com.WebLearning.WebLearning.FormData.NewsFormDto;
import com.WebLearning.WebLearning.Models.News;
import com.WebLearning.WebLearning.Repository.NewsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NewsService {

    @Autowired
    private NewsRepository newsRepository;

    public News findById(Long id) { return newsRepository.findById(id).get(); }


    public News save(News news) { return newsRepository.saveAndFlush(news); }

    public List<News> findAll()  { return newsRepository.findAll(); }

    public List<News> findTopSixNews() { return newsRepository.findTop6ByOrderByIdDesc(); }

    public void update(Long id, NewsFormDto news) {
        News editNews = newsRepository.findById(id).get();
        editNews.setTitle(news.getTitle());
        editNews.setTime(news.getTime());
        editNews.setImagePage(news.getImagePage());
        editNews.setDescription(news.getDescription());
        editNews.setDetail(news.getDetail());
        newsRepository.save(editNews);
    }

    public void add(NewsFormDto newNews) {
        News news = new News();;
        news.setTitle(newNews.getTitle());
        news.setTime(newNews.getTime());
        news.setImagePage(newNews.getImagePage());
        news.setDescription(newNews.getDescription());
        news.setDetail(newNews.getDetail());
        newsRepository.saveAndFlush(news);
    }

    public void delete(Long id) {
        newsRepository.deleteById(id);
    }
}
