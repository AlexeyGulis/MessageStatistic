package bsu.mail.MessageStatistic.models;


import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "message")
public class Message {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "id_message")
    private String idMessage;

    @Column(name = "date_message")
    private LocalDateTime dateMessage;

    @Column(name = "sender")
    private String sender;

    @Column(name = "recipient")
    private Integer recipient;

    @Column(name = "subject")
    private Boolean subject;

    public Message() {
    }

    public Message(String idMessage, LocalDateTime dateMessage,
                   String sender, Integer recipient, Boolean subject) {
        this.idMessage = idMessage;
        this.dateMessage = dateMessage;
        this.sender = sender;
        this.recipient = recipient;
        this.subject = subject;
    }

    public String getIdMessage() {
        return idMessage;
    }

    public void setIdMessage(String idMessage) {
        this.idMessage = idMessage;
    }

    public LocalDateTime getDateMessage() {
        return dateMessage;
    }

    public void setDateMessage(LocalDateTime dateMessage) {
        this.dateMessage = dateMessage;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public Integer getRecipient() {
        return recipient;
    }

    public void setRecipient(Integer recipient) {
        this.recipient = recipient;
    }

    public Boolean getSubject() {
        return subject;
    }

    public void setSubject(Boolean subject) {
        this.subject = subject;
    }
}
