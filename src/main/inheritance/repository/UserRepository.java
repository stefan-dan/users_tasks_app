package main.inheritance.repository;

import main.model.User;

public class UserRepository extends GenericRepository<User> {
    private static final String USERS_FILE_NAME = "users.txt";

    public void saveUser(User user) {
        if (usernameExists(user.getUserName())) {
            throw new IllegalArgumentException("Username has already been used.");
        }
        super.add(user);
    }

    public boolean usernameExists(String username) {
        return findAll()
                .stream()
                .anyMatch(user -> user.getUserName().equals(username));
    }

    @Override
    public String getFileName() {
        return USERS_FILE_NAME;
    }
}
