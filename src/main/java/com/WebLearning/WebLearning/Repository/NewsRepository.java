package com.WebLearning.WebLearning.Repository;

import com.WebLearning.WebLearning.Models.ModelNews;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NewsRepository extends JpaRepository<ModelNews, Long> {

//    @Query("SELECT n FROM ModelNews n ORDER BY n.id DESC LIMIT 6")
    List<ModelNews> findTop6ByOrderByIdDesc();

}
