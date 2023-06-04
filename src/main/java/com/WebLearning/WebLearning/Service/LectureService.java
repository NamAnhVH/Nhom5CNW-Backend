package com.WebLearning.WebLearning.Service;

import com.WebLearning.WebLearning.FormData.LectureFormDto;
import com.WebLearning.WebLearning.Models.Course;
import com.WebLearning.WebLearning.Models.Lecture;
import com.WebLearning.WebLearning.Repository.CourseRepository;
import com.WebLearning.WebLearning.Repository.LectureRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Service
public class LectureService {
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private LectureRepository lectureRepository;

    public void addLecture(Long courseId, LectureFormDto lectureDto) throws IOException {
        Course course = courseRepository.findById(courseId).get();
        Lecture lecture = new Lecture();
        lecture.setCourse(course);
        lecture.setTitle(lectureDto.getTitle());
        lecture.setDescription(lectureDto.getDescription());
        if(!lectureDto.getVideo().isEmpty()){
            File directory = new File("videos");
            String path = directory.getPath() + "/" + lectureDto.getVideo().getOriginalFilename();
            File file = new File(path);
            if (! directory.exists()) {
                directory.mkdir();
            }
            try (OutputStream os = new FileOutputStream(file)) {
                os.write(lectureDto.getVideo().getBytes());
            }
            lecture.setUrlVideo(path);
        }
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

    public void updateLecture(Long lectureId, LectureFormDto lectureDto) throws IOException {
        Lecture lecture = lectureRepository.findById(lectureId).get();
        lecture.setTitle(lectureDto.getTitle());
        lecture.setDescription(lectureDto.getDescription());
        if(!lectureDto.getVideo().isEmpty()){
            if(!lecture.getUrlVideo().equals("")){
                File oldVideo = new File(lecture.getUrlVideo());
                oldVideo.delete();
            }
            File directory = new File("videos");
            String path = directory.getPath() + "/" + lectureDto.getVideo().getOriginalFilename();
            File file = new File(path);
            if (! directory.exists()) {
                directory.mkdir();
            }
            try (OutputStream os = new FileOutputStream(file)) {
                os.write(lectureDto.getVideo().getBytes());
            }
            lecture.setUrlVideo(path);
        }
        lectureRepository.save(lecture);
    }

    public void deleteLecture(Long lectureId) {
        Lecture lecture = lectureRepository.findById(lectureId).get();
        if(!lecture.getUrlVideo().equals("")){
            File oldVideo = new File(lecture.getUrlVideo());
            oldVideo.delete();
        }
        lectureRepository.deleteById(lectureId);
    }
}
