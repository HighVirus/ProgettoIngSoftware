
CREATE DATABASE IF NOT EXISTS farmacia;
use farmacia;

--- Creazione della tabella Farmaco---

        CREATE TABLE farmaco (
        codice_aic int(9) primary key,
        lotto varchar(6) unique,
        nome_farmaco varchar(255) not null,
        principio_attivo varchar(100),
        prescrivibilità varchar(2) not null,
        data_scadenza date,
        costo float default 0.00,
        unita int(255) not null default 0
        );

INSERT INTO farmaco VALUES
(012745182, 'abe789', 'tachipirina 1000 mg compresse 16 compresse', 'paracetamolo', 'no', '2027-07-00', 4.54, 250),
(012745232, 'abe775', 'tachipirina 10 mg/ml soluzione per infusione" 1 sacca da 50 ml', 'paracetamolo', 'no', '2025-06-00', 12.50, 15),
(042386488, 'rfq416', 'brufen 400 mg compresse rivestite con film 16 compresse in blister opa/al/pvc/al/vmch', 'ibuprofene', 'no', '2022-07-00', 4.75, 25),
(034246013, 'trf741', 'nurofen 200 mg + 30 mg compresse rivestite 12 compresse rivestite', 'ibuprofene', 'no', '2024-12-00', 6.00, 150),
(027860016, 'frt654', 'zitromax 250 mg capsule rigide 6 capsule', 'azitromicina', 'si', '2023-05-00', 8.50, 21),
(024840074, 'bgt541', 'cardioaspirin 100 mg compresse gastroresistenti 30 compresse', 'acido acetilsalicilico', 'si', '2022-09-00', 2.35, 40),
(019655051, 'bfh845', '1 mg compresse effervescenti 10 compresse', 'betametasone', 'no', '2022-07-00', 1.35, 19)
;


-- Visualizza tutti i farmaci presenti all'interno del magazzino

select *
from farmaco
;

-- seleziona tutti i farmaci con un numero di unità inferiore a 20
select *
from farmaco
where unita <20
;



