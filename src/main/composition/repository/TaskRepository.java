package main.composition.repository;

import main.model.Task;

import java.util.List;

public class TaskRepository {
    public static final String TASKS_FILE_NAME = "tasks.txt";

    private final UserRepository userRepository = new UserRepository();
    private final RepositoryWorker<Task> repositoryWorker = new RepositoryWorker<>();

    public void addTask(Task task) {
        if (!userRepository.usernameExists(task.getUsername())) {
            throw new IllegalArgumentException("No such username.");
        }
        repositoryWorker.add(task);
    }

    public List<Task> findAllByUsername(String username) {
        return repositoryWorker.findAll(Task.class)
                .stream()
                .filter(task -> task.getUsername().equals(username))
                .toList();
    }
}
