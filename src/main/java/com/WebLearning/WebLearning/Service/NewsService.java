package com.WebLearning.WebLearning.Service;

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

    public void update(Long id, News modelNews) {
        News news = newsRepository.findById(id).get();
        news.setTitle(modelNews.getTitle());
        news.setTime(modelNews.getTime());
        news.setImagePage(modelNews.getImagePage());
        news.setDescription(modelNews.getDescription());
        news.setDetail(modelNews.getDetail());
        newsRepository.save(news);
    }

    public void add(News modelNews) {
        News news = new News();;
        news.setTitle(modelNews.getTitle());
        news.setTime(modelNews.getTime());
        news.setImagePage(modelNews.getImagePage());
        news.setDescription(modelNews.getDescription());
        news.setDetail(modelNews.getDetail());
        newsRepository.saveAndFlush(news);
    }

    public void delete(Long id) {
        newsRepository.deleteById(id);
    }
}
