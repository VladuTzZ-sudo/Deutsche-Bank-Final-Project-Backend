package com.app.eLearning.service;

import com.app.eLearning.repository.CourseRepository;
import com.app.eLearning.repository.QuizRepository;
import com.app.eLearning.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class MailService
{
	@Autowired
	QuizRepository quizRepository;

	@Autowired
	CourseRepository courseRepository;

	@Autowired
	UserRepository userRepository;

	@Autowired
	JavaMailSender javaMailSender;

	@Scheduled(fixedDelay = 1000)
	public void scheduleFixedDelayTask() {

	}


	public void sendSimpleMessage(String to, String subject, String text)
	{
		SimpleMailMessage message = new SimpleMailMessage();
		message.setFrom("elearningdbapp@gmail.com");
		message.setTo(to);
		message.setSubject(subject);
		message.setText(text);
		javaMailSender.send(message);
	}

}
