# 🅿️ PARKING SYSTEM - Hệ Thống Quản Lý Bãi Gửi Xe

![Java](https://img.shields.io/badge/Java-17%2B-orange) ![Swing](https://img.shields.io/badge/GUI-Swing-blue) ![License](https://img.shields.io/badge/License-MIT-green)

**Parking System** là một ứng dụng Desktop được xây dựng bằng Java Swing, giúp quản lý hoạt động của bãi gửi xe một cách hiệu quả, chính xác và chuyên nghiệp. Hệ thống hỗ trợ quản lý xe ra vào, vé tháng, tính phí tự động và báo cáo doanh thu chi tiết.

---

## 🚀 Tính Năng Nổi Bật

### 1. Quản Lý Vận Hành (Dành cho Bảo Vệ)
*   **Check-in xe nhanh chóng:** Chỉ cần nhập biển số, hệ thống tự động nhận diện vé tháng hoặc vé lượt.
*   **Check-out & Tính phí tự động:**
    *   Tự động tính tiền dựa trên loại xe (Xe máy/Ô tô) và thời gian gửi.
    *   Hỗ trợ vé tháng (miễn phí khi ra).
    *   Hiển thị số tiền cần thu rõ ràng.
*   **Tra cứu xe:** Tìm kiếm xe đang có trong bãi, xem thời gian vào.
*   **Theo dõi chỗ trống:** Hiển thị số lượng chỗ trống theo thời gian thực ngay trên màn hình.

### 2. Quản Lý & Thống Kê (Dành cho Admin)
*   **Quản lý Vé Tháng:**
    *   Đăng ký vé tháng mới.
    *   Gia hạn vé tháng.
    *   Quản lý danh sách khách hàng thân thiết.
*   **Báo Cáo Doanh Thu:**
    *   **Theo Ngày:** Chi tiết doanh thu vé lượt.
    *   **Theo Tháng:** Tổng hợp doanh thu từ Vé lượt và Vé tháng (Đăng ký mới/Gia hạn).
    *   Phân tách rõ ràng nguồn thu từ Xe Máy và Ô tô.
*   **Lịch Sử Ra Vào:** Tra cứu lịch sử xe ra vào chi tiết để đối soát.

---

## 🛠️ Công Nghệ Sử Dụng

*   **Ngôn ngữ:** Java (JDK 17 trở lên).
*   **Giao diện (GUI):** Java Swing (Sử dụng `CardLayout`, `GridBagLayout` tùy biến giao diện hiện đại, phẳng).
*   **Lưu trữ dữ liệu:** File CSV (Không cần cài đặt Database phức tạp).
*   **Kiến trúc:** MVC (Model-View-Controller) kết hợp DAO Pattern.
*   **Design Patterns:** Singleton, Strategy (cho tính phí), Factory.

---

## 📥 Hướng Dẫn Cài Đặt & Chạy

### Cách 1: Chạy từ Source Code (Khuyên dùng)
1.  **Clone dự án:**
    ```bash
    git clone https://github.com/YOUR_USERNAME/QuanLyBaiGuiXe.git
    ```
2.  **Mở bằng IntelliJ IDEA (hoặc Eclipse):**
    *   Chọn `File` -> `Open` -> Chọn thư mục dự án.
    *   Đợi Maven tải các thư viện (nếu có).
3.  **Chạy ứng dụng:**
    *   Tìm file `src/OOP/Parking/main/Main.java`.
    *   Nhấn nút **Run** (▶️).

### Cách 2: Chạy bằng file JAR (Nếu đã đóng gói)
1.  Tải file `QuanLyBaiGuiXe.jar` (nếu có trong phần Releases).
2.  Mở Terminal/CMD và chạy lệnh:
    ```bash
    java -jar QuanLyBaiGuiXe.jar
    ```
    *(Lưu ý: Đảm bảo thư mục `data` nằm cùng cấp với file JAR để chương trình đọc dữ liệu)*.

---

## 🔑 Tài Khoản Demo

Hệ thống đã tạo sẵn 2 tài khoản mẫu để bạn trải nghiệm:

| Vai Trò | Tên Đăng Nhập | Mật Khẩu | Quyền Hạn |
| :--- | :--- | :--- | :--- |
| **Quản Lý (Admin)** | `admin` | `123` | Full quyền (Báo cáo, Vé tháng...) |
| **Bảo Vệ (Staff)** | `baove` | `123` | Chỉ Check-in/out, Tra cứu |

---

## 📂 Cấu Trúc Thư Mục Dữ Liệu

Dữ liệu được lưu trong thư mục `data/` (tự động tạo khi chạy chương trình):
*   `users.csv`: Danh sách tài khoản.
*   `ve_thang.csv`: Danh sách vé tháng.
*   `phuong_tien.csv`: Xe đang trong bãi.
*   `lich_su.csv`: Lịch sử ra vào và doanh thu.

---

## 📸 Giao Diện (Screenshots)

*(Bạn có thể thêm ảnh chụp màn hình vào đây để file README sinh động hơn)*

---

## 🤝 Đóng Góp

Mọi ý kiến đóng góp đều được hoan nghênh! Hãy tạo **Issue** hoặc **Pull Request** nếu bạn muốn cải thiện dự án.

---
**Developed by [Your Name]**