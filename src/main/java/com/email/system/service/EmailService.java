package com.email.system.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

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

	public String sendEmail(EmailDto emailDto) {
		if (emailDto == null) {
			return "Email data cannot be null";
		}
		if (emailDto.getSender() == null || emailDto.getSender().isEmpty()) {
			return "Sender cannot be empty";
		}
		if (emailDto.getReceiver() == null || emailDto.getReceiver().isEmpty()) {
			return "Receiver cannot be empty";
		}
		if (emailDto.getSubject() == null || emailDto.getSubject().isEmpty()) {
			return "Subject cannot be empty";
		}
		if (emailDto.getContent() == null || emailDto.getContent().isEmpty()) {
			return "Content cannot be empty";
		}
		SimpleMailMessage mailMessage = new SimpleMailMessage();
		mailMessage.setFrom(emailDto.getSender());
		mailMessage.setTo(emailDto.getReceiver());
		mailMessage.setSubject(emailDto.getSubject());
		mailMessage.setText(emailDto.getContent());
		try {
			// sending the email;
			mailSender.send(mailMessage);
			Email email = new Email();
			email.setSender(emailDto.getSender());
			email.setReceiver(emailDto.getReceiver());
			email.setSubject(emailDto.getSubject());
			email.setBody(emailDto.getContent());
			email.setStatus(Status.SENT);
			email.setSentDate(LocalDateTime.now());
			emailRepository.save(email);
			return "Email Sent Successfully and save to database";
		} catch (Exception e) {
			return "Failed to send email: " + e.getMessage();
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

//	public List<Email> getAllEmailsByStatus(String status) {
//	    try {
//	        if (status == null || status.trim().isEmpty()) {
//	            System.out.println("Error: Status cannot be null or empty.");
//	            return new ArrayList<>(); // Return an empty list for invalid input
//	        }
//	        String normalizedStatus = status.trim().toUpperCase();
//	        Status isValidStatus = Status.valueOf(normalizedStatus);
//	        return emailRepository.findByStatus(isValidStatus);
//	    } catch (IllegalArgumentException e) {
//	        System.out.println("Error: Invalid Status! Please provide a valid status from: ");
//	        for (Status validStatus : Status.values()) {
//	            System.out.println(" - " + validStatus.name());
//	        }
//	        return new ArrayList<>(); // Return an empty list if the status is invalid
//	    } catch (Exception e) {
//	        System.out.println("Error: Unable to fetch emails. " + e.getMessage());
//	        return new ArrayList<>();
//	    }
//	}
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
