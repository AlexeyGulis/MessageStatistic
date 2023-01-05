package bsu.mail.MessageStatistic.services;

import bsu.mail.MessageStatistic.models.Message;
import bsu.mail.MessageStatistic.repositories.MessagesRepository;
import bsu.mail.MessageStatistic.util.MessageNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;

@Service
@Transactional(readOnly = true)
public class MessagesService {
    private final MessagesRepository messagesRepository;

    @Autowired
    public MessagesService(MessagesRepository messagesRepository) {
        this.messagesRepository = messagesRepository;
    }

    public List<Message> findAll() {
        return messagesRepository.findAll();
    }

    public Message findOne(int id) {
        Optional<Message> messageOptional = messagesRepository.findById(id);
        return messageOptional.orElseThrow(MessageNotFoundException::new);
    }

    @Transactional
    public void saveMessage(Message message) {
        messagesRepository.save(message);
    }

    @Transactional
    public void saveMessageList(List<Message> messageList) {
        for (Message message : messageList
        ) {
            if (messagesRepository.existsMessageByIdMessage(message.getIdMessage())) continue;
            messagesRepository.save(message);
        }
    }

    public List<Map<?, Integer>> getInformationAboutMessages(LocalDate date) {
        List<Message> messageList = messagesRepository.findAllByDateMessageGreaterThanEqualAndDateMessageLessThan(date.atStartOfDay(), date.plusDays(1).atStartOfDay());
        List<Map<?, Integer>> list = new ArrayList<>();
        Map<Integer, Integer> countSentMessagesPerHour = new TreeMap<>();
        Map<Integer, Integer> countSentSpamMessagesPerHour = new TreeMap<>();
        Map<Integer, Integer> countReceivedMessagesPerHour = new TreeMap<>();
        Map<Integer, Integer> countReceivedSpamMessagesPerHour = new TreeMap<>();
        Map<String, Integer> listUsersSentMessage = new TreeMap<>();
        Map<String, Integer> listUsersSentSpamMessage = new TreeMap<>();
        Map<String, Integer> listUsersReceiveMessage = new TreeMap<>();
        Map<String, Integer> listUsersReceiveSpamMessage = new TreeMap<>();
        Map<String, Integer> info = new TreeMap<>();
        for (Message m : messageList
        ) {
            if (m.getSender().contains("@bsu.by")) {
                infoAboutUsersPerHour(countSentMessagesPerHour, countSentSpamMessagesPerHour, m);
                infoAboutUsersOverall("out", info, m);
                listUsers(listUsersSentSpamMessage, listUsersSentMessage, m);

            } else {
                infoAboutUsersPerHour(countReceivedMessagesPerHour, countReceivedSpamMessagesPerHour, m);
                infoAboutUsersOverall("in", info, m);
                listUsers(listUsersReceiveSpamMessage, listUsersReceiveMessage, m);
            }
        }
        if (!info.containsKey("outSpam")) info.put("outSpam", 0);
        if (!info.containsKey("out")) info.put("out", 0);
        if (!info.containsKey("in")) info.put("in", 0);
        if (!info.containsKey("inSpam")) info.put("inSpam", 0);
        for (int i = 0; i < 24; i++) {
            if (!countSentMessagesPerHour.containsKey(i)) {
                countSentMessagesPerHour.put(i, 0);
            }
            if (!countSentSpamMessagesPerHour.containsKey(i)) {
                countSentSpamMessagesPerHour.put(i, 0);
            }
            if (!countReceivedMessagesPerHour.containsKey(i)) {
                countReceivedMessagesPerHour.put(i, 0);
            }
            if (!countReceivedSpamMessagesPerHour.containsKey(i)) {
                countReceivedSpamMessagesPerHour.put(i, 0);
            }
        }
        listUsersSentMessage.values();
        list.add(info);
        list.add(countSentMessagesPerHour);
        list.add(countSentSpamMessagesPerHour);
        list.add(countReceivedMessagesPerHour);
        list.add(countReceivedSpamMessagesPerHour);
        sortList(listUsersSentMessage, list);
        sortList(listUsersSentSpamMessage, list);
        sortList(listUsersReceiveMessage, list);
        sortList(listUsersReceiveSpamMessage, list);
        return list;
    }

    private void sortList(Map<String, Integer> map, List<Map<?, Integer>> list) {
        Map<String, Integer> reverseSortedMap = new LinkedHashMap<>();
        map.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .forEachOrdered(x -> reverseSortedMap.put(x.getKey(), x.getValue()));
        list.add(reverseSortedMap);
    }

    private void listUsers(Map<String, Integer> map1, Map<String, Integer> map2, Message m) {
        if (m.getSubject()) {
            if (map1.containsKey(m.getSender())) {
                map1.put(m.getSender(), map1.get(m.getSender()) + m.getRecipient());
            } else {
                map1.put(m.getSender(), m.getRecipient());
            }
        }
        if (map2.containsKey(m.getSender())) {
            map2.put(m.getSender(), map2.get(m.getSender()) + m.getRecipient());
        } else {
            map2.put(m.getSender(), m.getRecipient());
        }

    }

    private void infoAboutUsersOverall(String key, Map<String, Integer> map, Message m) {
        if (map.containsKey(key)) {
            map.put(key, map.get(key) + m.getRecipient());
        } else {
            map.put(key, m.getRecipient());
        }
        if (m.getSubject()) {
            if (map.containsKey(key + "Spam")) {
                map.put(key + "Spam", map.get(key + "Spam") + m.getRecipient());
            } else {
                map.put(key + "Spam", m.getRecipient());
            }
        }

    }

    private void infoAboutUsersPerHour(Map<Integer, Integer> map1, Map<Integer, Integer> map2, Message m) {
        if (m.getSubject()) {
            if (map2.containsKey(m.getDateMessage().getHour())) {
                map2.put(m.getDateMessage().getHour(),
                        map2.get(m.getDateMessage().getHour()) + m.getRecipient());
            } else {
                map2.put(m.getDateMessage().getHour(), m.getRecipient());
            }
        }
        if (map1.containsKey(m.getDateMessage().getHour())) {
            map1.put(m.getDateMessage().getHour(), map1.get(m.getDateMessage().getHour()) + m.getRecipient());
        } else {
            map1.put(m.getDateMessage().getHour(), m.getRecipient());
        }
    }
}
