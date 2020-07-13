package az.ibatech.todo.utility;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
@Slf4j
public class GenerateCustomToken {

    String s = "qwertyuiopasdfghjklzxcvbnmQWERTYUIOASDFGHJKLZXCVBNM1234567890";

    public String generateToken(){
        log.info("Starting to generate custom token");
        String token = "";
        Random rand = new Random();
        while(token.length()<50){
            token+=s.charAt(rand.nextInt(50));
        }

        log.info("Generated token is : {}",token);
        return token;
    }

}
