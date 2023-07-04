package net.battle.core.sql;

import java.math.BigDecimal;
import java.sql.Array;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;

public class ConnectionSQL {

    protected Connection conn;

    public void openConnection(String ip, String password) throws SQLException, ClassNotFoundException {
        Class.forName("com.mysql.jdbc.Driver");
        conn = DriverManager.getConnection("jdbc:mysql://" + ip + "/battlemania?autoReconnect=true&useUnicode=true",
                    "root", password);
    }

    public void closeConnection() {
        try {
            if (conn == null || !isConnected()) {
                return;
            }
            conn.close();
            conn = null;
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean isConnected() {
        try {
            return conn != null && !conn.isClosed();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private PreparedStatement convertArgumentsToStatement(String sql, Object[] variables, int setting) {
        try {
            PreparedStatement statement = conn.prepareStatement(sql, setting);
            for (int idx = 0; idx < variables.length; idx++) {
                Object currentArg = variables[idx];
                int sqlIdx = idx + 1;

                if (currentArg instanceof Integer argBoolean) {
                    statement.setInt(sqlIdx, argBoolean);
                    continue;
                }
                if (currentArg instanceof Long argBoolean) {
                    statement.setLong(sqlIdx, argBoolean);
                    continue;
                }
                if (currentArg instanceof Array argBoolean) {
                    statement.setArray(sqlIdx, argBoolean);
                    continue;
                }
                if (currentArg instanceof String argBoolean) {
                    statement.setString(sqlIdx, argBoolean);
                    continue;
                }
                if (currentArg instanceof BigDecimal argBoolean) {
                    statement.setBigDecimal(sqlIdx, argBoolean);
                    continue;
                }
                if (currentArg instanceof Float argBoolean) {
                    statement.setFloat(sqlIdx, argBoolean);
                    continue;
                }
                if (currentArg instanceof Boolean argBoolean) {
                    statement.setBoolean(sqlIdx, argBoolean);
                    continue;
                }
                if (currentArg instanceof Date argDate) {
                    // Weird issue caused by java.sql.Date
                    // https://stackoverflow.com/questions/2400955/how-to-store-java-date-to-mysql-datetime-with-jpa
                    statement.setTimestamp(sqlIdx, new Timestamp(argDate.getTime()));
                    continue;
                }

                throw new SQLException("Invalid argument type in prepared statement: " + currentArg.getClass());
            }

            return statement;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public synchronized void execute(String sql, Object... variables) {
        PreparedStatement statement = convertArgumentsToStatement(sql, variables, Statement.SUCCESS_NO_INFO);
        try {
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public synchronized ResultSet executeAndFetchGeneratedKeys(String sql, Object... variables) {
        PreparedStatement statement = convertArgumentsToStatement(sql, variables, Statement.RETURN_GENERATED_KEYS);
        try {
            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Query affected 0 rows, but function requires generated keys to be returned");
            }
            return statement.getGeneratedKeys();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public synchronized ResultSet queryAndFetch(String sql, Object... variables) {
        PreparedStatement statement = convertArgumentsToStatement(sql, variables, Statement.SUCCESS_NO_INFO);
        try {
            return statement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}