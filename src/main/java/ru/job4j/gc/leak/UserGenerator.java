package ru.job4j.gc.leak;

import ru.job4j.gc.leak.models.User;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class UserGenerator implements Generate {

    public final String PATH_NAMES = "files/names.txt";
    public final String PATH_SURNAMES = "files/surnames.txt";
    public final String PATH_PATRONS = "files/patr.txt";

    public final String SEPARATOR = " ";
    public final Integer NEW_USERS = 1000;

    public List<String> names;
    public List<String> patrons;
    private List<String> surnames;
    private final List<User> USERS = new ArrayList<>();
    private final Random random;

    public UserGenerator(Random random) {
        this.random = random;
        readAll();
    }

    @Override
    public void generate() {
        USERS.clear();
        for (int i = 0; i < NEW_USERS; i++) {
            var user = new User();
            user.setName(String.format("%s%s%s%s%s",
                    surnames.get(random.nextInt(surnames.size())), SEPARATOR,
                    names.get(random.nextInt(names.size())), SEPARATOR,
                    patrons.get(random.nextInt(patrons.size()))));
            USERS.add(user);
        }
    }

    private void readAll() {
        try {
            names = read(PATH_NAMES);
            surnames = read(PATH_SURNAMES);
            patrons = read(PATH_PATRONS);
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public User randomUser() {
        return USERS.get(random.nextInt(USERS.size()));
    }
}