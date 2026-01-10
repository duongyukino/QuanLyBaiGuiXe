package OOP.Parking.DAO;

import java.io.IOException;
import java.util.List;

/**
 * Interface cho tất cả các DAO
 * T - Loại đối tượng
 * ID - Kiểu của ID (String, Integer, etc.)
 */
public interface BaseDAO<T, ID> {
    /**
     * Lấy tất cả đối tượng
     */
    List<T> getAll() throws IOException;

    /**
     * Lấy đối tượng theo ID
     */
    T getById(ID id) throws IOException;

    /**
     * Thêm mới đối tượng
     */
    boolean add(T obj) throws IOException;

    /**
     * Cập nhật đối tượng
     */
    boolean update(T obj) throws IOException;

    /**
     * Xóa đối tượng theo ID
     */
    boolean delete(ID id) throws IOException;

    /**
     * Lưu tất cả đối tượng
     */
    boolean saveAll(List<T> list) throws IOException;
}