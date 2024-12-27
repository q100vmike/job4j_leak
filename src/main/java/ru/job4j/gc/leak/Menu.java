package ru.job4j.gc.leak;

import ru.job4j.gc.leak.models.Post;

import java.util.Random;
import java.util.Scanner;

public class Menu {

    public final Integer ADD_POST = 1;
    public final Integer ADD_MANY_POST = 2;
    public final Integer SHOW_ALL_POSTS = 3;
    public final Integer DELETE_POST = 4;

    public final String SELECT = "Выберите меню";
    public final String COUNT = "Выберите количество создаваемых постов";
    public final String TEXT_OF_POST = "Введите текст";
    public final String EXIT = "Конец работы";

    public final String MENU = """
                Введите 1 для создание поста.
                Введите 2, чтобы создать определенное количество постов.
                Введите 3, чтобы показать все посты.
                Введите 4, чтобы удалить все посты.
                Введите любое другое число для выхода.
            """;

    public static void main(String[] args) {
        Random random = new Random();
        Menu menu = new Menu();
        UserGenerator userGenerator = new UserGenerator(random);
        CommentGenerator commentGenerator = new CommentGenerator(random, userGenerator);
        Scanner scanner = new Scanner(System.in);
        PostStore postStore = new PostStore();
        menu.start(commentGenerator, scanner, userGenerator, postStore);
    }

    private void start(CommentGenerator commentGenerator, Scanner scanner, UserGenerator userGenerator, PostStore postStore) {
        boolean run = true;
        while (run) {
            System.out.println(MENU);
            System.out.println(SELECT);
            int userChoice = Integer.parseInt(scanner.nextLine());
            System.out.println(userChoice);
            if (ADD_POST == userChoice) {
                System.out.println(TEXT_OF_POST);
                String text = scanner.nextLine();
                userGenerator.generate();
                commentGenerator.generate();
                var post = new Post();
                post.setText(text);
                post.setComments(CommentGenerator.getComments());
                var saved = postStore.add(post);
                System.out.println("Generate: " + saved.getId());
            } else if (ADD_MANY_POST == userChoice) {
                System.out.println(TEXT_OF_POST);
                String text = scanner.nextLine();
                System.out.println(COUNT);
                String count = scanner.nextLine();
                memUsage();
                for (int i = 0; i < Integer.parseInt(count); i++) {
                    System.out.printf("\rGenerate %.2f%% %.2fMb",
                            ((double) i / Integer.parseInt(count)) * 100,
                            memUsage());
                    createPost(commentGenerator, userGenerator, postStore, text);
                }
                System.out.println();
                memUsage();
            } else if (SHOW_ALL_POSTS == userChoice) {
                System.out.println(postStore.getPosts());
            } else if (DELETE_POST == userChoice) {
                System.out.println("Удаление всех постов ...");
                postStore.removeAll();
            } else {
                run = false;
                System.out.println(EXIT);
            }
        }
    }

    private double memUsage() {
        var rt = Runtime.getRuntime();
        var totalMem = rt.totalMemory();
        var freeMem = rt.freeMemory();
        var usedMem = totalMem - freeMem;
        return (double) usedMem / 1024 / 1024;
    }

    private void createPost(CommentGenerator commentGenerator,
                                   UserGenerator userGenerator,
                                   PostStore postStore, String text) {
        userGenerator.generate();
        commentGenerator.generate();
        var post = new Post();
        post.setText(text);
        post.setComments(CommentGenerator.getComments());
        postStore.add(post);
    }
}