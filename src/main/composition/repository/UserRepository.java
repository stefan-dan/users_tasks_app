package main.composition.repository;

import main.model.User;

import java.util.List;

public class UserRepository {
    public static final String USERS_FILE_NAME = "users.txt";

    private final RepositoryWorker<User> repositoryWorker = new RepositoryWorker<>();

    public void saveUser(User user) {
        if (usernameExists(user.getUserName())) {
            throw new IllegalArgumentException("Username has already been used.");
        }
        repositoryWorker.add(user);
    }

    public boolean usernameExists(String username) {
        return repositoryWorker.findAll(User.class)
                .stream()
                .anyMatch(user -> user.getUserName().equals(username));
    }

    public List<User> findAll() {
        return repositoryWorker.findAll(User.class);
    }
}
