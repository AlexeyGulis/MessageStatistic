package bsu.mail.MessageStatistic.controllers;

import bsu.mail.MessageStatistic.DTO.BlockUserDTO;
import bsu.mail.MessageStatistic.DTO.MessageDTO;
import bsu.mail.MessageStatistic.models.BlockUser;
import bsu.mail.MessageStatistic.models.Message;
import bsu.mail.MessageStatistic.services.BlockUsersService;
import bsu.mail.MessageStatistic.services.MessagesService;
import bsu.mail.MessageStatistic.util.MessageErrorResponse;
import bsu.mail.MessageStatistic.util.MessageNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/api")
public class MessageController {

    private MessagesService messagesService;
    private BlockUsersService blockUsersService;
    private ModelMapper modelMapper;

    @Autowired
    public MessageController(MessagesService messagesService, BlockUsersService blockUsersService, ModelMapper modelMapper) {
        this.messagesService = messagesService;
        this.blockUsersService = blockUsersService;
        this.modelMapper = modelMapper;
    }

    public void saveMessage(Message message) {
        messagesService.saveMessage(message);
    }

    @GetMapping("/date")
    public String showDateStatistics(Model model) {
        List<Map<?, Integer>> list = messagesService.getInformationAboutMessages(LocalDate.parse("2022-12-28",
                DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        model.addAttribute("listUsersSentMessage", list.get(5));
        model.addAttribute("listUsersSentSpamMessage",list.get(6));
        return "statistics/dateOfStatistic";
    }

    @GetMapping("/statistics")
    public String showStatistics(@RequestParam(name = "date", required = false) String date, Model model) {
        List<Map<?, Integer>> list;
        List<BlockUser> blockUsers;
        if (date == null || "".equals(date)) {
            date = LocalDate.now().toString();
            list = messagesService.getInformationAboutMessages(LocalDate.now());
            blockUsers = blockUsersService.getBlockedUsers(LocalDate.now());
        } else {
            list = messagesService.getInformationAboutMessages(LocalDate.parse(date,
                            DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            blockUsers = blockUsersService.getBlockedUsers(LocalDate.parse(date,
                    DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        }

        model.addAttribute("date", date);
        model.addAttribute("in", list.get(0).get("in"));
        model.addAttribute("inSpam", list.get(0).get("inSpam"));
        model.addAttribute("out", list.get(0).get("out"));
        model.addAttribute("outSpam", list.get(0).get("outSpam"));
        model.addAttribute("keysOut", list.get(1).keySet());
        model.addAttribute("val1Out", list.get(1).values());
        model.addAttribute("val2Out", list.get(2).values());
        model.addAttribute("keysIn", list.get(3).keySet());
        model.addAttribute("val1In", list.get(3).values());
        model.addAttribute("val2In", list.get(4).values());
        model.addAttribute("listUsersSentMessage", list.get(5));
        model.addAttribute("listUsersSentSpamMessage",list.get(6));
        model.addAttribute("listUsersReceiveMessage", list.get(7));
        model.addAttribute("listUsersReceiveSpamMessage",list.get(8));
        model.addAttribute("blockUsers", blockUsers);
        return "statistics/stat";
    }

    @ResponseBody
    @PostMapping("/add")
    public ResponseEntity<HttpStatus> create(@RequestBody MessageDTO messageDTO) {
        messagesService.saveMessage(convertToMessage(messageDTO));
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @ResponseBody
    @PostMapping("/addNewMessages")
    public ResponseEntity<HttpStatus> createList(@RequestBody List<MessageDTO> messageDTOList) {
        messagesService.saveMessageList(messageDTOList.stream().map(this::convertToMessage).collect(Collectors.toList()));
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @ResponseBody
    @PostMapping("/addBlockedUser")
    public ResponseEntity<HttpStatus> addBlockUser(@RequestBody BlockUserDTO blockUserDTO){
        List<BlockUserDTO> list = new ArrayList<>();
        list.add(blockUserDTO);
        blockUsersService.addBlockUsersList(list.stream().map(this::convertToBlockUser).collect(Collectors.toList()));
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @ResponseBody
    @PostMapping("/addBlockedUsers")
    public ResponseEntity<HttpStatus> addBlockUsers(@RequestBody List<BlockUserDTO> blockUserDTOList){
        blockUsersService.addBlockUsersList(blockUserDTOList.stream().map(this::convertToBlockUser).collect(Collectors.toList()));
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @ResponseBody
    @GetMapping("/messages")
    public List<MessageDTO> getMessages() {
        return messagesService.findAll().stream().map(this::convertToMessageDTO).collect(Collectors.toList());
    }

    @ResponseBody
    @GetMapping("/{id}")
    public MessageDTO getMessage(@PathVariable("id") int id) {
        return convertToMessageDTO(messagesService.findOne(id));
    }

    @ExceptionHandler
    private ResponseEntity<MessageErrorResponse> handleException(MessageNotFoundException ex) {
        MessageErrorResponse messageErrorResponse = new MessageErrorResponse("Message with this id not found", System.currentTimeMillis());
        return new ResponseEntity<>(messageErrorResponse, HttpStatus.NOT_FOUND);
    }

    private Message convertToMessage(MessageDTO messageDTO) {
        return modelMapper.map(messageDTO, Message.class);
    }

    private MessageDTO convertToMessageDTO(Message message) {
        return modelMapper.map(message, MessageDTO.class);
    }
    private BlockUser convertToBlockUser(BlockUserDTO blockUserDTO) {
        return modelMapper.map(blockUserDTO, BlockUser.class);
    }

    private BlockUserDTO convertToBlockUserDTO(BlockUser blockUser) {
        return modelMapper.map(blockUser, BlockUserDTO.class);
    }
}
