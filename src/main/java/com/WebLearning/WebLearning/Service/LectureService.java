package com.WebLearning.WebLearning.Service;

import com.WebLearning.WebLearning.FormData.LectureFormDto;
import com.WebLearning.WebLearning.Models.Course;
import com.WebLearning.WebLearning.Models.Lecture;
import com.WebLearning.WebLearning.Models.StudentProgress;
import com.WebLearning.WebLearning.Repository.CourseRepository;
import com.WebLearning.WebLearning.Repository.LectureRepository;
import com.WebLearning.WebLearning.Repository.QuizRepository;
import com.WebLearning.WebLearning.Repository.StudentProgressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class LectureService {
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private LectureRepository lectureRepository;
    @Autowired
    private QuizRepository quizRepository;
    @Autowired
    private StudentProgressRepository studentProgressRepository;

    public void addLecture(Long courseId, LectureFormDto lectureDto) throws IOException {
        Course course = courseRepository.findById(courseId).get();
        Lecture lecture = new Lecture();
        lecture.setCourse(course);
        lecture.setTitle(lectureDto.getTitle());
        lecture.setDescription(lectureDto.getDescription());
        if(!lectureDto.getVideo().isEmpty()){
            File directory = new File("videos");
            String path = directory.getPath() + "/"  + formatLocalDateTimeToString(LocalDateTime.now()) + lectureDto.getVideo().getOriginalFilename();
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
            if(!(lecture.getUrlVideo() == null)){
                File oldVideo = new File(lecture.getUrlVideo());
                oldVideo.delete();
            }
            File directory = new File("videos");
            String path = directory.getPath() + "/" + formatLocalDateTimeToString(LocalDateTime.now()) + lectureDto.getVideo().getOriginalFilename();
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

    @Transactional
    public void deleteLecture(Long lectureId) {
        Lecture lecture = lectureRepository.findById(lectureId).get();
        if(!(lecture.getUrlVideo() == null)){
            File oldVideo = new File(lecture.getUrlVideo());
            oldVideo.delete();
        }
        studentProgressRepository.deleteByLectureId(lectureId);
        quizRepository.deleteByLectureId(lectureId);
        lectureRepository.deleteById(lectureId);
    }

    public String formatLocalDateTimeToString(LocalDateTime time){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH-mm-ss dd-MM-yyyy ");
        LocalDateTime roundedTime = time.withNano(0);
        return roundedTime.format(formatter);
    }
}
