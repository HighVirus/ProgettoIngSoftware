package me.unipa.progettoingsoftware.externalcomponents;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import javafx.application.Platform;
import javafx.stage.Stage;
import lombok.Getter;
import me.unipa.progettoingsoftware.utils.entity.Farmaco;
import me.unipa.progettoingsoftware.utils.entity.Order;
import me.unipa.progettoingsoftware.utils.entity.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
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
                 PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM account WHERE EMAIL=? AND PASSWORD=?")) {
                preparedStatement.setString(1, email);
                preparedStatement.setString(2, password);
                ResultSet resultSet = preparedStatement.executeQuery();
                if (resultSet.next())
                    return User.createInstance(resultSet.getInt("ID"), resultSet.getInt("type"), resultSet.getString("email"), resultSet.getString("nome"), resultSet.getString("cognome"));
                else return null;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }, executor);
    }

    public CompletableFuture<List<Farmaco>> getFarmaciCatalogList() {
        return CompletableFuture.supplyAsync(() -> {
            try (Connection connection = getConnection();
                 PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM catalogo_aziendale")) {
                List<Farmaco> farmacoList = new ArrayList<>();
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    String codAic = resultSet.getString("codice_aic");
                    String farmacoName = resultSet.getString("nome_farmaco");
                    String principioAttivo = resultSet.getString("principio_attivo");
                    double costo = resultSet.getDouble("costo");
                    farmacoList.add(new Farmaco(codAic, farmacoName, principioAttivo, costo));
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
                 PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM catalogo_aziendale WHERE codice_aic = ?")) {
                preparedStatement.setString(1, codice_aic);
                ResultSet resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) {
                    String codAic = resultSet.getString("codice_aic");
                    String farmacoName = resultSet.getString("nome_farmaco");
                    String principioAttivo = resultSet.getString("principio_attivo");
                    boolean prescrivibile = resultSet.getBoolean("prescrivibilita");
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
                 PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO catalogo_aziendale (codice_aic, nome_farmaco, principio_attivo, prescrivibilita, costo) " +
                         "VALUES (?,?,?,?,?)")) {
                preparedStatement.setString(1, codiceAic);
                preparedStatement.setString(2, nomeFarmaco);
                preparedStatement.setString(3, principioAttivo);
                preparedStatement.setBoolean(4, prescrivibile);
                preparedStatement.setDouble(5, costo);
                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }, executor);
    }

    public void removeFarmacoToCatalog(String codiceAic) {
        CompletableFuture.runAsync(() -> {
            try (Connection connection = getConnection();
                 PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM catalogo_aziendale WHERE codice_aic = ?")) {
                preparedStatement.setString(1, codiceAic);
                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }, executor);
    }

    public CompletableFuture<List<Farmaco>> getFarmaciListFromStorage() {
        String STORAGE_TABLE = (this == DBMSB.getAzienda()) ? "magazzino_aziendale" : "magazzino_farmacia";
        return CompletableFuture.supplyAsync(() -> {
            try (Connection connection = getConnection();
                 PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM " + STORAGE_TABLE)) {
                List<Farmaco> farmacoList = new ArrayList<>();
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    String codAic = resultSet.getString("codice_aic");
                    String lotto = resultSet.getString("lotto");
                    String farmacoName = resultSet.getString("nome_farmaco");
                    String principioAttivo = resultSet.getString("principio_attivo");
                    boolean prescrivibilita = resultSet.getBoolean("prescrivibilita");
                    Date expireDate = resultSet.getDate("data_scadenza");
                    double costo = resultSet.getDouble("costo");
                    int unita = resultSet.getInt("unita");
                    farmacoList.add(new Farmaco(codAic, lotto, farmacoName, principioAttivo, prescrivibilita, expireDate, costo, unita));
                }
                return farmacoList;

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }, executor);
    }

    public CompletableFuture<Farmaco> getFarmacoFromStorage(String codice_aic, String lotto) {
        String STORAGE_TABLE = (this == DBMSB.getAzienda()) ? "magazzino_aziendale" : "magazzino_farmacia";
        return CompletableFuture.supplyAsync(() -> {
            try (Connection connection = getConnection();
                 PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM " + STORAGE_TABLE + " WHERE codice_aic = ? AND lotto = ?")) {
                preparedStatement.setString(1, codice_aic);
                preparedStatement.setString(2, lotto);
                ResultSet resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) {
                    String codAic = resultSet.getString("codice_aic");
                    String farmacoName = resultSet.getString("nome_farmaco");
                    String principioAttivo = resultSet.getString("principio_attivo");
                    boolean prescrivibilita = resultSet.getBoolean("prescrivilita");
                    Date expireDate = resultSet.getDate("data_scadenza");
                    double costo = resultSet.getDouble("costo");
                    int unita = resultSet.getInt("unita");
                    return new Farmaco(codAic, lotto, farmacoName, principioAttivo, prescrivibilita, expireDate, costo, unita);
                }
                return null;

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }, executor);
    }

    public void removeFarmacoFromStorage(String codiceAic, String lotto) {
        String STORAGE_TABLE = (this == DBMSB.getAzienda()) ? "magazzino_aziendale" : "magazzino_farmacia";
        CompletableFuture.runAsync(() -> {
            try (Connection connection = getConnection();
                 PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM " + STORAGE_TABLE + " WHERE codice_aic = ? AND lotto=?")) {
                preparedStatement.setString(1, codiceAic);
                preparedStatement.setString(2, lotto);
                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }, executor);
    }

    public CompletableFuture<List<Order>> getOrderList() {
        return CompletableFuture.supplyAsync(() -> {
            try (Connection connection = getConnection();
                 PreparedStatement preparedStatement = connection.prepareStatement("SELECT ord.codice_ordine, ord.data_consegna, far.partita_iva, far.nome_farmacia, acc.email, far.indirizzo, far.cap, farmlistord.codice_aic_o, cat.nome_farmaco, farmlistord.unita FROM ordini ord, farmacia_ord farord, ord_far farmlistord, catalogo_aziendale cat, farmacia far, account acc, farmaccount faracc" +
                         " WHERE farord.codice_ordine_fo=ord.codice_ordine AND farord.partita_iva_fo=far.partita_iva AND ord.codice_ordine=farmlistord.codice_ordine_o AND cat.codice_aic=farmlistord.codice_aic_o AND far.partita_iva=faracc.partita_iva AND faracc.IDACCOUNT_F=acc.ID")) {
                Map<String, Order> orderMap = new HashMap<>();
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    String orderCode = resultSet.getString("codice_ordine");
                    String codAic = resultSet.getString("codice_aic_o");
                    String farmacoName = resultSet.getString("nome_farmaco");
                    int unita = resultSet.getInt("unita");
                    if (!orderMap.containsKey(orderCode)){
                        Date deliveryDate = resultSet.getDate("data_consegna");
                        String pivaFarmacia = resultSet.getString("partita_iva");
                        String farmaciaName = resultSet.getString("nome_farmacia");
                        String indirizzo = resultSet.getString("indirizzo");
                        String cap = resultSet.getString("cap");
                        String email = resultSet.getString("email");
                        Order order = new Order(orderCode, deliveryDate, pivaFarmacia, farmaciaName, indirizzo, cap, email);
                        order.getFarmacoList().add(new Farmaco(codAic, farmacoName, unita));
                        orderMap.put(orderCode, order);
                    } else {
                        orderMap.get(orderCode).getFarmacoList().add(new Farmaco(codAic, farmacoName, unita));
                    }
                }
                return new ArrayList<>(orderMap.values());
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }, executor);
    }
}
