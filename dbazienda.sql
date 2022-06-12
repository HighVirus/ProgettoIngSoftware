
--- DBMS AZIENDA ---

CREATE DATABASE IF NOT EXISTS azienda;
USE azienda;

create TABLE if not exists ACCOUNT (
    ID INTEGER PRIMARY KEY AUTO_INCREMENT,
    TYPE INT,
    NAME VARCHAR(100),
    SURNAME VARCHAR(100),
    EMAIL VARCHAR(200),
    PASSWORD VARCHAR(200)
);

INSERT INTO ACCOUNT (TYPE, NAME, SURNAME, EMAIL, PASSWORD) VALUES (1,'Alessandro', 'Spank', 'ciao', 'ciao');

create table farmacista(
    partita_iva bigint primary key,
    IDACCOUNT INTEGER ,
    nome_farmacia varchar(255),
    cap int,
    indirizzo varchar(255),
    numero_civico int 
);

create table catalogo_farmaci(
        codice_aic bigint primary key,
        lotto varchar(6) unique,
        nome_farmaco varchar(255) not null,
        principio_attivo varchar(100),
        prescrivibilità varchar(2) not null,
        data_scadenza date not null,
        costo float default 0.00,
        unita int not null 
        );
      
create table ordini(
	codice_ordine int primary key,
    data_consegna date not null,
    email_o varchar(255) not null,
    codice_aic_o bigint,
    foreign key(email_o) references dipendenti(email)
);

INSERT INTO dipendenti VALUES
('alessandro', 'bianchi', 'alessandrobianchi@gmail.com', 'agr784'),
('marco', 'rossi', 'marcorossi@gmail.com','ciaociao96'),
('giuseppe', 'antonini','giuseppeantonini@gmail.com')
;

INSERT INTO farmacista VALUES
(94165746623, 'la mia farmacia', 90010, 'via Ernesto Basile', 154),
(98774123459, 'farmacia pennino', 02077, 'viale Europa', 50),       
(54127441659, 'farmacia mineo', 50127, 'via Giuseppe Garibaldi', 10)
;

INSERT INTO farmaci VALUES
(012745182, 'abe789', 'tachipirina 1000 mg compresse 16 compresse', 'paracetamolo', 'no', '2027-07-00', 4.54, 4503),
(012745232, 'abe775', 'tachipirina 10 mg/ml soluzione per infusione" 1 sacca da 50 ml', 'paracetamolo', 'no', '2025-06-00', 12.50, 10),
(042386488, 'rfq416', 'brufen 400 mg compresse rivestite con film 16 compresse in blister opa/al/pvc/al/vmch', 'ibuprofene', 'no', '2022-07-00', 4.75, 89),
(034246013, 'trf741', 'nurofen 200 mg + 30 mg compresse rivestite 12 compresse rivestite', 'ibuprofene', 'no', '2024-12-00', 6.00, 241),
(027860016, 'frt654', 'zitromax 250 mg capsule rigide 6 capsule', 'azitromicina', 'si', '2023-05-00', 8.50, 700),
(024840074, 'bgt541', 'cardioaspirin 100 mg compresse gastroresistenti 30 compresse', 'acido acetilsalicilico', 'si', '2022-09-00', 2.35, 450),
(019655051, 'bfh845', '1 mg compresse effervescenti 10 compresse', 'betametasone', 'no', '2022-07-00', 1.35, 1500)
;


INSERT INTO ordini VALUES
(14578, '2022-07-01', 'marcorossi@gmail.com');



