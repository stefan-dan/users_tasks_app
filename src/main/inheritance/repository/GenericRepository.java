package main.inheritance.repository;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public abstract class GenericRepository<T> {
    public static final String FILE_NOT_FOUND = "File not found!";
    public static final String INIT_STREAM_ERROR = "Error initializing stream";
    private static final Logger logger = Logger.getLogger(GenericRepository.class.getName());

    public void add(T object) {
        File file = new File(getFileName());
        if (!file.exists()) {
            createNewFile(object);
        } else {
            updateExistingFile(object);
        }
    }

    private void createNewFile(T object) {
        try (FileOutputStream f = new FileOutputStream(getFileName());
             ObjectOutputStream o = new ObjectOutputStream(f)) {
            o.writeObject(object);
        } catch (FileNotFoundException e) {
            logger.info(FILE_NOT_FOUND);
        } catch (IOException e) {
            logger.severe(INIT_STREAM_ERROR);
        }
    }

    private void updateExistingFile(T object) {
        try (FileOutputStream f = new FileOutputStream(getFileName(), true);
             ObjectOutputStream o = new ObjectOutputStream(f) {
                 @Override
                 protected void writeStreamHeader() throws IOException {
                     reset();
                 }
             }) {
            o.writeObject(object);
        } catch (FileNotFoundException e) {
            logger.severe(FILE_NOT_FOUND);
        } catch (IOException e) {
            logger.severe(INIT_STREAM_ERROR);
        }
    }

    @SuppressWarnings("unchecked")
    public List<T> findAll() {
        List<T> objects = new ArrayList<>();
        try (InputStream buffer = new BufferedInputStream(new FileInputStream(getFileName()));
             ObjectInput input = new ObjectInputStream(buffer)) {
            //deserialize the List
            objects = (List<T>) input.readObject();
        } catch (ClassNotFoundException ex) {
            logger.severe("Cannot perform input. Class not found.");
        } catch (IOException ex) {
            logger.severe("Cannot perform input.");
        }

        return objects;
    }

    protected abstract String getFileName();
}
