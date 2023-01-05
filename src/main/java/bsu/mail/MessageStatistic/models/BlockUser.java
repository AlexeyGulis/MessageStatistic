package bsu.mail.MessageStatistic.models;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "BLOCKEDUSER")
public class BlockUser {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "sender")
    private String sender;
    @Column(name = "date_of_block")
    private LocalDateTime dateOfBlock;

    public BlockUser() {
    }

    public BlockUser(String sender, LocalDateTime dateOfBlock) {
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
