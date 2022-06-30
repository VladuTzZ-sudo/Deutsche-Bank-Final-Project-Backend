package com.app.eLearning.controller;

import com.app.eLearning.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.session.RedisSessionProperties;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;

@Controller
@CrossOrigin
public class FileController
{
	@Autowired
	FileService fileService;

	@PostMapping("/upload")
	public ResponseEntity saveFile(@RequestHeader("Authorization") String authHeader,
	                               @RequestParam(name = "files") MultipartFile[] files){

		try
		{
			Arrays.asList(files).stream().forEach(
					file -> fileService.saveFile(file)
			);
			return new ResponseEntity("Files uploaded successfully!", HttpStatus.OK);
		}
		catch (Exception e)
		{
			return new ResponseEntity("Files could not be uploaded!", HttpStatus.FORBIDDEN);
		}
	}

	@GetMapping("/files")
	public ResponseEntity getFileNames(@RequestHeader("Authorization") String authHeader)
	{
		return fileService.getAllFiles();
	}

	@GetMapping("/files/{fileName}")
	public ResponseEntity getFile(@RequestHeader("Authorization") String authHeader,
	                              @PathVariable(name = "fileName") String fileName) throws IOException
	{
		Resource file = fileService.getFile(fileName);
		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
				.contentLength(file.contentLength())
				.contentType(MediaType.APPLICATION_OCTET_STREAM)
				.body(file);
	}
}
