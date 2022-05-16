package com.solvd.taxi.dao.jdbcMySQLImpl;

import com.solvd.taxi.dao.IOrdersDAO;
import com.solvd.taxi.models.CustomersModel;
import com.solvd.taxi.models.DriversModel;
import com.solvd.taxi.models.OrdersModel;
import com.solvd.taxi.utilites.ConnectionDB;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OrdersDAO implements IOrdersDAO {

    private static final Logger LOGGER = LogManager.getLogger(OrdersDAO.class);

    final String DELETE = "DELETE FROM Orders WHERE id=?";
    final String GET = "SELECT * FROM Orders ORDER BY id";
    final String INSERT = "INSERT INTO Orders VALUES (?, ?, ?, ?, ?)";
    final String UPDATE = "UPDATE Orders SET total=? WHERE id=?";
    final String RIGHTJOIN = "SELECT Orders.id, Drivers.f_name, Drivers.date_of_start FROM Orders RIGHT JOIN Drivers ON Orders.driver_id = Drivers.id  ORDER BY Orders.id";


    PreparedStatement stmt = null;
    ResultSet rs = null;

    @Override
    public void createOrders(OrdersModel ordersModel) {
        Connection dbConnect = ConnectionDB.getConnection();
        try {
            stmt = dbConnect.prepareStatement(INSERT);
            stmt.setInt(1, ordersModel.getId());
            stmt.setString(2, ordersModel.getTime());
            stmt.setDouble(3, ordersModel.getTotal());
            stmt.setString(4, ordersModel.getFromAddress());
            stmt.setString(5, ordersModel.getToAddress());
            int i = stmt.executeUpdate();
            LOGGER.info(i + " records inserted");
        } catch (Exception e) {
            LOGGER.info(e);
        }
        finally {
            ConnectionDB.close(stmt);
            ConnectionDB.close(dbConnect);
            ConnectionDB.close(rs);
        }
    }

    @Override
    public void updateOrdersById(OrdersModel ordersModel) {
        Connection dbConnect = ConnectionDB.getConnection();
        try {
            stmt = dbConnect.prepareStatement(UPDATE);
            stmt.setDouble(1, ordersModel.getTotal());
            stmt.setInt(2, ordersModel.getId());
            int i = stmt.executeUpdate();
        } catch (Exception e) {
            LOGGER.info(e);
        }
        finally {
            ConnectionDB.close(stmt);
            ConnectionDB.close(dbConnect);
            ConnectionDB.close(rs);
        }
    }

    @Override
    public void deleteOrdersById(OrdersModel ordersModel) {
        int x = 0;

        Connection dbConnect = ConnectionDB.getConnection();
        try {
            stmt = dbConnect.prepareStatement(DELETE);
            stmt.setInt(1, ordersModel.getId());
            int i = stmt.executeUpdate();
            LOGGER.info(i + " records deleted");
        } catch (SQLException e) {
            LOGGER.error("ERROR DELETE Orders WITH ID " + e.getMessage());
            x = 1;
        } finally {
            ConnectionDB.close(stmt);
            ConnectionDB.close(dbConnect);
            ConnectionDB.close(rs);
        }
    }

    @Override
    public OrdersModel getOrders() {
        List<OrdersModel> allOrders = new ArrayList<>();
        Connection dbConnect = ConnectionDB.getConnection();
        try {
            stmt = dbConnect.prepareStatement(GET);
            rs = stmt.executeQuery();
            while (rs.next()) {
                LOGGER.info("\nId: " + rs.getInt(1)
                        + "\nTimes: " + rs.getString(2)
                        + "\nCustomer id: " + rs.getString(3)
                        + "\nCar id: " + rs.getString(4)
                        + "\nDriver id: " + rs.getString(5)
                        + "\nOrder from id: " + rs.getString(6)
                        + "\nOrder to id: " + rs.getString(7)
                        + "\nTotal price order: " + rs.getString(8)
                        + "\nOperator id: " + rs.getString(9));
            }
            LOGGER.info("ALL is OK!");
        } catch (Exception e) {
            LOGGER.info(e);
        }
        finally {
            ConnectionDB.close(stmt);
            ConnectionDB.close(dbConnect);
            ConnectionDB.close(rs);
        }
        return null;
    }

    public List<OrdersModel> getOrdersRightJoinDrivers() {
        ArrayList<OrdersModel> allOrders = new ArrayList<>();
        Connection dbConnect = ConnectionDB.getConnection();
        try {
            stmt = dbConnect.prepareStatement(RIGHTJOIN);
            rs = stmt.executeQuery();
            while (rs.next()) {
                OrdersModel ordersModel = new OrdersModel();
                ordersModel.setId(rs.getInt("id"));
                ordersModel.setFirstName(rs.getString("f_name"));
                ordersModel.setDayOfStart(rs.getString("date_of_start"));
                allOrders.add(ordersModel);
            }
            LOGGER.info("ALL is OK!");
            return allOrders;
        } catch (Exception e) {
            LOGGER.info(e);
        }
        finally {
            ConnectionDB.close(stmt);
            ConnectionDB.close(dbConnect);
            ConnectionDB.close(rs);
        }
        return null;
    }
}
