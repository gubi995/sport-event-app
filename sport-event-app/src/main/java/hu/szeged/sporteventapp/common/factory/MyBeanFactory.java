package hu.szeged.sporteventapp.common.factory;

import java.time.LocalDateTime;
import java.util.ArrayList;

import org.springframework.stereotype.Component;

import hu.szeged.sporteventapp.backend.data.entity.SportEvent;
import hu.szeged.sporteventapp.backend.data.entity.User;

@Component
public class MyBeanFactory {

    public static SportEvent createNewSportEvent(){
        return new SportEvent("", "", LocalDateTime.now(),
				LocalDateTime.now(), 20, "", "", new User(), new ArrayList<>());
    }
}