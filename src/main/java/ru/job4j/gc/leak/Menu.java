package ru.job4j.gc.leak;

import ru.job4j.gc.leak.models.Post;

import java.util.Random;
import java.util.Scanner;

public class Menu {

    public final Integer addpost = 1;
    public final Integer addmanypost = 2;
    public final Integer showallposts = 3;
    public final Integer deletepost = 4;

    public final String select = "Выберите меню";
    public final String count = "Выберите количество создаваемых постов";
    public final String textofpost = "Введите текст";
    public final String exit = "Конец работы";

    public final String menu = """
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
            System.out.println(menu);
            System.out.println(select);
            int userChoice = Integer.parseInt(scanner.nextLine());
            System.out.println(userChoice);
            if (addpost == userChoice) {
                System.out.println(textofpost);
                String text = scanner.nextLine();
                userGenerator.generate();
                commentGenerator.generate();
                var post = new Post();
                post.setText(text);
                post.setComments(commentGenerator.getComments());
                var saved = postStore.add(post);
                System.out.println("Generate: " + saved.getId());
            } else if (addmanypost == userChoice) {
                System.out.println(textofpost);
                String text = scanner.nextLine();
                System.out.println(count);
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
            } else if (showallposts == userChoice) {
                System.out.println(postStore.getPosts());
            } else if (deletepost == userChoice) {
                System.out.println("Удаление всех постов ...");
                postStore.removeAll();
            } else {
                run = false;
                System.out.println(exit);
            }
        }
    }

    private double memUsage() {
        Runtime rt = Runtime.getRuntime();
        long totalMem = rt.totalMemory();
        long freeMem = rt.freeMemory();
        return (double) (totalMem - freeMem) / 1024 / 1024;
    }

    private void createPost(CommentGenerator commentGenerator,
                                   UserGenerator userGenerator,
                                   PostStore postStore, String text) {
        userGenerator.generate();
        commentGenerator.generate();
        var post = new Post();
        post.setText(text);
        post.setComments(commentGenerator.getComments());
        postStore.add(post);
    }
}