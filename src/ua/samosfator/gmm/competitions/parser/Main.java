package ua.samosfator.gmm.competitions.parser;

public class Main {
    public static void main(String[] args) {
        if (args.length > 0 && args[0].equals("simple")) new SimpleParser(new UserList().readUserList("users.txt")).start();
        else new Parser(new UserList().readUserList("users.txt")).start();
    }
}
