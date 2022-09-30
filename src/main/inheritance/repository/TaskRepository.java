package main.inheritance.repository;

import main.model.Task;

import java.util.List;

public class TaskRepository extends GenericRepository<Task> {
    public static final String TASKS_FILE_NAME = "tasks.txt";

    private final UserRepository userRepository = new UserRepository();

    public void addTask(Task task) {
        if (!userRepository.usernameExists(task.getUsername())) {
            throw new IllegalArgumentException("No such username.");
        }
        super.add(task);
    }

    public List<Task> findAllByUsername(String username) {
        return findAll()
                .stream()
                .filter(task -> task.getUsername().equals(username))
                .toList();
    }

    @Override
    public String getFileName() {
        return TASKS_FILE_NAME;
    }
}
