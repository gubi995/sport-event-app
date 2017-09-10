package hu.szeged.sporteventapp.common.factory.server;

import hu.szeged.sporteventapp.backend.data.entity.SportEvent;
import hu.szeged.sporteventapp.backend.data.entity.User;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;

@Component
public class MyBeanFactory {

    public static SportEvent createNewSportEvent(){
        return new SportEvent("", "", LocalDateTime.now(),
                LocalDateTime.now(), 20,"", "",false, new User(), new HashSet<>());
    }
}
