package com.app.eLearning.controller;

import com.app.eLearning.exceptions.FileAlreadyExistsException;
import com.app.eLearning.exceptions.FileNotFoundException;
import com.app.eLearning.exceptions.WrongTokenException;
import com.app.eLearning.service.FileService;
import com.app.eLearning.service.UserService;
import com.app.eLearning.utils.LoginAuthorization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Arrays;

@Controller
@CrossOrigin
public class FileController
{
	@Autowired
	FileService fileService;

	@Autowired
	UserService userService;

	@PostMapping("/courses/{courseId}/sections/{sectionId}/upload")
	public ResponseEntity saveFile(@RequestHeader("Authorization") String authHeader,
	                               @RequestParam(name = "files") MultipartFile[] files,
	                               @PathVariable(name = "courseId") int courseId,
	                               @PathVariable(name = "sectionId") int sectionId) throws WrongTokenException, FileAlreadyExistsException
	{

		Pair<Integer, String> loginAuth = null;

		loginAuth = LoginAuthorization.validateAuthorization(authHeader);

		if (!userService.checkIfUserExists(loginAuth.getFirst()))
		{
			return new ResponseEntity<>("The user id you provided is not valid!", HttpStatus.UNAUTHORIZED);
		}

		Pair<Integer, String> finalLoginAuth = loginAuth;
		for (MultipartFile file : files)
		{
			fileService.saveFile(file, finalLoginAuth.getFirst(), courseId, sectionId);
		}
		return new ResponseEntity("Files uploaded successfully!", HttpStatus.OK);
	}

	@GetMapping("/courses/{courseId}/sections/{sectionId}/files/{fileName}")
	public ResponseEntity getFile(@RequestHeader("Authorization") String authHeader,
	                              @PathVariable(name = "courseId") int courseId,
	                              @PathVariable(name = "sectionId") int sectionId,
	                              @PathVariable(name = "fileName") String fileName) throws IOException, FileNotFoundException
	{
		Resource file = fileService.getFile(courseId, sectionId, fileName);
		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
				.contentLength(file.contentLength())
				.contentType(MediaType.APPLICATION_OCTET_STREAM)
				.body(file);
	}

	@PostMapping("/files")
	public ResponseEntity saveStudentFile(@RequestHeader("Authorization") String authHeader,
	                                      @RequestParam(name = "files") MultipartFile[] files) throws WrongTokenException, FileAlreadyExistsException
	{
		Pair<Integer, String> loginAuth = null;

		loginAuth = LoginAuthorization.validateAuthorization(authHeader);

		if (!userService.checkIfUserExists(loginAuth.getFirst()))
		{
			return new ResponseEntity<>("The user id you provided is not valid!", HttpStatus.UNAUTHORIZED);
		}

		Pair<Integer, String> finalLoginAuth = loginAuth;
		for (MultipartFile file : files)
		{
			fileService.saveFile(file, finalLoginAuth.getFirst(), 0, 0);
		}
		return new ResponseEntity("Files uploaded successfully!", HttpStatus.OK);
	}


	@GetMapping("/files")
	public ResponseEntity getStudentFile(@RequestHeader("Authorization") String authHeader,
	                                     @PathVariable(name = "fileName") String fileName) throws WrongTokenException, IOException, FileNotFoundException
	{

		Pair<Integer, String> loginAuth = null;

		loginAuth = LoginAuthorization.validateAuthorization(authHeader);

		if (!userService.checkIfUserExists(loginAuth.getFirst()))
		{
			return new ResponseEntity<>("The user id you provided is not valid!", HttpStatus.UNAUTHORIZED);
		}

		Resource file = fileService.getFile(0, 0, fileName);
		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
				.contentLength(file.contentLength())
				.contentType(MediaType.APPLICATION_OCTET_STREAM)
				.body(file);
	}

}
