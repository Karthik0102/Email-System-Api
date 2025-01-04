package com.email.system.model;

import java.time.LocalDateTime;

import com.email.system.enums.Status;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Email {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String sender;
	private String receiver;
	@Column(name = "body", length = 1000)
	private String body;
	private String subject;
	@Column(name = "sent_date")
	private LocalDateTime sentDate;
	@Enumerated(EnumType.STRING)
	@Column(name = "status")
	private Status status;

	public Email() {
	}

	public Email(String sender, String receiver, String subject, String body, LocalDateTime sentDate, Status status) {
		super();
		this.sender = sender;
		this.receiver = receiver;
		this.subject = subject;
		this.body = body;
		this.sentDate = sentDate;
		this.status = status;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public String getReceiver() {
		return receiver;
	}

	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public LocalDateTime getSentDate() {
		return sentDate;
	}

	public void setSentDate(LocalDateTime sentDate) {
		this.sentDate = sentDate;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status sent) {
		this.status = sent;
	}

	@Override
	public String toString() {
		return "Email [id=" + id + ", sender=" + sender + ", receiver=" + receiver + ", body=" + body + ", subject="
				+ subject + ", sentDate=" + sentDate + ", status=" + status + "]";
	}
}