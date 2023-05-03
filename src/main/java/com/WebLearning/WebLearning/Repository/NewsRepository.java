package com.WebLearning.WebLearning.Repository;

import com.WebLearning.WebLearning.Models.ModelNews;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NewsRepository extends JpaRepository<ModelNews, Long> {

}
