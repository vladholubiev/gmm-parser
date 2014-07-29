package ua.samosfator.gmm.competitions.parser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedHashSet;

public class UserList {
    private LinkedHashSet<User> users = new LinkedHashSet<>();

    public LinkedHashSet<User> readUserList(String filename) {
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
