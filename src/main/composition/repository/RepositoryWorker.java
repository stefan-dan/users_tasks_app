package main.composition.repository;

import main.model.Task;
import main.model.User;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class RepositoryWorker<T> {

    public void add(T object) {
        File file = new File(getFileName(object.getClass()));
        if (file.exists()) {
            appendToExistingFile(object);
        } else {
            addToNewFile(object);
        }
    }

    private void addToNewFile(T object) {
        try (ObjectOutputStream o = new ObjectOutputStream(new FileOutputStream(getFileName(object.getClass())))) {
            o.writeObject(object);
        } catch (FileNotFoundException e) {
            e.printStackTrace(); // will not happen
        } catch (IOException e) {
            System.out.println("Error initializing stream when add to new file");
        }
    }

    private void appendToExistingFile(T object) {
        String fileName = getFileName(object.getClass());
        try (ObjectOutputStream o = new ObjectOutputStream(new FileOutputStream(fileName, true)) {
            @Override
            protected void writeStreamHeader() throws IOException {
                reset();
            }
        }) {
            o.writeObject(object);
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + fileName);
        } catch (IOException e) {
            System.out.println("Error initializing stream when updating existing file");
        }
    }

    @SuppressWarnings("unchecked")
    public List<T> findAll(Class<T> type) {
        List<T> objects = new ArrayList<>();
        String fileName = getFileName(type);
        try (ObjectInputStream oi = new ObjectInputStream(new FileInputStream(fileName))) {
            while (oi.available() >= 0) {
                objects.add((T) oi.readObject());
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + fileName);
        } catch (IOException e) {
            System.out.println("Error initializing stream when reading objects from file");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return objects;
    }

    //TODO maybe pass fileName as a parameter to decouple this class
    public String getFileName(Class<?> type) {
        if (Task.class.equals(type)) {
            return TaskRepository.TASKS_FILE_NAME;
        } else if (User.class.equals(type)) {
            return UserRepository.USERS_FILE_NAME;
        } else {
            return "File not found for type: " + type;
        }
    }
}
