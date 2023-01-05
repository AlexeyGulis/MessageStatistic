package bsu.mail.MessageStatistic.services;

import bsu.mail.MessageStatistic.models.BlockUser;
import bsu.mail.MessageStatistic.models.Message;
import bsu.mail.MessageStatistic.repositories.BlockUsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;


@Service
@Transactional(readOnly = true)
public class BlockUsersService {
    private final BlockUsersRepository blockUsersRepository;

    @Autowired
    public BlockUsersService(BlockUsersRepository blockUsersRepository) {
        this.blockUsersRepository = blockUsersRepository;
    }
    @Transactional
    public void addBlockUsersList(List<BlockUser> blockUserList) {
        for (BlockUser blockUser : blockUserList
        ) {
            blockUsersRepository.save(blockUser);
        }
    }

    public List<BlockUser> getBlockedUsers(LocalDate date){
        List<BlockUser> messageList = blockUsersRepository.findAllByDateOfBlockGreaterThanEqualAndDateOfBlockLessThan(date.atStartOfDay(), date.plusDays(1).atStartOfDay());
        return messageList;
    }
}
