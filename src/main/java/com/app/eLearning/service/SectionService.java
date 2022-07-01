package com.app.eLearning.service;

import com.app.eLearning.dao.*;
import com.app.eLearning.dto.FileDTO;
import com.app.eLearning.dto.ResponseQuizDTO;
import com.app.eLearning.dto.ResponseSectionDTO;
import com.app.eLearning.exceptions.CourseNotFoundException;
import com.app.eLearning.exceptions.QuizNotFoundException;
import com.app.eLearning.exceptions.SectionNotFoundException;
import com.app.eLearning.exceptions.WrongTokenException;
import com.app.eLearning.repository.*;
import org.apache.catalina.webresources.FileResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class SectionService
{

	@Autowired
	SectionRepository sectionRepository;

	@Autowired
	CourseRepository courseRepository;

	@Autowired
	TakenQuizRepository takenQuizRepository;

	@Autowired
	UserRepository userRepository;

	@Autowired
	FileRepository fileRepository;

	public ResponseEntity<String> postSection(Section section, int courseID) throws WrongTokenException
	{

		Course foundCourse = null;

		try
		{
			foundCourse = courseRepository.findById(courseID).get();
		}
		catch (Exception e)
		{
			return new ResponseEntity<>("Course not found!", HttpStatus.BAD_REQUEST);
		}

		try
		{
			foundCourse.addSection(section);
			courseRepository.saveAndFlush(foundCourse);
			return new ResponseEntity<>("Section inserted!", HttpStatus.OK);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return new ResponseEntity<>("Could not insert new Section", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	public List<ResponseSectionDTO> getSectionsForCourse(int courseId, String role, int userId) throws CourseNotFoundException, WrongTokenException
	{
		Course foundCourse = null;
		User foundUser = null;
		List<File> files = null;
		List<ResponseSectionDTO> sectionListDTO = new ArrayList<>();

		try
		{
			foundUser = userRepository.findById(userId).get();
		}
		catch (Exception e)
		{
			throw new WrongTokenException();
		}

		if (foundUser == null)
		{
			throw new WrongTokenException();
		}

		try
		{
			foundCourse = courseRepository.findById(courseId).get();
		}
		catch (Exception e)
		{
			throw new CourseNotFoundException();
		}

		if (foundCourse == null)
		{
			throw new CourseNotFoundException();
		}

		try
		{
			files = fileRepository.findByCourseId(courseId);
		}
		catch (Exception e)
		{
			// throw new files not found exception
			e.printStackTrace();
		}

		Map<Integer, List<FileDTO>> sectionFiles = new HashMap<>();

		for (Section s : foundCourse.getCourseSections())
		{
			sectionFiles.put(s.getId(), new ArrayList<>());
			for (File f : files)
			{
				if (s.getId() == f.getSectionId())
				{
					sectionFiles.get(s.getId()).add(new FileDTO(f.getName(), f.getDate()));
				}
			}
		}

		if (role.equals("teacher"))
		{
			for (Section s : foundCourse.getCourseSections())
			{

				if (s.getQuiz() == null)
				{
					sectionListDTO.add(new ResponseSectionDTO(s.getId(), s.getTitle(), s.getDescription(), null, sectionFiles.get(s.getId())));
				}
				else
				{
					boolean isQuizEnded = false;
					for (TakenQuiz takenQuiz : foundUser.getTakenQuizzes())
					{
						if (takenQuiz.getQuiz().getId() == s.getQuiz().getId())
						{
							if (takenQuiz.getGivenAnswers().size() > 0)
							{
								isQuizEnded = true;
							}
						}
					}

					Date currentDate = new Date(System.currentTimeMillis());
					if (s.getQuiz().getDeadline().compareTo(currentDate) < 0) {
						isQuizEnded = true;
					}

					sectionListDTO.add(new ResponseSectionDTO(s.getId(), s.getTitle(), s.getDescription(), new ResponseQuizDTO(s.getQuiz().getId(), s.getQuiz().getQuizName(), s.getQuiz().getDescription(), s.getQuiz().getIsVisible(), isQuizEnded), sectionFiles.get(s.getId())));
				}
			}
			return sectionListDTO;
		}
		else
		{
			for (Section s : foundCourse.getCourseSections())
			{
				if (s.getQuiz() == null)
				{
					sectionListDTO.add(new ResponseSectionDTO(s.getId(), s.getTitle(), s.getDescription(), null, sectionFiles.get(s.getId())));
				}
				else if (s.getQuiz().getIsVisible())
				{
					boolean isQuizEnded = false;
					for (TakenQuiz takenQuiz : foundUser.getTakenQuizzes())
					{
						if (takenQuiz.getQuiz().getId() == s.getQuiz().getId())
						{
							if (takenQuiz.getGivenAnswers().size() > 0)
							{
								isQuizEnded = true;
							}
						}
					}

					Date currentDate = new Date(System.currentTimeMillis());
					if (s.getQuiz().getDeadline().compareTo(currentDate) < 0) {
						isQuizEnded = true;
					}

					sectionListDTO.add(new ResponseSectionDTO(s.getId(), s.getTitle(), s.getDescription(), new ResponseQuizDTO(s.getQuiz().getId(), s.getQuiz().getQuizName(), s.getQuiz().getDescription(), s.getQuiz().getIsVisible(), isQuizEnded), sectionFiles.get(s.getId())));
				}
				else if (!s.getQuiz().getIsVisible())
				{
					sectionListDTO.add(new ResponseSectionDTO(s.getId(), s.getTitle(), s.getDescription(), null, sectionFiles.get(s.getId())));
				}
			}
			return sectionListDTO;
		}
	}

	public Section getSpecificSection(int sectionId, String role) throws SectionNotFoundException
	{

		Section foundSection = null;

		try
		{
			foundSection = sectionRepository.findById(sectionId).get();
		}
		catch (Exception e)
		{
			throw new SectionNotFoundException();
		}

		if (foundSection == null)
		{
			throw new SectionNotFoundException();
		}

		if (role.equals("teacher"))
		{
			return foundSection;
		}
		else
		{
			if (foundSection.getQuiz() != null)
			{
				if (foundSection.getQuiz().getIsVisible() == false)
				{
					foundSection.setQuiz(null);
				}
			}
			return foundSection;
		}
	}

}
