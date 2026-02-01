package dao;

import model.Dispatch;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DispatchDAO {
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
            }
        }

}
