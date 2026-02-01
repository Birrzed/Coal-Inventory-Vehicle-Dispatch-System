package dao;

import model.Dispatch;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DispatchDAO {


        public void createDispatch(Dispatch dispatch) {
            String sql = "INSERT INTO dispatch (product_mass, dispatch_date, seller_id, transporter_id, destination_id, status) VALUES (?, ?, ?, ?, ?, ?)";
            try (Connection conn = DBConnection.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {

                stmt.setDouble(1, dispatch.getProductMass());
                stmt.setDate(2, dispatch.getDispatchDate());
                stmt.setInt(3, dispatch.getSellerId());
                stmt.setInt(4, dispatch.getTransporterId());
                stmt.setInt(5, dispatch.getDestinationId());
                stmt.setString(6, "Pending");

                stmt.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }}

            public void updateConfirmDate(int dispatchId, Date date) {
                String sql = "UPDATE dispatch SET confirm_date = ?, status = 'In Transit' WHERE id = ?";
                try (Connection conn = DBConnection.getConnection();
                     PreparedStatement stmt = conn.prepareStatement(sql)) {

                    stmt.setDate(1, date);
                    stmt.setInt(2, dispatchId);
                    stmt.executeUpdate();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            public void updateArrivalDate(int dispatchId, Date date) {
                String sql = "UPDATE dispatch SET arrival_date = ?, status = 'Delivered' WHERE id = ?";
                try (Connection conn = DBConnection.getConnection();
                     PreparedStatement stmt = conn.prepareStatement(sql)) {

                    stmt.setDate(1, date);
                    stmt.setInt(2, dispatchId);
                    stmt.executeUpdate();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            public void updateStatus(int dispatchId, String status) {
                String sql = "UPDATE dispatch SET status = ? WHERE id = ?";
                try (Connection conn = DBConnection.getConnection();
                     PreparedStatement stmt = conn.prepareStatement(sql)) {
                    stmt.setString(1, status);
                    stmt.setInt(2, dispatchId);
                    stmt.executeUpdate();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            public List<Dispatch> getDispatchesByRole(String role, int userId) {
                List<Dispatch> list = new ArrayList<>();
                String sql = "";

                switch (role) {
                    case "Seller":
                        sql = "SELECT * FROM dispatch WHERE seller_id = ?";
                        break;
                    case "Transporter":
                        sql = "SELECT * FROM dispatch WHERE transporter_id = ?";
                        break;
                    case "Destination":
                        sql = "SELECT * FROM dispatch WHERE destination_id = ?";
                        break;
                    case "Admin":
                        sql = "SELECT * FROM dispatch";
                        break;
                }

                try (Connection conn = DBConnection.getConnection();
                     PreparedStatement stmt = conn.prepareStatement(sql)) {

                    if (!role.equals("Admin")) {
                        stmt.setInt(1, userId);
                    }

                    ResultSet rs = stmt.executeQuery();
                    while (rs.next()) {
                        list.add(new Dispatch(
                                rs.getInt("id"),
                                rs.getDouble("product_mass"),
                                rs.getDate("dispatch_date"),
                                rs.getDate("confirm_date"),
                                rs.getDate("arrival_date"),
                                rs.getInt("seller_id"),
                                rs.getInt("transporter_id"),
                                rs.getInt("destination_id"),
                                rs.getString("status")));
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                return list;
            }

            public Dispatch getDispatchById(int id) {
                String sql = "SELECT * FROM dispatch WHERE id = ?";
                try (Connection conn = DBConnection.getConnection();
                     PreparedStatement stmt = conn.prepareStatement(sql)) {
                    stmt.setInt(1, id);
                    ResultSet rs = stmt.executeQuery();
                    if (rs.next()) {
                        return new Dispatch(
                                rs.getInt("id"),
                                rs.getDouble("product_mass"),
                                rs.getDate("dispatch_date"),
                                rs.getDate("confirm_date"),
                                rs.getDate("arrival_date"),
                                rs.getInt("seller_id"),
                                rs.getInt("transporter_id"),
                                rs.getInt("destination_id"),
                                rs.getString("status"));
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                return null;

        }


}
