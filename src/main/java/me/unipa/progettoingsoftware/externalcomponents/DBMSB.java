package me.unipa.progettoingsoftware.externalcomponents;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import javafx.application.Platform;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.SneakyThrows;
import me.unipa.progettoingsoftware.utils.entity.Farmaco;
import me.unipa.progettoingsoftware.utils.entity.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicReference;

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
        this.executor = Executors.newFixedThreadPool(2);
        this.setup();
    }

    private Connection getJdbcUrl() throws SQLException {
        hikariConfig.setJdbcUrl("jdbc:mysql://" +
                this.host +
                ":" +
                this.port +
                "/" +
                this.database +
                "?allowPublicKeyRetrieval=true&useSSL=" +
                this.ssl);
        hikariConfig.setUsername(this.username);
        hikariConfig.setPassword(this.password);

        hikariDataSource = new HikariDataSource(hikariConfig);
        return hikariDataSource.getConnection();
    }

    public synchronized Connection getConnection() throws SQLException {
        if (hikariDataSource == null) {
            throw new SQLException("Hikari is null");
        }
        Connection connection;
        try {
            connection = this.hikariDataSource.getConnection();
            return connection;
        } catch (SQLTransientConnectionException e) {
            AtomicReference<Stage> stage = new AtomicReference<>();
            Platform.runLater(() -> {
                stage.set(new Stage());
                new RestoreConnectionC(stage.get());
            });
            while (true) {
                try {
                    Connection connection1 = this.hikariDataSource.getConnection();
                    if (connection1 != null)
                        Platform.runLater(() -> stage.get().close());
                    return connection1;
                } catch (Exception ignored) {

                }
            }

        }

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
                    return User.createInstance(resultSet.getInt("TYPE"), resultSet.getString("EMAIL"), resultSet.getString("NAME"), resultSet.getString("SURNAME"));
                else return null;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }, executor);
    }

    public CompletableFuture<List<Farmaco>> getFarmaciCatalogList() {
        return CompletableFuture.supplyAsync(() -> {
            try (Connection connection = getConnection();
                 PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM catalogo_farmaci")) {
                List<Farmaco> farmacoList = new ArrayList<>();
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    String codAic = resultSet.getString("codice_aic");
                    String farmacoName = resultSet.getString("nome_farmaco");
                    String principioAttivo = resultSet.getString("principio_attivo");
                    boolean prescrivibile = resultSet.getBoolean("prescrivibile");
                    double costo = resultSet.getDouble("costo");
                    farmacoList.add(new Farmaco(codAic, farmacoName, principioAttivo, prescrivibile, costo));
                }
                return farmacoList;

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }, executor);
    }

    public CompletableFuture<Farmaco> getFarmacoFromCatalog(String codice_aic) {
        return CompletableFuture.supplyAsync(() -> {
            try (Connection connection = getConnection();
                 PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM catalogo_farmaci WHERE codice_aic = ?")) {
                preparedStatement.setString(1, codice_aic);
                ResultSet resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) {
                    String codAic = resultSet.getString("codice_aic");
                    String farmacoName = resultSet.getString("nome_farmaco");
                    String principioAttivo = resultSet.getString("principio_attivo");
                    boolean prescrivibile = resultSet.getBoolean("prescrivibile");
                    double costo = resultSet.getDouble("costo");
                    return new Farmaco(codAic, farmacoName, principioAttivo, prescrivibile, costo);
                }
                return null;

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }, executor);
    }

    public void addFarmacoToCatalog(String codiceAic, String nomeFarmaco, String principioAttivo, boolean prescrivibile, double costo) {
        CompletableFuture.runAsync(() -> {
            try (Connection connection = getConnection();
                 PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO catalogo_farmaci (codice_aic, nome_farmaco, principio_attivo, prescrivibile, costo, unita, lotto, data_scadenza) " +
                         "VALUES (?,?,?,?,?,?,?,?)")) {
                preparedStatement.setInt(1, Integer.parseInt(codiceAic));
                preparedStatement.setString(2, nomeFarmaco);
                preparedStatement.setString(3, principioAttivo);
                preparedStatement.setBoolean(4, prescrivibile);
                preparedStatement.setDouble(5, costo);
                preparedStatement.setInt(6, 10);
                preparedStatement.setString(7, "231232");
                preparedStatement.setDate(8, new Date(System.currentTimeMillis()));
                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }, executor);
    }

    public void removeFarmacoToCatalog(String codiceAic) {
        CompletableFuture.runAsync(() -> {
            try (Connection connection = getConnection();
                 PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM catalogo_farmaci WHERE codice_aic = ?")) {
                preparedStatement.setString(1, codiceAic);
                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }, executor);
    }
}
