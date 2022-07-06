package com.app.eLearning.service;

import com.app.eLearning.dao.Course;
import com.app.eLearning.dao.Section;
import com.app.eLearning.dao.TakenQuiz;
import com.app.eLearning.dao.User;
import com.app.eLearning.dto.EmailResponseDTO;
import com.app.eLearning.exceptions.CourseNotFoundException;
import com.app.eLearning.repository.CourseRepository;
import com.app.eLearning.repository.QuizRepository;
import com.app.eLearning.repository.TakenQuizRepository;
import com.app.eLearning.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class MailService {
    @Autowired
    QuizRepository quizRepository;

    @Autowired
    CourseRepository courseRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    TakenQuizRepository takenQuizRepository;

    @Autowired
    JavaMailSender javaMailSender;

    private HashMap<Integer, Set<Integer>> sentEmails= new HashMap<>();

    @Scheduled(fixedDelay = 10000)
    public void scheduleFixedDelayTask() throws CourseNotFoundException {

        List<User> foundUserList = new ArrayList<>();
        List<Course> foundCourseList = new ArrayList<>();
        List<User> studentList = new ArrayList<>();


        try {
            foundUserList = userRepository.findAll();
        } catch (Exception e) {
            throw new CourseNotFoundException();
        }

        if (foundUserList.size() <= 0) {
            throw new CourseNotFoundException();
        }


        for (User user : foundUserList) {
            if (user.getUserRole().getRoleId() == 2 && user.getActive() != null) {
                studentList.add(user);
            }
        }


        for (User u : foundUserList) {
            List<EmailResponseDTO> emailResponseDTOList = new ArrayList<>();


            for (Course c : u.getUserCourses()) {

                for (Section s : c.getCourseSections()) {

                    if (s.getQuiz() != null) {

                        Date currentDate = new Date(System.currentTimeMillis());

                        if (s.getQuiz().getDeadline().compareTo(currentDate) < 0) {
                            boolean existsTakenQuiz = false;
                            List<String> studentsGrades = new ArrayList<>();
                            for (User student : studentList){
                                for (TakenQuiz takenQuiz : student.getTakenQuizzes()){

                                    if (takenQuiz.getQuiz().getId() == s.getQuiz().getId()){
                                        studentsGrades.add(student.getName() + " " + student.getSurname() + " grade: " + takenQuiz.getGrade());
                                        existsTakenQuiz = true;

                                        if (sentEmails.get(u.getId()) == null){
                                            sentEmails.put(u.getId(), new HashSet<>());
                                        }



                                        Set<Integer> quizSet = sentEmails.get(u.getId());
                                        quizSet.add(s.getQuiz().getId());
                                        sentEmails.put(u.getId(), quizSet);
                                        break;
                                    }
                                }
                            }
                            if (existsTakenQuiz == true){
                                EmailResponseDTO emailResponseDTO = new EmailResponseDTO();
                                emailResponseDTO.setQuizName(s.getQuiz().getQuizName());
                                emailResponseDTO.setStudentsGrades(studentsGrades);

                                emailResponseDTOList.add(emailResponseDTO);
                            }

                            //verificarea 2=[1,2]


                            System.out.println("Quiz ended");
                            System.out.println(u.getName());
                        }


                    }

                }

            }

            System.out.println(emailResponseDTOList);
            System.out.println(sentEmails.toString());
        }




    }


    public void sendSimpleMessage(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("elearningdbapp@gmail.com");
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        javaMailSender.send(message);
    }

}
