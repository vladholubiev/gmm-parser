package ua.samosfator.gmm.competitions.parser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;

public class UserList {
    private HashSet<User> users = new HashSet<>();

    public HashSet<User> readUserList(String filename) {
        try (BufferedReader br = new BufferedReader(new FileReader(new File(filename)))) {
            String line;
            while ((line = br.readLine()) != null) {
                users.add(new User(line));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return users;
    }
}
