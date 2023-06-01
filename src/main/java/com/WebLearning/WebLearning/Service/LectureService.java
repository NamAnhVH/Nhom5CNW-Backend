package com.WebLearning.WebLearning.Service;

import com.WebLearning.WebLearning.FormData.LectureFormDto;
import com.WebLearning.WebLearning.Models.Course;
import com.WebLearning.WebLearning.Models.Lecture;
import com.WebLearning.WebLearning.Repository.CourseRepository;
import com.WebLearning.WebLearning.Repository.LectureRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class LectureService {
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private LectureRepository lectureRepository;

    public void addLecture(Long courseId, LectureFormDto lectureDto) {
        Course course = courseRepository.findById(courseId).get();
        Lecture lecture = new Lecture();
        lecture.setCourse(course);
        lecture.setTitle(lectureDto.getTitle());
        lecture.setDescription(lectureDto.getDescription());
        lecture.setUrlVideo(lectureDto.getUrlVideo());
        lectureRepository.save(lecture);
    }

    public List<LectureFormDto> getLectureByCourseId(Long courseId) {
        List<Lecture> listLecture = lectureRepository.findByCourseId(courseId);
        List<LectureFormDto> listLectureDto = new ArrayList<>();
        int i = 1;
        for(Lecture lecture: listLecture){
            LectureFormDto lectureDto = new LectureFormDto();
            lectureDto.setNumber(i);
            lectureDto.setTitle(lecture.getTitle());
            lectureDto.setId(lecture.getId());
            listLectureDto.add(lectureDto);
            i++;
        }
        return listLectureDto;
    }

    public LectureFormDto getLectureById(Long lectureId) {
        Lecture lecture = lectureRepository.findById(lectureId).get();
        LectureFormDto lectureDto = new LectureFormDto();
        lectureDto.setId(lecture.getId());
        lectureDto.setTitle(lecture.getTitle());
        lectureDto.setDescription(lecture.getDescription());
        lectureDto.setUrlVideo(lecture.getUrlVideo());
        return lectureDto;
    }

    public void updateLecture(Long lectureId, LectureFormDto lectureDto) {
        Lecture lecture = lectureRepository.findById(lectureId).get();
        lecture.setTitle(lectureDto.getTitle());
        lecture.setDescription(lectureDto.getDescription());
        lecture.setUrlVideo(lectureDto.getUrlVideo());
        lectureRepository.save(lecture);
    }

    public void deleteLecture(Long lectureId) {
        lectureRepository.deleteById(lectureId);
    }
}
