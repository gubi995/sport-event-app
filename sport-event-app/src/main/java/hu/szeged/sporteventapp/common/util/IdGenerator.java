package hu.szeged.sporteventapp.common.util;

import org.springframework.util.AlternativeJdkIdGenerator;

public final class IdGenerator {

    private static org.springframework.util.IdGenerator idGenerator = new AlternativeJdkIdGenerator();

    private IdGenerator() {
        throw new UnsupportedOperationException("IdGenerator is an utility class");
    }

    public static String generateId() {
        return idGenerator.generateId().toString().replaceAll("-", "");
    }

}