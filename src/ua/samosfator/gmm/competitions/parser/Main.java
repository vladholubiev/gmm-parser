package ua.samosfator.gmm.competitions.parser;

public class Main {
    public static void main(String[] args) throws Throwable {
        new Parser(new UserList().readUserList("users.txt")).start();
    }
}
