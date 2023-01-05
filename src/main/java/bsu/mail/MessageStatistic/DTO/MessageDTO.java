package bsu.mail.MessageStatistic.DTO;

import java.time.LocalDateTime;

public class MessageDTO {
    private String idMessage;
    private LocalDateTime dateMessage;
    private String sender;
    private Integer recipient;
    private Boolean subject;

    public MessageDTO() {
    }

    public MessageDTO(String idMessage, String sender,
                      LocalDateTime dateMessage, Integer recipient, Boolean subject) {
        this.idMessage = idMessage;
        this.sender = sender;
        this.dateMessage = dateMessage;
        this.recipient = recipient;
        this.subject = subject;
    }

    public String getIdMessage() {
        return idMessage;
    }

    public void setIdMessage(String idMessage) {
        this.idMessage = idMessage;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public LocalDateTime getDateMessage() {
        return dateMessage;
    }

    public void setDateMessage(LocalDateTime dateMessage) {
        this.dateMessage = dateMessage;
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
