package com.WebLearning.WebLearning.Service;

import com.WebLearning.WebLearning.FormData.AnswerFormDto;
import com.WebLearning.WebLearning.FormData.QuizFormDto;
import com.WebLearning.WebLearning.Models.Lecture;
import com.WebLearning.WebLearning.Models.Quiz;
import com.WebLearning.WebLearning.Repository.LectureRepository;
import com.WebLearning.WebLearning.Repository.QuizRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class QuizService {

    @Autowired
    private LectureRepository lectureRepository;
    @Autowired
    private QuizRepository quizRepository;

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
        quizRepository.save(quiz);
    }

    public void updateQuiz(Long quizId, QuizFormDto quizDto) {
        Quiz quiz = quizRepository.findById(quizId).get();
        quiz.setQuestion(quizDto.getQuestion());
        quiz.setAnswer1(quizDto.getAnswer1());
        quiz.setAnswer2(quizDto.getAnswer2());
        quiz.setAnswer3(quizDto.getAnswer3());
        quiz.setAnswer4(quizDto.getAnswer4());
        quiz.setCorrectAnswer(quizDto.getCorrectAnswer());
        quizRepository.save(quiz);
    }

    public void deleteQuiz(Long quizId) {
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
        return String.valueOf(Math.round((grade*10/listAnswer.size()) * 100.0)/ 100.0);
    }
}
