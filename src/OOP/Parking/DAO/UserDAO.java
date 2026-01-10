package OOP.Parking.DAO;

import OOP.Parking.model.User;
import OOP.Parking.util.CSVUtils;
import java.io.File;
import java.io.IOException;
import java.util.*;

public class UserDAO implements BaseDAO<User, String> {
    // Sử dụng đường dẫn tuyệt đối hoặc tương đối từ thư mục gốc dự án
    // Lưu ý: Khi chạy từ IDE, thư mục gốc là thư mục chứa pom.xml
    private static final String FILE_PATH = "src/main/resources/data/users.csv";
    private static UserDAO instance;
    private Map<String, User> users;

    // Singleton Pattern
    private UserDAO() throws IOException {
        users = new HashMap<>();
        loadData();
    }

    public static synchronized UserDAO getInstance() throws IOException {
        if (instance == null) {
            instance = new UserDAO();
        }
        return instance;
    }

    private void loadData() throws IOException {
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            System.err.println("File không tồn tại: " + file.getAbsolutePath());
            return;
        }
        
        List<String[]> records = CSVUtils.readCSV(FILE_PATH);
        for (String[] record : records) {
            if (record.length >= 6) {
                try {
                    User user = new User(
                            record[0],  // username
                            record[1],  // password
                            record[2],  // role
                            record[3],  // fullName
                            Boolean.parseBoolean(record[4]),  // active
                            record[5]   // email
                    );
                    users.put(user.getUsername(), user);
                } catch (Exception e) {
                    System.err.println("Lỗi parse dòng user: " + Arrays.toString(record));
                }
            }
        }
        System.out.println("Đã load " + users.size() + " users từ " + file.getAbsolutePath());
    }

    private void saveToFile() throws IOException {
        List<String[]> data = new ArrayList<>();
        for (User user : users.values()) {
            String[] record = {
                    user.getUsername(),
                    user.getPassword(),
                    user.getRole(),
                    user.getFullName(),
                    String.valueOf(user.isActive()),
                    user.getEmail()
            };
            data.add(record);
        }
        CSVUtils.writeCSV(FILE_PATH, data);
    }

    @Override
    public List<User> getAll() throws IOException {
        return new ArrayList<>(users.values());
    }

    @Override
    public User getById(String username) throws IOException {
        return users.get(username);
    }

    @Override
    public boolean add(User user) throws IOException {
        if (users.containsKey(user.getUsername())) {
            return false;
        }
        users.put(user.getUsername(), user);
        saveToFile();
        return true;
    }

    @Override
    public boolean update(User user) throws IOException {
        if (!users.containsKey(user.getUsername())) {
            return false;
        }
        users.put(user.getUsername(), user);
        saveToFile();
        return true;
    }

    @Override
    public boolean delete(String username) throws IOException {
        if (!users.containsKey(username)) {
            return false;
        }
        users.remove(username);
        saveToFile();
        return true;
    }

    @Override
    public boolean saveAll(List<User> list) throws IOException {
        users.clear();
        for (User user : list) {
            users.put(user.getUsername(), user);
        }
        saveToFile();
        return true;
    }

    // Phương thức đặc biệt cho User
    public User authenticate(String username, String password) throws IOException {
        User user = getById(username);
        if (user != null && user.authenticate(password) && user.isActive()) {
            return user;
        }
        return null;
    }
}