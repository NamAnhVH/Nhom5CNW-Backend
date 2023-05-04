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
}
