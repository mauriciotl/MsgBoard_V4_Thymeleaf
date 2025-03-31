package com.mau.msgboard_v4_thymeleaf.app.controller;

import com.mau.msgboard_v4_thymeleaf.app.model.Message;
import com.mau.msgboard_v4_thymeleaf.app.model.User;
import com.mau.msgboard_v4_thymeleaf.app.service.MessageService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.sql.Timestamp;

@Controller
@RequestMapping("/messages")
public class MessageBoardController {

    private final MessageService messageService;

    public MessageBoardController(MessageService messageService) {
        this.messageService = messageService;
    }

    @GetMapping
    public String showMessageBoard(Model model, @AuthenticationPrincipal User user) {
        model.addAttribute("user", user);
        model.addAttribute("messages", messageService.getAllMessages());
        return "messageBoard";
    }

    @ModelAttribute(name = "message")
    public Message message() {
        return new Message();
    }

    @PostMapping("/add")
    public String addMessage(@Valid @ModelAttribute("message") Message message, BindingResult result, Model model,
                             @AuthenticationPrincipal User user) {
        if (result.hasErrors()) {
            model.addAttribute("messages", messageService.getAllMessages());
            model.addAttribute("user", user);
            return "messageBoard";
        }
        model.addAttribute("user", user);
        message.setUserId(user.getUserId());
        message.setCreationDate(new Timestamp(System.currentTimeMillis()));
        messageService.saveMessage(message);
        return "redirect:/messages";
    }

    @GetMapping("/edit/{id}")
    public String showEditMessage(@PathVariable("id") int messageId, Model model) {
        Message message = messageService.getMessageById(messageId);
        if (message == null) {
            return "redirect:/messages";
        }
        model.addAttribute("message", message);
        return "editMessage";
    }

    @PostMapping("/update")
    public String updateMessage(@Valid @ModelAttribute("message") Message message, BindingResult result) {
        if (result.hasErrors()) {
            return "editMessage";
        }
        message.setCreationDate(new Timestamp(System.currentTimeMillis()));
        messageService.updateMessage(message);
        return "redirect:/messages";
    }

    @GetMapping("/delete/{id}")
    public String deleteMessage(@PathVariable("id") int messageId) {
        messageService.deleteMessage(messageId);
        return "redirect:/messages";
    }
}
