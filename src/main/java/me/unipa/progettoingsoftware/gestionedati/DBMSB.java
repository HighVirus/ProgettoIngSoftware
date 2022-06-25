package me.unipa.progettoingsoftware.gestionedati;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import javafx.application.Platform;
import javafx.stage.Stage;
import lombok.Getter;
import me.unipa.progettoingsoftware.gestionedati.entity.Farmaco;
import me.unipa.progettoingsoftware.gestionedati.entity.Order;
import me.unipa.progettoingsoftware.gestionedati.entity.User;
import me.unipa.progettoingsoftware.utils.RestoreConnectionC;

import java.sql.*;
import java.sql.Date;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
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
    private final ExecutorService executor;
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

    public void closePool() {
        if (hikariDataSource != null && !hikariDataSource.isClosed()) {
            hikariDataSource.close();
        }
        executor.shutdown();
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
                if (resultSet.next()) {
                    return User.createInstance(resultSet.getInt("ID"), resultSet.getInt("type"), resultSet.getString("email"), resultSet.getString("nome"), resultSet.getString("cognome"));
                } else return null;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }, executor);
    }

    public CompletableFuture<List<String>> getFarmaciaFromUserId(int id) {  //TODO: list(0) restituisce la piva, list(1) il nome della farmacia
        return null;
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
                 PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO catalogo_aziendale (codice_aic, nome_farmaco, principio_attivo, prescrivibile, costo) " +
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

    public void removeFarmacoFromCatalog(String codiceAic) {
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

    public CompletableFuture<List<Farmaco>> getFarmacoListFromStorage() {
        String STORAGE_TABLE = (this == DBMSB.getAzienda()) ? "magazzino_aziendale" : "farmaco";
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
        String STORAGE_TABLE = (this == DBMSB.getAzienda()) ? "magazzino_aziendale" : "farmaco";
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
                    boolean prescrivibilita = resultSet.getBoolean("prescrivibilita");
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

    public CompletableFuture<List<Farmaco>> getFarmacoListFromStorage(String codice_aic) {
        String STORAGE_TABLE = (this == DBMSB.getAzienda()) ? "magazzino_aziendale" : "farmaco";
        return CompletableFuture.supplyAsync(() -> {
            try (Connection connection = getConnection();
                 PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM " + STORAGE_TABLE + " WHERE codice_aic = ?")) {
                preparedStatement.setString(1, codice_aic);
                ResultSet resultSet = preparedStatement.executeQuery();
                List<Farmaco> farmacoList = new ArrayList<>();
                while (resultSet.next()) {
                    String codAic = resultSet.getString("codice_aic");
                    String lotto = resultSet.getString("lotto");
                    String farmacoName = resultSet.getString("nome_farmaco");
                    String principioAttivo = resultSet.getString("principio_attivo");
                    boolean prescrivibilita = resultSet.getBoolean("prescrivibilita");
                    Date expireDate = resultSet.getDate("data_scadenza");
                    int unita = resultSet.getInt("unita");
                    farmacoList.add(new Farmaco(codAic, lotto, farmacoName, principioAttivo, prescrivibilita, expireDate, unita));
                }
                return farmacoList;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }, executor);
    }

    public CompletableFuture<List<Farmaco>> getFarmacoListFromStorageToOrder(String codice_aic, int quantity) {
        return CompletableFuture.supplyAsync(() -> {
            try (Connection connection = getConnection();
                 PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM magazzino_aziendale WHERE codice_aic = ?")) {
                preparedStatement.setString(1, codice_aic);
                ResultSet resultSet = preparedStatement.executeQuery();
                List<Farmaco> farmacoList = new ArrayList<>();
                int total = 0;
                while (resultSet.next()) {
                    String codAic = resultSet.getString("codice_aic");
                    String lotto = resultSet.getString("lotto");
                    String farmacoName = resultSet.getString("nome_farmaco");
                    String principioAttivo = resultSet.getString("principio_attivo");
                    boolean prescrivibilita = resultSet.getBoolean("prescrivibilita");
                    Date expireDate = resultSet.getDate("data_scadenza");
                    int unita = resultSet.getInt("unita");
                    total += unita;
                    farmacoList.add(new Farmaco(codAic, lotto, farmacoName, principioAttivo, prescrivibilita, expireDate, unita));
                    if (total > quantity) break;
                }
                return farmacoList;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }, executor);
    }

    public CompletableFuture<Boolean> checkFarmacoAvailability(String codice_aic, int quantity) {
        String STORAGE_TABLE = (this == DBMSB.getAzienda()) ? "magazzino_aziendale" : "farmaco";
        if (STORAGE_TABLE.equals("magazzino_aziendale")) {
            return CompletableFuture.supplyAsync(() -> {
                try (Connection connection = getConnection();
                     PreparedStatement preparedStatement = connection.prepareStatement("select sum(unita) from " + STORAGE_TABLE + " where codice_aic=? group by codice_aic having sum(unita)>=?")) {
                    preparedStatement.setString(1, codice_aic);
                    preparedStatement.setInt(2, quantity);
                    ResultSet resultSet = preparedStatement.executeQuery();
                    return resultSet.next();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }, executor);
        } else {
            return CompletableFuture.supplyAsync(() -> {
                try (Connection connection = getConnection();
                     PreparedStatement preparedStatement = connection.prepareStatement("select sum(unita) from " + STORAGE_TABLE + " where codice_aic=? group by codice_aic having sum(unita)>=?")) {
                    preparedStatement.setString(1, codice_aic);
                    preparedStatement.setInt(2, quantity);
                    ResultSet resultSet = preparedStatement.executeQuery();
                    return resultSet.next();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }, executor);
        }


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

    public CompletableFuture<List<String>> getFarmaciaInfo(String piva) {
        return CompletableFuture.supplyAsync(() -> {
            try (Connection connection = getConnection();
                 PreparedStatement preparedStatement = connection.prepareStatement("SELECT far.nome_farmacia, acc.email, far.indirizzo, far.cap FROM farmacia far, account acc, farmaccount faracc WHERE far.partita_iva=faracc.partita_iva AND faracc.IDACCOUNT_F=acc.ID AND far.partita_iva=?")) {
                preparedStatement.setString(1, piva);
                ResultSet resultSet = preparedStatement.executeQuery();
                List<String> infoList = new ArrayList<>();
                if (resultSet.next()) {
                    infoList.add(resultSet.getString("nome_farmacia"));
                    infoList.add(resultSet.getString("email"));
                    infoList.add(resultSet.getString("indirizzo"));
                    infoList.add(resultSet.getString("cap"));
                }
                return infoList;

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }, executor);
    }

    public CompletableFuture<List<Order>> getOrderList() {
        return CompletableFuture.supplyAsync(() -> {
            try (Connection connection = getConnection();
                 PreparedStatement preparedStatement = connection.prepareStatement("SELECT ord.codice_ordine, ord.stato, ord.data_consegna, far.partita_iva, far.nome_farmacia, acc.email, far.indirizzo, far.cap, farmlistord.codice_aic_o, farmlistord.lotto_o, cat.nome_farmaco, farmlistord.unita FROM ordini ord, farmacia_ord farord, ord_far farmlistord, catalogo_aziendale cat, farmacia far, account acc, farmaccount faracc" +
                         " WHERE farord.codice_ordine_fo=ord.codice_ordine AND farord.partita_iva_fo=far.partita_iva AND ord.codice_ordine=farmlistord.codice_ordine_o AND cat.codice_aic=farmlistord.codice_aic_o AND far.partita_iva=faracc.partita_iva AND faracc.idaccount_f=acc.ID")) {
                Map<String, Order> orderMap = new HashMap<>();
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    String orderCode = resultSet.getString("codice_ordine");
                    String codAic = resultSet.getString("codice_aic_o");
                    String codLotto = resultSet.getString("lotto_o");
                    String farmacoName = resultSet.getString("nome_farmaco");
                    int unita = resultSet.getInt("unita");
                    if (!orderMap.containsKey(orderCode)) {
                        Date deliveryDate = resultSet.getDate("data_consegna");
                        String pivaFarmacia = resultSet.getString("partita_iva");
                        String farmaciaName = resultSet.getString("nome_farmacia");
                        String indirizzo = resultSet.getString("indirizzo");
                        String cap = resultSet.getString("cap");
                        String email = resultSet.getString("email");
                        int status = resultSet.getInt("stato");
                        Order order = new Order(orderCode, deliveryDate, pivaFarmacia, farmaciaName, indirizzo, cap, email, status);
                        order.getFarmacoList().add(new Farmaco(codAic, codLotto, farmacoName, unita));
                        orderMap.put(orderCode, order);
                    } else {
                        orderMap.get(orderCode).getFarmacoList().add(new Farmaco(codAic, codLotto, farmacoName, unita));
                    }
                }
                return new ArrayList<>(orderMap.values());
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }, executor);
    }

    public CompletableFuture<List<Order>> getOrderList(String farmaciaPiva) {  //TODO: DA SISTEMARE LA QUERY
        return CompletableFuture.supplyAsync(() -> {
            try (Connection connection = getConnection();
                 PreparedStatement preparedStatement = connection.prepareStatement("SELECT ord.codice_ordine, ord.stato, ord.data_consegna, far.partita_iva, far.nome_farmacia, acc.email, far.indirizzo, far.cap, farmlistord.codice_aic_o, farmlistord.lotto_o, cat.nome_farmaco, farmlistord.unita FROM ordini ord, farmacia_ord farord, ord_far farmlistord, catalogo_aziendale cat, farmacia far, account acc, farmaccount faracc" +
                         " WHERE farord.codice_ordine_fo=ord.codice_ordine AND farord.partita_iva_fo=far.partita_iva AND ord.codice_ordine=farmlistord.codice_ordine_o AND cat.codice_aic=farmlistord.codice_aic_o AND far.partita_iva=faracc.partita_iva AND faracc.idaccount_f=acc.ID")) {
                Map<String, Order> orderMap = new HashMap<>();
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    String orderCode = resultSet.getString("codice_ordine");
                    String codAic = resultSet.getString("codice_aic_o");
                    String codLotto = resultSet.getString("lotto_o");
                    String farmacoName = resultSet.getString("nome_farmaco");
                    int unita = resultSet.getInt("unita");
                    if (!orderMap.containsKey(orderCode)) {
                        Date deliveryDate = resultSet.getDate("data_consegna");
                        String pivaFarmacia = resultSet.getString("partita_iva");
                        String farmaciaName = resultSet.getString("nome_farmacia");
                        String indirizzo = resultSet.getString("indirizzo");
                        String cap = resultSet.getString("cap");
                        String email = resultSet.getString("email");
                        int status = resultSet.getInt("stato");
                        Order order = new Order(orderCode, deliveryDate, pivaFarmacia, farmaciaName, indirizzo, cap, email, status);
                        order.getFarmacoList().add(new Farmaco(codAic, codLotto, farmacoName, unita));
                        orderMap.put(orderCode, order);
                    } else {
                        orderMap.get(orderCode).getFarmacoList().add(new Farmaco(codAic, codLotto, farmacoName, unita));
                    }
                }
                return new ArrayList<>(orderMap.values());
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }, executor);
    }

    public CompletableFuture<List<Farmaco>> getFarmaciFromOrder(String orderCode) {
        return CompletableFuture.supplyAsync(() -> {
            try (Connection connection = getConnection();
                 PreparedStatement preparedStatement = connection.prepareStatement("SELECT ord.codice_ordine, ord.stato, ord.data_consegna, far.partita_iva, far.nome_farmacia, acc.email, far.indirizzo, far.cap, farmlistord.codice_aic_o, farmlistord.lotto_o, cat.nome_farmaco, farmlistord.unita FROM ordini ord, farmacia_ord farord, ord_far farmlistord, catalogo_aziendale cat, farmacia far, account acc, farmaccount faracc" +
                         " WHERE farord.codice_ordine_fo=ord.codice_ordine AND farord.partita_iva_fo=far.partita_iva AND ord.codice_ordine=farmlistord.codice_ordine_o AND cat.codice_aic=farmlistord.codice_aic_o AND far.partita_iva=faracc.partita_iva AND faracc.idaccount_f=acc.ID AND ord.codice_ordine = ?")) {
                List<Farmaco> farmacoList = new ArrayList<>();
                preparedStatement.setString(1, orderCode);
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    String codAic = resultSet.getString("codice_aic_o");
                    String codLotto = resultSet.getString("lotto_o");
                    String farmacoName = resultSet.getString("nome_farmaco");
                    int unita = resultSet.getInt("unita");
                    farmacoList.add(new Farmaco(codAic, codLotto, farmacoName, unita));
                }
                return farmacoList;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }, executor);
    }

    public void addFarmacoToStorage(Farmaco farmaco) {
        CompletableFuture.runAsync(() -> {
            try (Connection connection = getConnection();
                 PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO magazzino_aziendale (?,?,?,?,?,?,?,?)")) {
                preparedStatement.setString(1, farmaco.getCodAic());
                preparedStatement.setString(2, farmaco.getLotto());
                preparedStatement.setString(3, farmaco.getFarmacoName());
                preparedStatement.setString(4, farmaco.getPrincipioAttivo());
                preparedStatement.setBoolean(5, farmaco.isPrescrivibile());
                preparedStatement.setDate(6, farmaco.getScadenza());
                preparedStatement.setDouble(7, farmaco.getCosto());
                preparedStatement.setInt(8, farmaco.getUnita());
                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }, executor);
    }

    public void addFarmacoListToStorage(List<Farmaco> farmacoList) {
        CompletableFuture.runAsync(() -> {
            try (Connection connection = getConnection();
                 PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO magazzino_aziendale (?,?,?,?,?,?,?,?)")) {
                preparedStatement.setString(1, farmaco.getCodAic());
                preparedStatement.setString(2, farmaco.getLotto());
                preparedStatement.setString(3, farmaco.getFarmacoName());
                preparedStatement.setString(4, farmaco.getPrincipioAttivo());
                preparedStatement.setBoolean(5, farmaco.isPrescrivibile());
                preparedStatement.setDate(6, farmaco.getScadenza());
                preparedStatement.setDouble(7, farmaco.getCosto());
                preparedStatement.setInt(8, farmaco.getUnita());
                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }, executor);
    }

    public void removeFarmaciExpired() {
        String STORAGE_TABLE = (this == DBMSB.getAzienda()) ? "magazzino_aziendale" : "magazzino_farmacia";
        CompletableFuture.runAsync(() -> {
            try (Connection connection = getConnection();
                 PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM " + STORAGE_TABLE + " WHERE data_scadenza<?")) {
                preparedStatement.setDate(1, new Date(System.currentTimeMillis()));
                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }, executor);
    }

    public CompletableFuture<Boolean> thereIsAlertsNotRead() {
        return CompletableFuture.supplyAsync(() -> {
            try (Connection connection = getConnection();
                 PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM alerts WHERE letto=false")) {
                ResultSet resultSet = preparedStatement.executeQuery();
                return resultSet.next();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }, executor);
    }

    public void createNewOrder(Order order) {
        CompletableFuture.runAsync(() -> {
            try (Connection connection = getConnection();
                 PreparedStatement ordineStatement = connection.prepareStatement("INSERT INTO ordini (codice_ordine, data_consegna, stato) VALUES (?,?,?)");
                 PreparedStatement ordFarStatement = connection.prepareStatement("INSERT INTO ord_far (codice_ordine_o, codice_aic_o, lotto_o, unita) VALUES (?,?,?,?)");
                 PreparedStatement farmaciaOrdStatement = connection.prepareStatement("INSERT INTO farmacia_ord (codice_ordine_fo, partita_iva_fo) VALUES (?,?)");
                 PreparedStatement magazzinoStatement = connection.prepareStatement("UPDATE magazzino_aziendale SET unita=? WHERE codice_aic=? AND lotto=?")) {
                ordineStatement.setString(1, order.getOrderCode());
                ordineStatement.setDate(2, order.getDeliveryDate());
                ordineStatement.setInt(3, 1);
                ordineStatement.executeUpdate();

                farmaciaOrdStatement.setString(1, order.getOrderCode());
                farmaciaOrdStatement.setString(2, order.getPivaFarmacia());
                farmaciaOrdStatement.executeUpdate();

                for (Farmaco farmacoOrder : order.getFarmacoList()) {
                    List<Farmaco> farmacos = this.getFarmacoListFromStorageToOrder(farmacoOrder.getCodAic(), farmacoOrder.getUnita()).join();
                    int unitaToOrder = farmacoOrder.getUnita();
                    for (Farmaco f : farmacos) {

                        if (f.getUnita() > unitaToOrder) {
                            ordFarStatement.setString(1, order.getOrderCode());
                            ordFarStatement.setString(2, farmacoOrder.getCodAic());
                            ordFarStatement.setString(3, f.getLotto());
                            ordFarStatement.setInt(4, farmacoOrder.getUnita());
                            ordFarStatement.executeUpdate();

                            magazzinoStatement.setInt(1, f.getUnita() - farmacoOrder.getUnita());
                            magazzinoStatement.setString(2, farmacoOrder.getCodAic());
                            magazzinoStatement.setString(3, f.getLotto());
                            magazzinoStatement.executeUpdate();
                            break;
                        } else {
                            unitaToOrder -= f.getUnita();
                            ordFarStatement.setString(1, order.getOrderCode());
                            ordFarStatement.setString(2, farmacoOrder.getCodAic());
                            ordFarStatement.setString(3, f.getLotto());
                            ordFarStatement.setInt(4, f.getUnita());
                            ordFarStatement.executeUpdate();

                            magazzinoStatement.setInt(1, 0);
                            magazzinoStatement.setString(2, farmacoOrder.getCodAic());
                            magazzinoStatement.setString(3, f.getLotto());
                            magazzinoStatement.executeUpdate();
                        }

                    }
                }

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }, executor);
    }

    public CompletableFuture<Date> getFarmacoAvailabilityDate(String codAIC, int quantity) {
        return CompletableFuture.supplyAsync(() -> {
            try (Connection connection = getConnection();
                 PreparedStatement preparedStatement = connection.prepareStatement("SELECT pf.start_production_date, pf.production_period, pf.unita_production FROM produzione_farmaco pf WHERE codice_aic_p = ?")) {
                preparedStatement.setString(1, codAIC);
                ResultSet resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) {
                    Date startDate = resultSet.getDate("start_production_date");
                    int period = resultSet.getInt("production_period");
                    int unita = resultSet.getInt("unita_production");
                    int tempAmount = 0;
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(startDate);
                    Calendar now = Calendar.getInstance();
                    now.setTime(new Date(System.currentTimeMillis()));
                    while (tempAmount < quantity) {
                        if (calendar.before(now)) {
                            calendar.add(Calendar.DAY_OF_YEAR, period);
                        } else {
                            calendar.add(Calendar.DAY_OF_YEAR, period);
                            tempAmount += unita;
                        }
                    }
                    return new Date(calendar.getTimeInMillis());
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return null;
        }, executor);
    }

    public void confirmSell(List<Farmaco> farmaciList) {
        CompletableFuture.runAsync(() -> {
            try (Connection connection = getConnection();
                 PreparedStatement updateStatement = connection.prepareStatement("UDPATE farmaco SET unita = ? WHERE codice_aic = ? AND lotto = ?");
                 PreparedStatement removeStatement = connection.prepareStatement("DELETE FROM farmaco WHERE codice_aic = ? AND lotto = ?")) {
                for (Farmaco farmacoSold : farmaciList) {
                    Farmaco farmacoStor = this.getFarmacoFromStorage(farmacoSold.getCodAic(), farmacoSold.getLotto()).join();
                    if ((farmacoStor.getUnita() - farmacoSold.getUnita()) <= 0) {
                        removeStatement.setInt(1, farmacoStor.getUnita() - farmacoSold.getUnita());
                        removeStatement.setString(2, farmacoSold.getCodAic());
                        removeStatement.setString(3, farmacoSold.getLotto());
                        removeStatement.executeUpdate();
                    } else {
                        updateStatement.setString(1, farmacoSold.getCodAic());
                        updateStatement.setString(2, farmacoSold.getLotto());
                        updateStatement.executeUpdate();
                    }
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }, executor);
    }

    public void makeDeliveryCompleted(String orderCode, int status) {

    }

    public void makeOrderDeliveredReadyToLoad() {

    }

    public CompletableFuture<List<Farmaco>> getFarmaciAlreadyOrdered() {
        return CompletableFuture.supplyAsync(() -> {
            return null;
        }
    }

    public void removeFarmacoFromAlreadyOrdered(String codAic) {

    }

    public CompletableFuture<Boolean> isFarmacoOrdered() {
        return null;
    }

    public CompletableFuture<List<Order>> getDeliveryList() {
        return null;
    }

    public CompletableFuture<List<Order>> getDeliveryInfo() {
        return null;
    }

    public CompletableFuture<List<Order>> getDeliveryDate() {
        return null;
    }

    public CompletableFuture<String> getAlertsAzienda() {
        return null;
    }

    public CompletableFuture<String> getAlertList() {
        return null;
    }

    public void sendAlert() {

    }

    public void addFarmaciToOrdered() {

    }

    public CompletableFuture<List<Farmaco>> getFarmaciBanco() {
        return null;
    }

    public void addFarmaciBancoToStorage() {

    }

    public void deleteOrder() {

    }

    public CompletableFuture<List<Order>> getOrderReadyToLoadList() {
        return null;
    }

    public void updateOrder() {

    }

    public CompletableFuture<Date> getFarmacoExpireDate() {
        return null;
    }

    public CompletableFuture<int> getFarmacoUnitaPeriodic() {
        return null;
    }

    public void updateUnitaPeriodicOrder() {

    }
}
