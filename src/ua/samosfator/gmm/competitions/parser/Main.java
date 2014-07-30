package ua.samosfator.gmm.competitions.parser;

public class Main {
    public static void main(String[] args) {
        if (!args[0].equals("simple")) new Parser(new UserList().readUserList("users.txt")).start();
        else new SimpleParser(new UserList().readUserList("users.txt")).start();
    }
}
