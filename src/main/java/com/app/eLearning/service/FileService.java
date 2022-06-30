package com.app.eLearning.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class FileService
{
	private final Path rootPath = Paths.get("uploads");

	@PostConstruct
	public void init()
	{
		try
		{
			Files.createDirectories(rootPath);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	public ResponseEntity saveFile(MultipartFile file)
	{
		try
		{
			Files.copy(file.getInputStream(), rootPath.resolve(file.getOriginalFilename()));
			return new ResponseEntity("File saved successfully!", HttpStatus.OK);
		}
		catch (IOException e)
		{
			e.printStackTrace();
			return new ResponseEntity("Could not save the file!", HttpStatus.FORBIDDEN);
		}
	}

	public void deleteAll()
	{

	}

	public ResponseEntity getAllFiles()
	{
		return null;
	}


	public ResponseEntity getFile()
	{
		return null;
	}
}
