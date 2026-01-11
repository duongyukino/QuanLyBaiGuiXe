package OOP.Parking.DAO;

import OOP.Parking.model.User;
import OOP.Parking.util.CSVUtils;
import OOP.Parking.util.DataPath;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class UserDAO implements BaseDAO<User, String> {
    private static final String FILE_NAME = "users.csv";
    private static UserDAO instance;
    private Map<String, User> users;

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
        String filePath = DataPath.getFilePath(FILE_NAME);
        File file = new File(filePath);
        
        if (!file.exists()) {
            System.err.println("File không tồn tại: " + filePath + ". Tạo file mới.");
            // Tạo file mới nếu chưa có
            if (file.createNewFile()) {
                // Ghi tài khoản mặc định
                List<String[]> defaultData = new ArrayList<>();
                defaultData.add(new String[]{"admin", "123", "QUẢN LÝ", "Admin", "true", "admin@parking.com"});
                defaultData.add(new String[]{"baove", "123", "BẢO VỆ", "Bảo Vệ", "true", "baove@parking.com"});
                CSVUtils.writeCSV(filePath, defaultData);
            }
        }
        
        List<String[]> records = CSVUtils.readCSV(filePath);
        for (String[] record : records) {
            if (record.length >= 6) {
                try {
                    User user = new User(
                            record[0], record[1], record[2], record[3],
                            Boolean.parseBoolean(record[4]), record[5]
                    );
                    users.put(user.getUsername(), user);
                } catch (Exception e) {
                    System.err.println("Lỗi parse user: " + Arrays.toString(record));
                }
            }
        }
    }

    private void saveToFile() throws IOException {
        String filePath = DataPath.getFilePath(FILE_NAME);
        List<String[]> data = new ArrayList<>();
        for (User user : users.values()) {
            data.add(new String[]{
                    user.getUsername(), user.getPassword(), user.getRole(),
                    user.getFullName(), String.valueOf(user.isActive()), user.getEmail()
            });
        }
        CSVUtils.writeCSV(filePath, data);
    }

    @Override public List<User> getAll() { return new ArrayList<>(users.values()); }
    @Override public User getById(String id) { return users.get(id); }
    @Override public boolean add(User obj) throws IOException { 
        if (users.containsKey(obj.getUsername())) return false;
        users.put(obj.getUsername(), obj); saveToFile(); return true; 
    }
    @Override public boolean update(User obj) throws IOException {
        if (!users.containsKey(obj.getUsername())) return false;
        users.put(obj.getUsername(), obj); saveToFile(); return true;
    }
    @Override public boolean delete(String id) throws IOException {
        if (!users.containsKey(id)) return false;
        users.remove(id); saveToFile(); return true;
    }
    @Override public boolean saveAll(List<User> list) throws IOException {
        users.clear(); for(User u : list) users.put(u.getUsername(), u); saveToFile(); return true;
    }
    public User authenticate(String username, String password) {
        User user = users.get(username);
        return (user != null && user.authenticate(password) && user.isActive()) ? user : null;
    }
}