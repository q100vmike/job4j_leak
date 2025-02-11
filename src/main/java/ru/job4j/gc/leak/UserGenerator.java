package ru.job4j.gc.leak;

import ru.job4j.gc.leak.models.User;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class UserGenerator implements Generate {

    public final String pathnames = "files/names.txt";
    public final String pathsurnames = "files/surnames.txt";
    public final String pathpatrons = "files/patr.txt";

    public final String separator = " ";
    public final Integer newusers = 1000;

    public List<String> names;
    public List<String> patrons;
    private List<String> surnames;
    private final List<User> users = new ArrayList<>();
    private final Random random;

    public UserGenerator(Random random) {
        this.random = random;
        readAll();
    }

    @Override
    public void generate() {
        users.clear();
        for (int i = 0; i < newusers; i++) {
            var user = new User();
            user.setName(String.format("%s%s%s%s%s",
                    surnames.get(random.nextInt(surnames.size())), separator,
                    names.get(random.nextInt(names.size())), separator,
                    patrons.get(random.nextInt(patrons.size()))));
            users.add(user);
        }
    }

    private void readAll() {
        try {
            names = read(pathnames);
            surnames = read(pathsurnames);
            patrons = read(pathpatrons);
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public User randomUser() {
        return users.get(random.nextInt(users.size()));
    }
}