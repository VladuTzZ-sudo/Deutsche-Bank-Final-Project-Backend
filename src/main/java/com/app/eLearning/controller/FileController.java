package com.app.eLearning.controller;

import com.app.eLearning.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@CrossOrigin
public class FileController
{
	@Autowired
	FileService fileService;

	@PostMapping("/upload")
	public ResponseEntity saveFile(@RequestHeader("Authorization") String authHeader,
	                               @RequestParam(name = "file") MultipartFile file){
		return fileService.saveFile(file);
	}
}
