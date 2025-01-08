package com.email.system.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.email.system.dto.EmailDto;
import com.email.system.enums.Status;
import com.email.system.model.Email;
import com.email.system.repository.EmailRepository;

@Service
public class EmailService {
	@Autowired
	private EmailRepository emailRepository;
	@Autowired
	private JavaMailSender mailSender;

	private static final String EMAIL_REGEX = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";

//	@Transactional
//	public String sendEmail(EmailDto emailDto) {
//
//		if (emailDto == null) {
//			return "Email data cannot be null";
//		}
//		if (emailDto.getSender() == null || emailDto.getSender().isEmpty()
//				|| !emailDto.getSender().matches(EMAIL_REGEX)) {
//			return "Sender cannot be empty";
//		}
//		if (emailDto.getReceiver() == null || emailDto.getReceiver().isEmpty()
//				|| !emailDto.getReceiver().matches(EMAIL_REGEX)) {
//			return "Receiver cannot be empty";
//		}
//		if (emailDto.getSubject() == null || emailDto.getSubject().isEmpty()) {
//			return "Subject cannot be empty";
//		}
//		if (emailDto.getContent() == null || emailDto.getContent().isEmpty()) {
//			return "Content cannot be empty";
//		}
//
//		if (emailDto.getContent().length() > 1000) {
//			return " Content lenght exceeds the allowed limit of 1000 characters";
//		}
//
//		SimpleMailMessage mailMessage = new SimpleMailMessage();
//		mailMessage.setFrom(emailDto.getSender());
//		mailMessage.setTo(emailDto.getReceiver());
//		mailMessage.setSubject(emailDto.getSubject());
//		mailMessage.setText(emailDto.getContent());
//		try {
//			// sending the email;
//			mailSender.send(mailMessage);
//			Email email = new Email();
//			email.setSender(emailDto.getSender());
//			email.setReceiver(emailDto.getReceiver());
//			email.setSubject(emailDto.getSubject());
//			email.setBody(emailDto.getContent());
//			email.setStatus(Status.SENT);
//			email.setSentDate(LocalDateTime.now());
//			emailRepository.save(email);
//			return "Email Sent Successfully and save to database";
//		} catch (Exception e) {
//			return "Failed to send email: " + e.getMessage();
//		}
//	}

	@Transactional(rollbackFor = Exception.class) // Ensures rollback for all exceptions
	
	public String sendEmail(EmailDto emailDto) {
		// Validate input
		if (emailDto == null) {
			return "Email data cannot be null";
		}
		if (emailDto.getSender() == null || emailDto.getSender().isEmpty()
				|| !emailDto.getSender().matches(EMAIL_REGEX)) {
			return "Invalid Sender email address";
		}
		if (emailDto.getReceiver() == null || emailDto.getReceiver().isEmpty()
				|| !emailDto.getReceiver().matches(EMAIL_REGEX)) {
			return "Invalid Receiver email address";
		}
		if (emailDto.getSubject() == null || emailDto.getSubject().isEmpty()) {
			return "Subject cannot be empty";
		}
		if (emailDto.getContent() == null || emailDto.getContent().isEmpty()) {
			return "Content cannot be empty";
		}

		if (emailDto.getContent().length() > 1000) {
			return "Content length exceeds the allowed limit of 1000 characters";
		}

		if (emailDto.getSubject().length() > 255) {
			return "Subject limit exceeded(255)";
		}

		Email email = new Email();
		email.setSender(emailDto.getSender());
		email.setReceiver(emailDto.getReceiver());
		email.setSubject(emailDto.getSubject());
		email.setBody(emailDto.getContent());
		email.setStatus(Status.PENDING);
		email.setSentDate(LocalDateTime.now());
		try {
			emailRepository.save(email);

			SimpleMailMessage mailMessage = new SimpleMailMessage();
			mailMessage.setFrom(emailDto.getSender());
			mailMessage.setTo(emailDto.getReceiver());
			mailMessage.setSubject(emailDto.getSubject());
			mailMessage.setText(emailDto.getContent());

			mailSender.send(mailMessage);

			email.setStatus(Status.SENT);
			email.setSentDate(LocalDateTime.now());

			emailRepository.save(email);
			return "Email sent successfully and saved to database";
		} catch (Exception e) {
			throw new RuntimeException("Failed to send email: " + e.getMessage());
		}
	}

	public List<Email> getAllEmailsByStatus(String status) {
		try {
			if (status == null || status.isEmpty()) {
				throw new IllegalArgumentException("Status cannot be null or empty");
			}
			Status isValidStatus = Status.valueOf(status.toUpperCase());
			List<Email> emailsByStatus = emailRepository.findByStatus(isValidStatus);
			return emailsByStatus;
		} catch (IllegalArgumentException e) {
			System.out.println("Error: Invalid Status! Please provide a valid status.");
			return new ArrayList<>(); // Return an empty list if status is invalid
		} catch (Exception e) {
			System.out.println("Error: Unable to fetch emails. " + e.getMessage());
			return new ArrayList<>();
		}
	}

	public String updateEmailStatus(Long emailId, Status status) {
		if (status == null)
			System.out.println("Status can't be empty or null");
		if (status == Status.SENT)
			return "Cannot update Status to SENT. Choose a different one.";
		Optional<Email> optionalEmail = emailRepository.findById(emailId);
		if (optionalEmail.isPresent()) {
			Email email = optionalEmail.get();
			email.setStatus(status);
			emailRepository.save(email);
			return "Email Status Updated Successfully! ";
		} else {
			return "Email with id " + emailId + " Not Found!";
		}
	}

	public List<Email> getAllEmails() {
		List<Email> allEmails = emailRepository.findAll();
		return allEmails;
	}
}
