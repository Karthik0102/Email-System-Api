package com.email.system.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.email.system.dto.EmailDto;
import com.email.system.enums.Status;
import com.email.system.model.Email;
import com.email.system.service.EmailService;
@RestController
@RequestMapping("api/emails")
public class EmailController {
	@Autowired
	private EmailService emailService;
	@PostMapping("/send")
	public ResponseEntity<String> sendEmail(@RequestBody EmailDto emailDto) {
		String sendEmail = emailService.sendEmail(emailDto);
		if (sendEmail.contains("successfully")) {
			return new ResponseEntity<>(sendEmail, HttpStatus.CREATED);
		} else {
			return new ResponseEntity<>(sendEmail, HttpStatus.BAD_REQUEST);
		}
	}
	@GetMapping("/inbox/")
	public ResponseEntity<List<Email>> getAllEmailsByStatus(@RequestParam String status) {
		try {
			List<Email> allEmails = emailService.getAllEmailsByStatus(status);
			return new ResponseEntity<>(allEmails, HttpStatus.OK);
		} catch (IllegalArgumentException e) {
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
		}
	}
	
	@GetMapping("/inbox")
	public ResponseEntity<List<Email>> getAllEmails() {
		try {
			List<Email> allEmails = emailService.getAllEmails();
			return new ResponseEntity<>(allEmails, HttpStatus.OK);
		} catch (IllegalArgumentException e) {
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
		}
	}
	@PutMapping("{emailId}/updateStatus")
	public ResponseEntity<String> updateStatusOfEmail(@PathVariable Long emailId, @RequestParam Status status) {
		String updateEmailStatus = emailService.updateEmailStatus(emailId, status);
		if (updateEmailStatus.contains("Updated Successfully!")) {
			return new ResponseEntity<>(updateEmailStatus, HttpStatus.NO_CONTENT);
		} else {
			return new ResponseEntity<>(updateEmailStatus, HttpStatus.BAD_REQUEST);
		}
	}
}