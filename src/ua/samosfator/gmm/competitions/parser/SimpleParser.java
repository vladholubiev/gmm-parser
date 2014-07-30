package ua.samosfator.gmm.competitions.parser;

import java.util.LinkedHashSet;

public class SimpleParser extends Parser {

    public SimpleParser(LinkedHashSet<User> users) {
        super(users);
    }

    @Override
    public void start() {
        for (User user : users) {
            sender.write(user.setFullInfo());
        }
    }
}
