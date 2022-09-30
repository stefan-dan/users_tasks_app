package main.composition;

import main.model.Task;
import main.model.User;
import main.composition.repository.TaskRepository;
import main.composition.repository.UserRepository;

public class Main {
    private static final UserRepository userRepository = new UserRepository();
    private static final TaskRepository taskRepository = new TaskRepository();

    public static void main(String[] args) {
        if (args.length > 0) {
            if ("-createUser".equals(args[0])) {
                createUser(args);
            } else if ("-showAllUsers".equals(args[0])) {
                showAllUser();
            } else if ("-addTask".equals(args[0])) {
                addTask(args);
            } else if ("-showTasks".equals(args[0])) {
                showTasks(args);
            } else {
                System.err.println("Unknown method");
            }
        } else {
            System.err.println("No arguments specified");
        }
    }

    private static void createUser(String[] args) {
        User user = new User();
        if (args.length == 4 && args[1].startsWith("-fn='") && args[2].startsWith("-ln='") && args[3].startsWith("-un='")) {
            user.setFirstName(getValidatedValueFrom(args[1]));
            user.setLastName(getValidatedValueFrom(args[2]));
            user.setUserName(getValidatedValueFrom(args[3]));
        } else {
            throw new IllegalArgumentException("Not all (or more) arguments are specified, or they are not in the right order.");
        }

        userRepository.saveUser(user);
        System.out.println("User was successfully created.");
    }

    private static void addTask(String[] args) {
        Task task = new Task();
        if (args.length == 4 && args[1].startsWith("-un='") && args[2].startsWith("-tt='") && args[3].startsWith("-td='")) {
            task.setUsername(getValidatedValueFrom(args[1]));
            task.setTitle(getValidatedValueFrom(args[2]));
            task.setDescription(getValidatedValueFrom(args[3]));
        } else {
            throw new IllegalArgumentException("Not all arguments are specified, or they are not in the right order.");
        }

        taskRepository.addTask(task);
        System.out.println("Task was successfully added.");
    }

    private static void showAllUser() {
        userRepository.findAll()
                .forEach(System.out::println);
    }

    private static void showTasks(String[] args) {
        if (args.length == 2 && args[1].startsWith("-un='")) {
            taskRepository.findAllByUsername(getValidatedValueFrom(args[1]))
                    .forEach(System.out::println);
        } else {
            throw new IllegalArgumentException("Username is missing, or there are more arguments.");
        }
    }

    private static String getValidatedValueFrom(String argument) {
        String value = argument.length() > 5 ? argument.substring(5, argument.length() - 1).trim() : "";
        if (value.length() <= 3) {
            throw new IllegalArgumentException("Value's length should be more then 3 characters: " + argument);
        }
        return value;
    }
}
