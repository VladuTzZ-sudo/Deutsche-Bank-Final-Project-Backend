package com.app.eLearning.service;

import com.app.eLearning.dao.File;
import com.app.eLearning.dto.FileDTO;
import com.app.eLearning.exceptions.FileAlreadyExistsException;
import com.app.eLearning.exceptions.FileNotFoundException;
import com.app.eLearning.repository.FileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;

@Service
public class FileService
{
	@Autowired
	FileRepository fileRepository;

	private final Path teachersRootPath = Paths.get("uploads/teachers");
	private final Path studentsRootPath = Paths.get("uploads/students");

	@PostConstruct
	public void init()
	{
		try
		{
			Files.createDirectories(teachersRootPath);
			Files.createDirectories(studentsRootPath);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	public ResponseEntity saveFile(MultipartFile file, int userId, int courseId, int sectionId) throws FileAlreadyExistsException
	{
		File found = fileRepository.findFirstByName(file.getOriginalFilename());

		if(found != null)
			throw new FileAlreadyExistsException();

		try
		{
			if(courseId != 0 && sectionId != 0)
				Files.copy(file.getInputStream(), teachersRootPath.resolve(file.getOriginalFilename()));
			else
				Files.copy(file.getInputStream(), studentsRootPath.resolve(file.getOriginalFilename()));


			File fileDAO = new File();
			fileDAO.setName(file.getOriginalFilename());
			fileDAO.setDate(new Date().getTime());
			fileDAO.setUserId(userId);
			fileDAO.setCourseId(courseId);
			fileDAO.setSectionId(sectionId);
			fileRepository.save(fileDAO);

			return new ResponseEntity("File saved successfully!", HttpStatus.OK);
		}
		catch (IOException e)
		{
			return new ResponseEntity("Could not save the file!", HttpStatus.FORBIDDEN);
		}
	}

	public Resource getFile(int courseId, int sectionId, String fileName) throws FileNotFoundException, MalformedURLException
	{
		File found = null;
		found = fileRepository.findFirstByName(fileName);

		if(found == null)
			throw new FileNotFoundException();

		Path file = null;

		if(courseId != 0 && sectionId != 0)
			file = teachersRootPath.resolve(fileName);
		else
			file = studentsRootPath.resolve(fileName);

		Resource resource = new UrlResource(file.toUri());
		if (resource.exists() || resource.isReadable())
		{
			if(courseId == 0 && sectionId == 0)
				return resource;
			else if(courseId != 0 && sectionId != 0 && found.getCourseId() == courseId && found.getSectionId() == sectionId)
				return resource;
			return null;
		}
		else
		{
			throw new FileNotFoundException();
		}
	}

//	public ResponseEntity getAllFiles()
//	{
//		List<String> fileNames = new ArrayList<>();
//		try
//		{
//			Files.walk(rootPath, 1)
//					.filter(path -> !path.equals(rootPath))
//					.forEach(path -> fileNames.add(path.getFileName().toString()));
//			return new ResponseEntity(fileNames, HttpStatus.OK);
//		}
//		catch (IOException e)
//		{
//			e.printStackTrace();
//			return new ResponseEntity("Could not load the files!", HttpStatus.BAD_REQUEST);
//		}
//	}

}
