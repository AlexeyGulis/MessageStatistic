package bsu.mail.MessageStatistic.repositories;

import bsu.mail.MessageStatistic.models.BlockUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface BlockUsersRepository extends JpaRepository<BlockUser, Integer> {
    public List<BlockUser> findAllByDateOfBlockGreaterThanEqualAndDateOfBlockLessThan(LocalDateTime after,LocalDateTime before);
}
