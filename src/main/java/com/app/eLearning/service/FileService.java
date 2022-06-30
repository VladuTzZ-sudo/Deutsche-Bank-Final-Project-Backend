package com.app.eLearning.service;

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
import java.util.ArrayList;
import java.util.List;

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

	public ResponseEntity getAllFiles()
	{
		List<String> fileNames = new ArrayList<>();
		try
		{
			Files.walk(rootPath, 1)
					.filter(path -> !path.equals(rootPath))
					.forEach(path -> fileNames.add(path.getFileName()
							.toString()));
			return new ResponseEntity(fileNames, HttpStatus.OK);
		}
		catch (IOException e)
		{
			e.printStackTrace();
			return new ResponseEntity("Could not load the files!", HttpStatus.BAD_REQUEST);
		}
	}

	public Resource getFile(String fileName)
	{
		try
		{
			Path file = rootPath.resolve(fileName);
			Resource resource = new UrlResource(file.toUri());
			if (resource.exists() || resource.isReadable())
			{
				return resource;
			}
			else
			{
				throw new RuntimeException("Could not read the file!");
			}
		}
		catch (MalformedURLException e)
		{
			e.printStackTrace();
			return null;
		}
	}

}
