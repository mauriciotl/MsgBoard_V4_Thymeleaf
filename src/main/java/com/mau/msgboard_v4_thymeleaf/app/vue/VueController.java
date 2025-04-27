package com.mau.msgboard_v4_thymeleaf.app.vue;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class VueController {
    @GetMapping("/vueMessages")
    public String getMessagesPage() {
        return "/vueApp/messagesVue";
    }
}
