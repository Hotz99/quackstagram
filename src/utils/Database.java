package utils;

import java.io.*;
import java.util.*;

public class Database {

  private String username;
  private String password;
  private HashMap<String, String> users = new HashMap();
  private static final String FILE_NAME = "users.txt";

  public Database(String username, String password) {
    this.username = username;
    this.password = password;
    loadUsers();
  }

  public void addUser(String username, String password) {
    users.put(username, password);
    saveUsers();
  }

  public void removeUser(String username) {
    users.remove(username);
    saveUsers();
  }

  public void updateUser(String username, String password) {
    users.put(username, password);
    saveUsers();
  }

  private void loadUsers() {
    try {
      FileInputStream fis = new FileInputStream(FILE_NAME);
      ObjectInputStream ois = new ObjectInputStream(fis);
      users = (HashMap<String, String>) ois.readObject();
      ois.close();
      fis.close();
    } catch (IOException ioe) {
      ioe.printStackTrace();
    } catch (ClassNotFoundException c) {
      System.out.println("Class not found");
      c.printStackTrace();
    }
  }

  private void saveUsers() {
    try {
      FileOutputStream fos = new FileOutputStream(FILE_NAME);
      ObjectOutputStream oos = new ObjectOutputStream(fos);
      oos.writeObject(users);
      oos.close();
      fos.close();
    } catch (IOException ioe) {
      ioe.printStackTrace();
    }
  }

  public boolean containsKey(String username2) {
    return users.containsKey(username2);
  }

  public Object get(String username2) {
    return users.get(username2);
  }
}
