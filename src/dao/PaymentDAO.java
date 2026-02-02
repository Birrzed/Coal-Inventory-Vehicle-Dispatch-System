package dao;

import model.Payment;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PaymentDAO {

    public void savePayment(Payment payment) {
        String sql = "INSERT INTO payment (dispatch_id, amount, payment_date, status) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, payment.getDispatchId());
            stmt.setDouble(2, payment.getAmount());
            stmt.setDate(3, payment.getPaymentDate());
            stmt.setString(4, payment.getStatus());

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Payment> getAllPayments() {
        List<Payment> list = new ArrayList<>();
        String sql = "SELECT p.*, u.username as transporter_username FROM payment p " +
                "JOIN dispatch d ON p.dispatch_id = d.id " +
                "JOIN users u ON d.transporter_id = u.id";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                list.add(new Payment(
                        rs.getInt("id"),
                        rs.getInt("dispatch_id"),
                        rs.getDouble("amount"),
                        rs.getDate("payment_date"),
                        rs.getString("status"),
                        rs.getString("transporter_username")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<Payment> getPaymentsForTransporter(int transporterId) {
        List<Payment> list = new ArrayList<>();
        String sql = "SELECT p.* FROM payment p JOIN dispatch d ON p.dispatch_id = d.id WHERE d.transporter_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, transporterId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                list.add(new Payment(
                        rs.getInt("id"),
                        rs.getInt("dispatch_id"),
                        rs.getDouble("amount"),
                        rs.getDate("payment_date"),
                        rs.getString("status")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public void updatePaymentStatus(int paymentId, String status) {
        String sql = "UPDATE payment SET status = ? WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, status);
            stmt.setInt(2, paymentId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean checkPaymentExists(int dispatchId) {
        String sql = "SELECT count(*) FROM payment WHERE dispatch_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, dispatchId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
