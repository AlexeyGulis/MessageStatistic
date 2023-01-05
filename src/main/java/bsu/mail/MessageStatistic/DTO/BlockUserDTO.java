package bsu.mail.MessageStatistic.DTO;

import java.time.LocalDateTime;

public class BlockUserDTO {
    private String sender;
    private LocalDateTime dateOfBlock;

    public BlockUserDTO() {
    }

    public BlockUserDTO(String sender, LocalDateTime dateOfBlock) {
        this.sender = sender;
        this.dateOfBlock = dateOfBlock;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public LocalDateTime getDateOfBlock() {
        return dateOfBlock;
    }

    public void setDateOfBlock(LocalDateTime dateOfBlock) {
        this.dateOfBlock = dateOfBlock;
    }
}
