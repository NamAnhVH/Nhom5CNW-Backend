package com.WebLearning.WebLearning.Service;

import com.WebLearning.WebLearning.Models.ModelNews;
import com.WebLearning.WebLearning.Repository.NewsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NewsService {

    @Autowired
    private NewsRepository newsRepository;

    public ModelNews findById(Long id) { return newsRepository.findById(id).get(); }

    public ModelNews save(ModelNews news) { return newsRepository.saveAndFlush(news); }

    public List<ModelNews> findAll()  { return newsRepository.findAll(); }

    public List<ModelNews> findTopSixNews() { return newsRepository.findTop6ByOrderByIdDesc(); }

    public void update(Long id, ModelNews modelNews) {
        ModelNews news = newsRepository.findById(id).get();
        news.setTitle(modelNews.getTitle());
        news.setTime(modelNews.getTime());
        news.setImagePage(modelNews.getImagePage());
        news.setDescription(modelNews.getDescription());
        news.setDetail(modelNews.getDetail());
        newsRepository.save(news);
    }

    public void add(ModelNews modelNews) {
        ModelNews news = new ModelNews();;
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
