package com.WebLearning.WebLearning.Repository;

import com.WebLearning.WebLearning.Models.News;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NewsRepository extends JpaRepository<News, Long> {

//    @Query("SELECT n FROM ModelNews n ORDER BY n.id DESC LIMIT 6")
    List<News> findTop6ByOrderByIdDesc();

}
