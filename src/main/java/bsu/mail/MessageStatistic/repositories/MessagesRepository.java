package bsu.mail.MessageStatistic.repositories;

import bsu.mail.MessageStatistic.models.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface MessagesRepository extends JpaRepository<Message, Integer> {
    public boolean existsMessageByIdMessage(String idMessage);
    public List<Message> findAllByDateMessageGreaterThanEqualAndDateMessageLessThan(LocalDateTime after,LocalDateTime before);
}
