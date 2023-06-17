package com.WebLearning.WebLearning.Service;

import com.WebLearning.WebLearning.FormData.AnswerFormDto;
import com.WebLearning.WebLearning.FormData.QuizFormDto;
import com.WebLearning.WebLearning.Models.*;
import com.WebLearning.WebLearning.Repository.LectureRepository;
import com.WebLearning.WebLearning.Repository.QuizRepository;
import com.WebLearning.WebLearning.Repository.StudentProfileRepository;
import com.WebLearning.WebLearning.Repository.StudentProgressRepository;
import com.WebLearning.WebLearning.Security.AuthenticationFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class QuizService {

    @Autowired
    private AuthenticationFacade authenticationFacade;
    @Autowired
    private LectureRepository lectureRepository;
    @Autowired
    private QuizRepository quizRepository;
    @Autowired
    private StudentProgressRepository studentProgressRepository;
    @Autowired
    private StudentProfileRepository studentProfileRepository;

    public List<QuizFormDto> getQuizByLectureId(Long lectureId) {
        List<Quiz> listQuiz = quizRepository.findByLectureId(lectureId);
        List<QuizFormDto> listQuizDto = new ArrayList<>();
        int i = 1;
        for(Quiz quiz: listQuiz){
            QuizFormDto quizDto = new QuizFormDto();
            quizDto.setQuestion(quiz.getQuestion());
            quizDto.setNumber(i);
            quizDto.setAnswer1(quiz.getAnswer1());
            quizDto.setAnswer2(quiz.getAnswer2());
            quizDto.setAnswer3(quiz.getAnswer3());
            quizDto.setAnswer4(quiz.getAnswer4());
            quizDto.setCorrectAnswer(quiz.getCorrectAnswer());
            quizDto.setId(quiz.getId());
            i++;
            listQuizDto.add(quizDto);
        }
        return listQuizDto;
    }
    @Transactional
    public void addQuiz(Long lectureId, QuizFormDto quizDto) {
        Lecture lecture = lectureRepository.findById(lectureId).get();
        Quiz quiz = new Quiz();
        quiz.setQuestion(quizDto.getQuestion());
        quiz.setAnswer1(quizDto.getAnswer1());
        quiz.setAnswer2(quizDto.getAnswer2());
        quiz.setAnswer3(quizDto.getAnswer3());
        quiz.setAnswer4(quizDto.getAnswer4());
        quiz.setCorrectAnswer(quizDto.getCorrectAnswer());
        quiz.setLecture(lecture);
        studentProgressRepository.deleteByLectureId(lectureId);
        quizRepository.save(quiz);
    }
    @Transactional
    public void updateQuiz(Long quizId, QuizFormDto quizDto) {
        Quiz quiz = quizRepository.findById(quizId).get();
        quiz.setQuestion(quizDto.getQuestion());
        quiz.setAnswer1(quizDto.getAnswer1());
        quiz.setAnswer2(quizDto.getAnswer2());
        quiz.setAnswer3(quizDto.getAnswer3());
        quiz.setAnswer4(quizDto.getAnswer4());
        quiz.setCorrectAnswer(quizDto.getCorrectAnswer());
        studentProgressRepository.deleteByLectureId(quiz.getLecture().getId());
        quizRepository.save(quiz);
    }
    @Transactional
    public void deleteQuiz(Long quizId) {
        Quiz quiz = quizRepository.findById(quizId).get();
        studentProgressRepository.deleteByLectureId(quiz.getLecture().getId());
        quizRepository.deleteById(quizId);
    }

    public String calGrade(AnswerFormDto answerDto, Long lectureId) {
        List<Quiz> listQuiz = quizRepository.findByLectureId(lectureId);
        List<String> listAnswer = answerDto.getAnswers();
        float grade = 0;
        for(int i = 0; i < listAnswer.size(); i++){
            if(listAnswer.get(i).equals(listQuiz.get(i).getCorrectAnswer() + ',')){
                grade++;
            }
        }
        grade = grade*10/listAnswer.size();
        updateStudentProgress(lectureId, grade);
        return String.valueOf(Math.round(grade * 100.0)/ 100.0);
    }

    private void updateStudentProgress(Long lectureId, float grade) {
        Account account = authenticationFacade.getAccount();
        StudentProfile student = studentProfileRepository.findByAccountId(account.getId());
        StudentProgress progress = studentProgressRepository.findByStudentIdAndLectureId(student.getId(), lectureId);
        if(progress == null){
            Lecture lecture = lectureRepository.findById(lectureId).get();
            progress = new StudentProgress();
            progress.setStudent(student);
            progress.setLecture(lecture);
            progress.setNumber(1);
            progress.setGrade(grade);
            progress.setTime(LocalDateTime.now());
            studentProgressRepository.save(progress);
        } else {
            progress.setNumber(progress.getNumber() + 1);
            if(grade > progress.getGrade()){
                progress.setGrade(grade);
                progress.setTime(LocalDateTime.now());
            }
            studentProgressRepository.save(progress);
        }
    }
}
