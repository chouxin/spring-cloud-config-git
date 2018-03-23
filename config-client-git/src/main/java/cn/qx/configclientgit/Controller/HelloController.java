package cn.qx.configclientgit.Controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @Value("${qx.hello}")
    private String gitConfigName;

    @GetMapping("/hello")
    public String fromGitHubServer(){
        return gitConfigName;
    }

}
