package me.unipa.progettoingsoftware.gestionedati;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import javafx.application.Platform;
import javafx.stage.Stage;
import lombok.Getter;
import me.unipa.progettoingsoftware.gestioneareafarmaceutica.gestioneordini.PeriodicOrder;
import me.unipa.progettoingsoftware.gestionedati.entity.AlertE;
import me.unipa.progettoingsoftware.gestionedati.entity.Farmaco;
import me.unipa.progettoingsoftware.gestionedati.entity.Order;
import me.unipa.progettoingsoftware.gestionedati.entity.User;
import me.unipa.progettoingsoftware.utils.AlertType;
import me.unipa.progettoingsoftware.utils.RestoreConnectionC;

import java.sql.Date;
import java.sql.*;
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
                    if (resultSet.getInt("type") == 2) {
                        String piva = this.getFarmaciaFromUserId(resultSet.getInt("ID")).join().get(0);
                        return User.createInstance(resultSet.getInt("ID"), resultSet.getInt("type"), resultSet.getString("email"), resultSet.getString("nome"), resultSet.getString("cognome"), piva);
                    }
                    return User.createInstance(resultSet.getInt("ID"), resultSet.getInt("type"), resultSet.getString("email"), resultSet.getString("nome"), resultSet.getString("cognome"));
                } else return null;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }, executor);
    }

    /**
     * Metodo per ottenere le informazioni di base di una farmacia(piva e nome)
     *
     * @param userId
     * @return list(0)=piva, list(1)=nome farmacia
     */

    public CompletableFuture<List<String>> getFarmaciaFromUserId(int userId) {  //TODO: list(0) restituisce la piva, list(1) il nome della farmacia
        return CompletableFuture.supplyAsync(() -> {
            try (Connection connection = getConnection();
                 PreparedStatement preparedStatement = connection.prepareStatement("SELECT f.partita_iva, f.nome_farmacia FROM farmacia f,account a, farmaccount fa WHERE a.ID=fa.IDACCOUNT_F AND fa.partita_iva=f.partita_iva AND a.ID=?")) {
                preparedStatement.setInt(1, userId);
                List<String> farmaciaInfo = new ArrayList<>();
                ResultSet resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) {
                    String piva = resultSet.getString("partita_iva");
                    String farmaciaName = resultSet.getString("nome_farmacia");
                    farmaciaInfo.add(piva);
                    farmaciaInfo.add(farmaciaName);
                }
                return farmaciaInfo;

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
        return CompletableFuture.supplyAsync(() -> {
            try (Connection connection = getConnection();
                 PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM magazzino_aziendale")) {

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

    public CompletableFuture<List<Farmaco>> getFarmacoListFromStorage(String piva) {
        String STORAGE_TABLE = (this == DBMSB.getAzienda()) ? "magazzino_aziendale" : "farmaco";
        return CompletableFuture.supplyAsync(() -> {
            try (Connection connection = getConnection();
                 PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM " + STORAGE_TABLE + " WHERE piva=?")) {
                preparedStatement.setString(1, piva);
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

    public CompletableFuture<Farmaco> getFarmacoFromStorage(String piva, String codice_aic, String lotto) {
        String STORAGE_TABLE = (this == DBMSB.getAzienda()) ? "magazzino_aziendale" : "farmaco";
        return CompletableFuture.supplyAsync(() -> {
            try (Connection connection = getConnection();
                 PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM " + STORAGE_TABLE + " WHERE codice_aic = ? AND lotto = ? AND piva=?")) {
                preparedStatement.setString(1, codice_aic);
                preparedStatement.setString(2, lotto);
                preparedStatement.setString(3, piva);
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

    public CompletableFuture<List<Farmaco>> getFarmacoListFromStorage(String piva, String codice_aic) {
        String STORAGE_TABLE = (this == DBMSB.getAzienda()) ? "magazzino_aziendale" : "farmaco";
        return CompletableFuture.supplyAsync(() -> {
            try (Connection connection = getConnection();
                 PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM " + STORAGE_TABLE + " WHERE codice_aic = ? AND piva=?")) {
                preparedStatement.setString(1, codice_aic);
                preparedStatement.setString(2, piva);
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
                     PreparedStatement storageStatement = connection.prepareStatement("SELECT sum(unita) FROM " + STORAGE_TABLE + " WHERE codice_aic=? GROUP BY codice_aic");
                     PreparedStatement ordiniStatement = connection.prepareStatement("SELECT sum(unita) FROM ord_far WHERE codice_aic_o=? GROUP BY codice_aic_o")) {
                    storageStatement.setString(1, codice_aic);
                    ordiniStatement.setString(1, codice_aic);
                    ResultSet storageResult = storageStatement.executeQuery();
                    ResultSet ordiniResult = ordiniStatement.executeQuery();
                    return (storageResult.getInt("sum(unita)") - ordiniResult.getInt("sum(unita)")) > quantity;
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
        String STORAGE_TABLE = (this == DBMSB.getAzienda()) ? "magazzino_aziendale" : "farmaco";
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

    /**
     * Solo azienda
     *
     * @return
     */
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

    public CompletableFuture<List<Order>> getOrderList(String farmaciaPiva) {
        String ORDER_TABLE = (this == DBMSB.getAzienda()) ? "ordini" : "ordine";
        if (ORDER_TABLE.equals("ordini")) {
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
        } else {
            return CompletableFuture.supplyAsync(() -> {
                try (Connection connection = getConnection();
                     PreparedStatement preparedStatement = connection.prepareStatement("SELECT ord.codice_ordine, ord.stato, ord.data_consegna, ordfar.codice_aic_of, ordfar.lotto_of, ordfar.unita, mag.nome_farmaco, mag.principio_attivo, mag.prescrivibilita, ordfar.data_consegna, ordfar.unita FROM ordine ord, ord_farmaci ordfar, farmaco mag" +
                             " WHERE ord.codice_ordine=ordfar.codice_ordine_of AND ordfar.codice_aic_of=mag.codice_aic AND ordfar.lotto_of=mag.lotto AND ord.piva = ?")) {
                    preparedStatement.setString(1, farmaciaPiva);
                    Map<String, Order> orderMap = new HashMap<>();
                    ResultSet resultSet = preparedStatement.executeQuery();
                    while (resultSet.next()) {
                        String orderCode = resultSet.getString("codice_ordine");
                        String codAic = resultSet.getString("codice_aic_of");
                        String codLotto = resultSet.getString("lotto_of");
                        String farmacoName = resultSet.getString("nome_farmaco");
                        String princioAttivo = resultSet.getString("principio_attivo");
                        boolean prescrivibile = resultSet.getBoolean("prescrivibilita");
                        Date expireDate = resultSet.getDate("data_consegna");
                        int unita = resultSet.getInt("unita");
                        if (!orderMap.containsKey(orderCode)) {
                            Date deliveryDate = resultSet.getDate("data_consegna");
                            String pivaFarmacia = farmaciaPiva;
                            int status = resultSet.getInt("stato");
                            Order order = new Order(orderCode, deliveryDate, pivaFarmacia, null, null, null, null, status);
                            order.getFarmacoList().add(new Farmaco(orderCode, codAic, codLotto, farmacoName, princioAttivo, prescrivibile, expireDate, 0, unita));
                            orderMap.put(orderCode, order);
                        } else {
                            orderMap.get(orderCode).getFarmacoList().add(new Farmaco(orderCode, codAic, codLotto, farmacoName, princioAttivo, prescrivibile, expireDate, 0, unita));
                        }
                    }
                    return new ArrayList<>(orderMap.values());
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }, executor);
        }
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

    public void addFarmacoListToStorage(String piva, List<Farmaco> farmacoList) {
        CompletableFuture.runAsync(() -> {
            try (Connection connection = getConnection();
                 PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO magazzino_aziendale (?,?,?,?,?,?,?,?,?)")) {
                for (Farmaco farmaco : farmacoList) {
                    preparedStatement.setString(1, piva);
                    preparedStatement.setString(2, farmaco.getCodAic());
                    preparedStatement.setString(3, farmaco.getLotto());
                    preparedStatement.setString(4, farmaco.getFarmacoName());
                    preparedStatement.setString(5, farmaco.getPrincipioAttivo());
                    preparedStatement.setBoolean(6, farmaco.isPrescrivibile());
                    preparedStatement.setDate(7, farmaco.getScadenza());
                    preparedStatement.setDouble(8, farmaco.getCosto());
                    preparedStatement.setInt(9, farmaco.getUnita());
                }
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

    public CompletableFuture<Boolean> thereIsAlertsNotRead(String farmaciaPiva) {
        return CompletableFuture.supplyAsync(() -> {
            try (Connection connection = getConnection();
                 PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM alerts WHERE letto=false AND piva=?")) {
                preparedStatement.setString(1, farmaciaPiva);
                ResultSet resultSet = preparedStatement.executeQuery();
                return resultSet.next();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }, executor);
    }

    public void createNewOrder(Order order) {
        if (this == DBMSB.getFarmacia()) {
            CompletableFuture.runAsync(() -> {
                try (Connection connection = getConnection();
                     PreparedStatement ordineStatement = connection.prepareStatement("INSERT INTO ordine (codice_ordine, piva, data_consegna, stato) VALUES (?,?,?,?)");
                     PreparedStatement ordFarStatement = connection.prepareStatement("INSERT INTO ord_farmaci (codice_ordine_of, codice_aic_of, lotto_of, unita) VALUES (?,?,?,?)")) {
                    ordineStatement.setString(1, order.getOrderCode());
                    ordineStatement.setString(2, order.getPivaFarmacia());
                    ordineStatement.setDate(3, order.getDeliveryDate());
                    ordineStatement.setInt(4, 1);
                    ordineStatement.executeUpdate();

                    for (Farmaco farmacoOrder : order.getFarmacoList()) {
                        ordFarStatement.setString(1, order.getOrderCode());
                        ordFarStatement.setString(2, farmacoOrder.getCodAic());
                        ordFarStatement.setString(3, farmacoOrder.getLotto());
                        ordFarStatement.setInt(4, farmacoOrder.getUnita());
                        ordFarStatement.executeUpdate();
                    }

                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }, executor);
        } else {
            CompletableFuture.runAsync(() -> {
                try (Connection connection = getConnection();
                     PreparedStatement ordineStatement = connection.prepareStatement("INSERT INTO ordini (codice_ordine, data_consegna, stato) VALUES (?,?,?)");
                     PreparedStatement ordFarStatement = connection.prepareStatement("INSERT INTO ord_far (codice_ordine_o, codice_aic_o, lotto_o, unita) VALUES (?,?,?,?)");
                     PreparedStatement farmaciaOrdStatement = connection.prepareStatement("INSERT INTO farmacia_ord (codice_ordine_fo, partita_iva_fo) VALUES (?,?)");) {
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
                                break;
                            } else {
                                unitaToOrder -= f.getUnita();
                                ordFarStatement.setString(1, order.getOrderCode());
                                ordFarStatement.setString(2, farmacoOrder.getCodAic());
                                ordFarStatement.setString(3, f.getLotto());
                                ordFarStatement.setInt(4, f.getUnita());
                                ordFarStatement.executeUpdate();
                            }

                        }
                    }

                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }, executor);
        }

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

    public void confirmSell(String piva, List<Farmaco> farmaciList) {
        CompletableFuture.runAsync(() -> {
            try (Connection connection = getConnection();
                 PreparedStatement updateStatement = connection.prepareStatement("UDPATE farmaco SET unita = ? WHERE codice_aic = ? AND lotto = ?");
                 PreparedStatement removeStatement = connection.prepareStatement("DELETE FROM farmaco WHERE codice_aic = ? AND lotto = ?")) {
                for (Farmaco farmacoSold : farmaciList) {
                    Farmaco farmacoStor = this.getFarmacoFromStorage(piva, farmacoSold.getCodAic(), farmacoSold.getLotto()).join();
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

    public void makeDeliveryCompleted(String orderCode) {
        String orderTable = (this == DBMSB.getAzienda()) ? "ordini" : "ordine";
        CompletableFuture.runAsync(() -> {
            try (Connection connection = getConnection();
                 PreparedStatement updateStatement = connection.prepareStatement("UDPATE " + orderTable + " SET stato = ? WHERE codice_ordine = ?")) {
                updateStatement.setInt(1, 3);
                updateStatement.setString(2, orderCode);
                updateStatement.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }, executor);
    }

    public void makeOrderDeliveredReadyToLoad(String orderCode) {
        String orderTable = (this == DBMSB.getAzienda()) ? "ordini" : "ordine";
        CompletableFuture.runAsync(() -> {
            try (Connection connection = getConnection();
                 PreparedStatement updateStatement = connection.prepareStatement("UDPATE " + orderTable + " SET stato = ? WHERE codice_ordine = ?")) {
                updateStatement.setInt(1, 2);
                updateStatement.setString(2, orderCode);
                updateStatement.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }, executor);
    }

    public CompletableFuture<List<String>> getFarmaciAlreadyOrdered(String piva) {
        return CompletableFuture.supplyAsync(() -> {
            try (Connection connection = getConnection();
                 PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM farmaci_ordinati WHERE piva=?")) {
                preparedStatement.setString(1, piva);
                List<String> codAicList = new ArrayList<>();
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    String codAic = resultSet.getString("codice_aic");
                    codAicList.add(codAic);
                }
                return codAicList;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }, executor);
    }

    public void removeFarmacoFromAlreadyOrdered(String piva, String codAic) {
        CompletableFuture.runAsync(() -> {
            try (Connection connection = getConnection();
                 PreparedStatement updateStatement = connection.prepareStatement("DELETE FROM farmaci_ordinati WHERE piva=? AND codice_ordine = ?")) {
                updateStatement.setString(1, piva);
                updateStatement.setString(2, codAic);
                updateStatement.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }, executor);
    }

    public CompletableFuture<Boolean> isFarmacoOrdered() {
        return null;
    }

    public CompletableFuture<List<Order>> getDeliveryList() {
        return CompletableFuture.supplyAsync(() -> {
            try (Connection connection = getConnection();
                 PreparedStatement preparedStatement = connection.prepareStatement("SELECT ord.codice_ordine, ord.stato, ord.data_consegna, far.partita_iva, far.nome_farmacia, acc.email, far.indirizzo, far.cap, farmlistord.codice_aic_o, farmlistord.lotto_o, cat.nome_farmaco, farmlistord.unita FROM ordini ord, farmacia_ord farord, ord_far farmlistord, catalogo_aziendale cat, farmacia far, account acc, farmaccount faracc" +
                         " WHERE farord.codice_ordine_fo=ord.codice_ordine AND farord.partita_iva_fo=far.partita_iva AND ord.codice_ordine=farmlistord.codice_ordine_o AND cat.codice_aic=farmlistord.codice_aic_o AND far.partita_iva=faracc.partita_iva AND faracc.idaccount_f=acc.ID AND ord.stato=1")) {
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


    public CompletableFuture<List<Order>> getDeliveryInfo() {
        return CompletableFuture.supplyAsync(() -> {
            try (Connection connection = getConnection();
                 PreparedStatement preparedStatement = connection.prepareStatement("SELECT DISTINCT ordini.codice_ordine, farmacia.partita_iva, farmacia.nome_farmacia, farmacia.cap, farmacia.indirizzo, account.email, magazzino_aziendale.nome_farmaco, ord_far.codice_aic_o, ord_far.lotto_o, ord_far.unita, ordini.stato FROM ordini, ord_far, magazzino_aziendale, farmacia_ord, farmacia, account, farmaccount WHERE ordini.codice_ordine = ord_far.codice_ordine_o AND magazzino_aziendale.codice_aic = ord_far.codice_aic_o AND farmacia_ord.codice_ordine_fo = ordini.codice_ordine and farmacia_ord.partita_iva_fo = farmacia.partita_iva and farmaccount.partita_iva = farmacia.partita_iva and farmaccount.IDACCOUNT_F = account.ID")) {
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

    public CompletableFuture<List<AlertE>> getAlertsAzienda() {
        return CompletableFuture.supplyAsync(() -> {
            try (Connection connection = getConnection();
                 PreparedStatement preparedStatement = connection.prepareStatement("SELECT codice_alert, partita_iva_aa FROM alert_azienda")) {
                List<AlertE> alertEList = new ArrayList<>();
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    String farmaciaName = this.getFarmaciaInfo(resultSet.getString("partita_iva_aa")).join().get(0);
                    alertEList.add(new AlertE(resultSet.getString("codice_alert"), AlertType.AZIENDA, resultSet.getString("partita_iva_aa"), farmaciaName));
                }
                return alertEList;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }, executor);
    }

    public CompletableFuture<List<AlertE>> getAlertList(String piva) {
        return CompletableFuture.supplyAsync(() -> {
            try (Connection connection = getConnection();
                 PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM alert_farmacia WHERE piva=?")) {
                preparedStatement.setString(1, piva);
                Map<String, Integer> alertMap = new HashMap<>();
                List<AlertE> alertEList = new ArrayList<>();
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    alertMap.put(resultSet.getString("codice_alert"), resultSet.getInt("type"));
                }
                for (Map.Entry<String, Integer> map : alertMap.entrySet()) {
                    if (map.getValue() == AlertType.FARMACIA_QUANTITY.getType()) {
                        List<Farmaco> farmacoList = this.getAlertListFarm(map.getKey(), piva).join();
                        alertEList.add(new AlertE(map.getKey(), AlertType.FARMACIA_QUANTITY, farmacoList));
                    } else if (map.getValue() == AlertType.FARMACIA_CARICO.getType()) {
                        List<Order> orderList = this.getAlertListOrd(map.getKey(), piva).join();
                        alertEList.add(new AlertE(map.getKey(), AlertType.FARMACIA_CARICO, orderList, "questa stringa non serve a niente"));
                    }
                }
                for (AlertE alertE : alertEList)
                    System.out.println(alertE.getMessage());
                return alertEList;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }, executor);
    }

    public CompletableFuture<List<Farmaco>> getAlertListFarm(String codAlert, String piva) {
        return CompletableFuture.supplyAsync(() -> {
            try (Connection connection = getConnection();
                 PreparedStatement preparedStatement = connection.prepareStatement("SELECT alefam.codice_aic_af, alefam.lotto_af, farmaco.nome_farmaco FROM alefam, farmaco, alert_farmacia WHERE alefam.codice_alert_af=? AND alefam.codice_aic_af=farmaco.codice_aic AND alert_farmacia.codice_alert=alefam.codice_alert_af AND alert_farmacia.piva=?")) {
                List<Farmaco> farmacoList = new ArrayList<>();
                preparedStatement.setString(1, codAlert);
                preparedStatement.setString(2, piva);
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    farmacoList.add(new Farmaco(resultSet.getString("codice_aic_af"), resultSet.getString("lotto_af"), resultSet.getString("nome_farmaco")));
                }
                return farmacoList;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }, executor);
    }

    public CompletableFuture<List<Order>> getAlertListOrd(String codAlert, String piva) {
        /*return CompletableFuture.supplyAsync(() -> {
            try (Connection connection = getConnection();
                 PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM aleord, alert_farmacia WHERE aleord.codice_alert_ao=? AND alert_farmacia.codice_alert=aleord.codice_alert_ao AND alert_farmacia.piva=?")) {
                List<Order> orderList = new ArrayList<>();
                preparedStatement.setString(1, codAlert);
                preparedStatement.setString(2, piva);
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    orderList.addAll(this.getOrderList(piva).join());
                }
                return orderList;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }, executor);*/
        return CompletableFuture.supplyAsync(() -> {
            try (Connection connection = getConnection();
                 PreparedStatement preparedStatement = connection.prepareStatement("SELECT aleord.codice_ordine_ao, ord.stato, ord.data_consegna, ordfar.codice_aic_of, ordfar.lotto_of, ordfar.unita, mag.nome_farmaco, mag.principio_attivo, mag.prescrivibilita, ordfar.data_consegna, ordfar.unita FROM ordine ord, aleord, alert_farmacia, ord_farmaci ordfar, farmaco mag" +
                         " WHERE ord.codice_ordine=ordfar.codice_ordine_of AND ordfar.codice_aic_of=mag.codice_aic AND ordfar.lotto_of=mag.lotto AND alert_farmacia.codice_alert=aleord.codice_alert_ao AND aleord.codice_alert_ao=? AND alert_farmacia.piva=?")) {
                preparedStatement.setString(1, codAlert);
                preparedStatement.setString(1, piva);
                Map<String, Order> orderMap = new HashMap<>();
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    String orderCode = resultSet.getString("codice_ordine_ao");
                    String codAic = resultSet.getString("codice_aic_of");
                    String codLotto = resultSet.getString("lotto_of");
                    String farmacoName = resultSet.getString("nome_farmaco");
                    String princioAttivo = resultSet.getString("principio_attivo");
                    boolean prescrivibile = resultSet.getBoolean("prescrivibilita");
                    Date expireDate = resultSet.getDate("data_consegna");
                    int unita = resultSet.getInt("unita");
                    if (!orderMap.containsKey(orderCode)) {
                        Date deliveryDate = resultSet.getDate("data_consegna");
                        int status = resultSet.getInt("stato");
                        Order order = new Order(orderCode, deliveryDate, piva, null, null, null, null, status);
                        order.getFarmacoList().add(new Farmaco(orderCode, codAic, codLotto, farmacoName, princioAttivo, prescrivibile, expireDate, 0, unita));
                        orderMap.put(orderCode, order);
                    } else {
                        orderMap.get(orderCode).getFarmacoList().add(new Farmaco(orderCode, codAic, codLotto, farmacoName, princioAttivo, prescrivibile, expireDate, 0, unita));
                    }
                }
                return new ArrayList<>(orderMap.values());
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    /**
     * usato per inviare l'alert all'azienda
     *
     * @param farmaciaPiva
     */
    public void sendAlert(String farmaciaPiva) {
        CompletableFuture.runAsync(() -> {
            try (Connection connection = getConnection();
                 PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO alert_azienda (partita_iva_aa) VALUES (?)")) {
                preparedStatement.setString(1, farmaciaPiva);
                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }, executor);
    }

    public CompletableFuture<List<Farmaco>> getFarmaciBanco(String piva) { //TODO da finire
        return CompletableFuture.supplyAsync(() -> {
            try (Connection connection = getConnection();
                 PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM farmaco WHERE prescrivibilita = false")) {
                //da completare.....

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            return null;
        }, executor);
    }

    public void addFarmaciBancoToStorage(String codiceAic, String lotto, String nomeFarmaco, String principioAttivo, boolean prescrivibilita, Date data_scadenza, double costo, int unita) {
        CompletableFuture.runAsync(() -> {
            try (Connection connection = getConnection();
                 PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO farmaco (codice_aic, lotto, nome_farmaco, principio_attivo, prescrivibilita, data_scadenza, costo, unita) " +
                         "VALUES (?,?,?,?,?,?,?,?)")) {
                preparedStatement.setString(1, codiceAic);
                preparedStatement.setString(2, lotto);
                preparedStatement.setString(3, nomeFarmaco);
                preparedStatement.setString(4, principioAttivo);
                preparedStatement.setBoolean(5, prescrivibilita);
                preparedStatement.setDate(6, data_scadenza);
                preparedStatement.setDouble(7, costo);
                preparedStatement.setInt(8, unita);
                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }, executor);
    }

    public void deleteOrder(String orderCode) {
        CompletableFuture.runAsync(() -> {
            try (Connection connection = getConnection();
                 PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM farmacia_ord WHERE or codice_ordine_fo = ?")) {
                preparedStatement.setString(1, orderCode);
                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }, executor);
    }


    public CompletableFuture<List<Order>> getOrderReadyToLoadList(String farmaciaPiva) {
        return CompletableFuture.supplyAsync(() -> {
            try (Connection connection = getConnection();
                 PreparedStatement preparedStatement = connection.prepareStatement("SELECT ord.codice_ordine, ord.stato, ord.data_consegna, ordfar.codice_aic_of, ordfar.lotto_of, ordfar.unita, mag.nome_farmaco FROM ordine ord, ord_farmaci ordfar, farmaco mag" +
                         " WHERE ord.codice_ordine=ordfar.codice_ordine_of AND ordfar.codice_aic_of=mag.codice_aic AND ordfar.lotto_of=mag.lotto AND ord.piva = ? AND ord.stato=2")) {
                preparedStatement.setString(1, farmaciaPiva);
                Map<String, Order> orderMap = new HashMap<>();
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    String orderCode = resultSet.getString("codice_ordine");
                    String codAic = resultSet.getString("codice_aic_of");
                    String codLotto = resultSet.getString("lotto_of");
                    String farmacoName = resultSet.getString("nome_farmaco");
                    int unita = resultSet.getInt("unita");
                    if (!orderMap.containsKey(orderCode)) {
                        Date deliveryDate = resultSet.getDate("data_consegna");
                        String pivaFarmacia = farmaciaPiva;
                        int status = resultSet.getInt("stato");
                        Order order = new Order(orderCode, deliveryDate, pivaFarmacia, null, null, null, null, status);
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

    public void updateOrder(String orderCode) {

    }

    public CompletableFuture<Date> getFarmacoExpireDate(String codAic) {
        return null;
    }

    /**
     * metodo che ottiene le informazioni sugli ordini periodici di una certa farmacia
     *
     * @param piva piva della farmacia
     * @return
     */
    public CompletableFuture<List<PeriodicOrder>> getFarmacoUnitaPeriodic(String piva) {
        return CompletableFuture.supplyAsync(() -> {
            try (Connection connection = getConnection();
                 PreparedStatement preparedStatement = connection.prepareStatement("SELECT op.piva, op.codice_aic, ca.nome_farmaco, ca.principio_attivo, op.unita, op.periodo_consegna FROM ordine_periodico op, catalogo_aziendale ca WHERE piva=? AND op.codice_aic_pm=ca.codice_aic")) {
                List<PeriodicOrder> periodicOrders = new ArrayList<>();
                preparedStatement.setString(1, piva);
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    periodicOrders.add(new PeriodicOrder(resultSet.getString("piva"),
                            resultSet.getString("codice_aic"),
                            resultSet.getString("nome_farmaco"),
                            resultSet.getString("principio_attivo"),
                            resultSet.getInt("unita"),
                            resultSet.getString("periodo_consegna")));
                }
                return periodicOrders;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }, executor);
    }

    public void updateUnitaPeriodicOrder(PeriodicOrder periodicOrder) {
        CompletableFuture.runAsync(() -> {
            try (Connection connection = getConnection();
                 PreparedStatement preparedStatement = connection.prepareStatement("UPDATE ordine_periodico SET unita=? WHERE piva=? AND codAic=?")) {
                preparedStatement.setInt(1, periodicOrder.getUnita());
                preparedStatement.setString(2, periodicOrder.getPiva());
                preparedStatement.setString(3, periodicOrder.getCodAic());
                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }, executor);
    }

    public CompletableFuture<List<String>> getFarmaciePivaList() {
        return CompletableFuture.supplyAsync(() -> {
            try (Connection connection = getConnection();
                 PreparedStatement preparedStatement = connection.prepareStatement("SELECT partita_iva FROM farmacia")) {
                List<String> pivaList = new ArrayList<>();
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    pivaList.add(resultSet.getString("partita_iva"));
                }
                return pivaList;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }, executor);
    }

    public CompletableFuture<List<Farmaco>> getFarmacoListCheckStorage() {
        return null;
    }

    public void addFarmaciToOrdered(String piva, List<Farmaco> codAicList) {
        CompletableFuture.runAsync(() -> {
            try (Connection connection = getConnection();
                 PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO farmaci_ordinati (piva, codice_aic) " +
                         "VALUES (?,?)")) {
                preparedStatement.setString(1, piva);
                for (Farmaco f : codAicList) {
                    preparedStatement.setString(2, f.getCodAic());
                    preparedStatement.executeUpdate();
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }, executor);
    }
}
