package me.unipa.progettoingsoftware.externalcomponents;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.Getter;
import me.unipa.progettoingsoftware.autenticazione.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class DBMSB {
    @Getter(lazy = true)
    private static final DBMSB azienda = new DBMSB("azienda", 30000, "azienda", "azienda");

    @Getter(lazy = true)
    private static final DBMSB farmacia = new DBMSB("farmacia", 30001, "farmacia", "farmacia");

    private final String host = "129.152.17.152";
    private final String database;
    private final String username;
    private final String password;
    private final int port;
    private final boolean ssl = false;
    private HikariDataSource hikariDataSource;
    private final HikariConfig hikariConfig = new HikariConfig();
    private final Executor executor;
    private Connection connection;

    private DBMSB(String database, int port, String username, String password) {
        this.database = database;
        this.port = port;
        this.username = username;
        this.password = password;
        hikariConfig.setConnectionTimeout(5000);
        this.executor = Executors.newFixedThreadPool(4);
        this.setup();
    }

    private Connection getJdbcUrl() throws SQLException {
        hikariConfig.setJdbcUrl("jdbc:mysql://" +
                this.host +
                ":" +
                this.port +
                "/" +
                this.database +
                "?autoReconnect=true&maxReconnects=12&useSSL=" +
                this.ssl);
        hikariConfig.setUsername(this.username);
        hikariConfig.setPassword(this.password);
        hikariDataSource = new HikariDataSource(hikariConfig);
        return hikariDataSource.getConnection();
    }

    public Connection getConnection() throws SQLException {
        if (hikariDataSource == null) {
            throw new SQLException("Hikari is null");
        }
        Connection connection = hikariDataSource.getConnection();
        int attempts = 1;
        while (connection == null && attempts < 13) {
            System.out.println("Tentativo di riconnessione numero: " + attempts);
            attempts++;
            connection = hikariDataSource.getConnection();
        }
        if (connection == null)
            throw new SQLException("Connection is null");
        return connection;
    }

    private void setup() {
        try {
            if (this.connection != null && !this.connection.isClosed()) {
                return;
            }
            connection = this.getJdbcUrl();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public CompletableFuture<User> getUser(String email, String password) {
        return CompletableFuture.supplyAsync(() -> {
            try (Connection connection = getConnection();
                 PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM ACCOUNT WHERE EMAIL=? AND PASSWORD=?")) {
                preparedStatement.setString(1, email);
                preparedStatement.setString(2, password);
                ResultSet resultSet = preparedStatement.executeQuery();
                if (resultSet.next())
                    return User.createInstance(resultSet.getInt("TYPE"), resultSet.getString("EMAIL"), resultSet.getString("PASSWORD"));
                else return null;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }, executor);
    }
}
