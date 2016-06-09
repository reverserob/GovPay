CREATE SEQUENCE seq_psp start 1 increment 1 maxvalue 9223372036854775807 minvalue 1 cache 1 NO CYCLE;

CREATE TABLE psp
(
	cod_psp VARCHAR(35) NOT NULL,
	ragione_sociale VARCHAR(70) NOT NULL,
	url_info VARCHAR(255),
	abilitato BOOLEAN NOT NULL,
	storno BOOLEAN NOT NULL,
	marca_bollo BOOLEAN NOT NULL,
	-- fk/pk columns
	id BIGINT DEFAULT nextval('seq_psp') NOT NULL,
	-- unique constraints
	CONSTRAINT unique_psp_1 UNIQUE (cod_psp),
	-- fk/pk keys constraints
	CONSTRAINT pk_psp PRIMARY KEY (id)
);




CREATE SEQUENCE seq_canali start 1 increment 1 maxvalue 9223372036854775807 minvalue 1 cache 1 NO CYCLE;

CREATE TABLE canali
(
	cod_canale VARCHAR(35) NOT NULL,
	cod_intermediario VARCHAR(35) NOT NULL,
	tipo_versamento VARCHAR(4) NOT NULL,
	modello_pagamento INT NOT NULL,
	disponibilita TEXT,
	descrizione TEXT,
	condizioni VARCHAR(35),
	url_info VARCHAR(255),
	abilitato BOOLEAN NOT NULL,
	-- fk/pk columns
	id BIGINT DEFAULT nextval('seq_canali') NOT NULL,
	id_psp BIGINT NOT NULL,
	-- unique constraints
	CONSTRAINT unique_canali_1 UNIQUE (id_psp,cod_canale,tipo_versamento),
	-- fk/pk keys constraints
	CONSTRAINT fk_canali_1 FOREIGN KEY (id_psp) REFERENCES psp(id) ON DELETE CASCADE,
	CONSTRAINT pk_canali PRIMARY KEY (id)
);




CREATE SEQUENCE seq_intermediari start 1 increment 1 maxvalue 9223372036854775807 minvalue 1 cache 1 NO CYCLE;

CREATE TABLE intermediari
(
	cod_intermediario VARCHAR(35) NOT NULL,
	cod_connettore_pdd VARCHAR(35) NOT NULL,
	denominazione VARCHAR(255) NOT NULL,
	abilitato BOOLEAN NOT NULL,
	-- fk/pk columns
	id BIGINT DEFAULT nextval('seq_intermediari') NOT NULL,
	-- unique constraints
	CONSTRAINT unique_intermediari_1 UNIQUE (cod_intermediario),
	-- fk/pk keys constraints
	CONSTRAINT pk_intermediari PRIMARY KEY (id)
);




CREATE SEQUENCE seq_stazioni start 1 increment 1 maxvalue 9223372036854775807 minvalue 1 cache 1 NO CYCLE;

CREATE TABLE stazioni
(
	cod_stazione VARCHAR(35) NOT NULL,
	password VARCHAR(35) NOT NULL,
	abilitato BOOLEAN NOT NULL,
	application_code INT NOT NULL,
	-- fk/pk columns
	id BIGINT DEFAULT nextval('seq_stazioni') NOT NULL,
	id_intermediario BIGINT NOT NULL,
	-- unique constraints
	CONSTRAINT unique_stazioni_1 UNIQUE (cod_stazione),
	-- fk/pk keys constraints
	CONSTRAINT fk_stazioni_1 FOREIGN KEY (id_intermediario) REFERENCES intermediari(id) ON DELETE CASCADE,
	CONSTRAINT pk_stazioni PRIMARY KEY (id)
);




CREATE SEQUENCE seq_applicazioni start 1 increment 1 maxvalue 9223372036854775807 minvalue 1 cache 1 NO CYCLE;

CREATE TABLE applicazioni
(
	cod_applicazione VARCHAR(35) NOT NULL,
	abilitato BOOLEAN NOT NULL,
	principal VARCHAR(255) NOT NULL,
	firma_ricevuta VARCHAR(1) NOT NULL,
	cod_connettore_esito VARCHAR(255),
	cod_connettore_verifica VARCHAR(255),
	trusted BOOLEAN NOT NULL,
	-- fk/pk columns
	id BIGINT DEFAULT nextval('seq_applicazioni') NOT NULL,
	-- unique constraints
	CONSTRAINT unique_applicazioni_1 UNIQUE (cod_applicazione),
	CONSTRAINT unique_applicazioni_2 UNIQUE (principal),
	-- fk/pk keys constraints
	CONSTRAINT pk_applicazioni PRIMARY KEY (id)
);




CREATE SEQUENCE seq_domini start 1 increment 1 maxvalue 9223372036854775807 minvalue 1 cache 1 NO CYCLE;

CREATE TABLE domini
(
	cod_dominio VARCHAR(35) NOT NULL,
	gln VARCHAR(35) NOT NULL,
	abilitato BOOLEAN NOT NULL,
	ragione_sociale VARCHAR(70) NOT NULL,
	xml_conti_accredito BYTEA NOT NULL,
	xml_tabella_controparti BYTEA NOT NULL,
	riuso_iuv BOOLEAN NOT NULL,
	custom_iuv BOOLEAN NOT NULL,
	-- fk/pk columns
	id BIGINT DEFAULT nextval('seq_domini') NOT NULL,
	id_stazione BIGINT NOT NULL,
	id_applicazione_default BIGINT,
	-- unique constraints
	CONSTRAINT unique_domini_1 UNIQUE (cod_dominio),
	-- fk/pk keys constraints
	CONSTRAINT fk_domini_1 FOREIGN KEY (id_stazione) REFERENCES stazioni(id) ON DELETE CASCADE,
	CONSTRAINT fk_domini_2 FOREIGN KEY (id_applicazione_default) REFERENCES applicazioni(id) ON DELETE CASCADE,
	CONSTRAINT pk_domini PRIMARY KEY (id)
);




CREATE SEQUENCE seq_uo start 1 increment 1 maxvalue 9223372036854775807 minvalue 1 cache 1 NO CYCLE;

CREATE TABLE uo
(
	cod_uo VARCHAR(35) NOT NULL,
	abilitato BOOLEAN NOT NULL,
	uo_codice_identificativo VARCHAR(35),
	uo_denominazione VARCHAR(70),
	uo_indirizzo VARCHAR(70),
	uo_civico VARCHAR(16),
	uo_cap VARCHAR(16),
	uo_localita VARCHAR(35),
	uo_provincia VARCHAR(35),
	uo_nazione VARCHAR(2),
	-- fk/pk columns
	id BIGINT DEFAULT nextval('seq_uo') NOT NULL,
	id_dominio BIGINT NOT NULL,
	-- unique constraints
	CONSTRAINT unique_uo_1 UNIQUE (cod_uo,id_dominio),
	-- fk/pk keys constraints
	CONSTRAINT fk_uo_1 FOREIGN KEY (id_dominio) REFERENCES domini(id) ON DELETE CASCADE,
	CONSTRAINT pk_uo PRIMARY KEY (id)
);




CREATE SEQUENCE seq_operatori start 1 increment 1 maxvalue 9223372036854775807 minvalue 1 cache 1 NO CYCLE;

CREATE TABLE operatori
(
	principal VARCHAR(255) NOT NULL,
	nome VARCHAR(35) NOT NULL,
	profilo VARCHAR(16) NOT NULL,
	abilitato BOOLEAN NOT NULL DEFAULT true,
	-- fk/pk columns
	id BIGINT DEFAULT nextval('seq_operatori') NOT NULL,
	-- unique constraints
	CONSTRAINT unique_operatori_1 UNIQUE (principal),
	-- fk/pk keys constraints
	CONSTRAINT pk_operatori PRIMARY KEY (id)
);




CREATE SEQUENCE seq_operatori_uo start 1 increment 1 maxvalue 9223372036854775807 minvalue 1 cache 1 NO CYCLE;

CREATE TABLE operatori_uo
(
	-- fk/pk columns
	id BIGINT DEFAULT nextval('seq_operatori_uo') NOT NULL,
	id_operatore BIGINT NOT NULL,
	id_uo BIGINT NOT NULL,
	-- fk/pk keys constraints
	CONSTRAINT fk_operatori_uo_1 FOREIGN KEY (id_operatore) REFERENCES operatori(id) ON DELETE CASCADE,
	CONSTRAINT fk_operatori_uo_2 FOREIGN KEY (id_uo) REFERENCES uo(id) ON DELETE CASCADE,
	CONSTRAINT pk_operatori_uo PRIMARY KEY (id)
);




CREATE SEQUENCE seq_operatori_applicazioni start 1 increment 1 maxvalue 9223372036854775807 minvalue 1 cache 1 NO CYCLE;

CREATE TABLE operatori_applicazioni
(
	-- fk/pk columns
	id BIGINT DEFAULT nextval('seq_operatori_applicazioni') NOT NULL,
	id_operatore BIGINT NOT NULL,
	id_applicazione BIGINT NOT NULL,
	-- fk/pk keys constraints
	CONSTRAINT fk_operatori_applicazioni_1 FOREIGN KEY (id_operatore) REFERENCES operatori(id) ON DELETE CASCADE,
	CONSTRAINT fk_operatori_applicazioni_2 FOREIGN KEY (id_applicazione) REFERENCES applicazioni(id) ON DELETE CASCADE,
	CONSTRAINT pk_operatori_applicazioni PRIMARY KEY (id)
);




CREATE SEQUENCE seq_connettori start 1 increment 1 maxvalue 9223372036854775807 minvalue 1 cache 1 NO CYCLE;

CREATE TABLE connettori
(
	cod_connettore VARCHAR(255) NOT NULL,
	cod_proprieta VARCHAR(255) NOT NULL,
	valore VARCHAR(255) NOT NULL,
	-- fk/pk columns
	id BIGINT DEFAULT nextval('seq_connettori') NOT NULL,
	-- unique constraints
	CONSTRAINT unique_connettori_1 UNIQUE (cod_connettore,cod_proprieta),
	-- fk/pk keys constraints
	CONSTRAINT pk_connettori PRIMARY KEY (id)
);




CREATE SEQUENCE seq_portali start 1 increment 1 maxvalue 9223372036854775807 minvalue 1 cache 1 NO CYCLE;

CREATE TABLE portali
(
	cod_portale VARCHAR(35) NOT NULL,
	default_callback_url VARCHAR(512) NOT NULL,
	principal VARCHAR(255) NOT NULL,
	abilitato BOOLEAN NOT NULL,
	-- fk/pk columns
	id BIGINT DEFAULT nextval('seq_portali') NOT NULL,
	-- unique constraints
	CONSTRAINT unique_portali_1 UNIQUE (cod_portale),
	CONSTRAINT unique_portali_2 UNIQUE (principal),
	-- fk/pk keys constraints
	CONSTRAINT pk_portali PRIMARY KEY (id)
);




CREATE SEQUENCE seq_operatori_portali start 1 increment 1 maxvalue 9223372036854775807 minvalue 1 cache 1 NO CYCLE;

CREATE TABLE operatori_portali
(
	-- fk/pk columns
	id BIGINT DEFAULT nextval('seq_operatori_portali') NOT NULL,
	id_operatore BIGINT NOT NULL,
	id_portale BIGINT NOT NULL,
	-- fk/pk keys constraints
	CONSTRAINT fk_operatori_portali_1 FOREIGN KEY (id_operatore) REFERENCES operatori(id) ON DELETE CASCADE,
	CONSTRAINT fk_operatori_portali_2 FOREIGN KEY (id_portale) REFERENCES portali(id) ON DELETE CASCADE,
	CONSTRAINT pk_operatori_portali PRIMARY KEY (id)
);




CREATE SEQUENCE seq_portali_applicazioni start 1 increment 1 maxvalue 9223372036854775807 minvalue 1 cache 1 NO CYCLE;

CREATE TABLE portali_applicazioni
(
	-- fk/pk columns
	id BIGINT DEFAULT nextval('seq_portali_applicazioni') NOT NULL,
	id_portale BIGINT NOT NULL,
	id_applicazione BIGINT NOT NULL,
	-- fk/pk keys constraints
	CONSTRAINT fk_portali_applicazioni_1 FOREIGN KEY (id_portale) REFERENCES portali(id) ON DELETE CASCADE,
	CONSTRAINT fk_portali_applicazioni_2 FOREIGN KEY (id_applicazione) REFERENCES applicazioni(id) ON DELETE CASCADE,
	CONSTRAINT pk_portali_applicazioni PRIMARY KEY (id)
);




CREATE SEQUENCE seq_iban_accredito start 1 increment 1 maxvalue 9223372036854775807 minvalue 1 cache 1 NO CYCLE;

CREATE TABLE iban_accredito
(
	cod_iban VARCHAR(255) NOT NULL,
	id_seller_bank VARCHAR(255),
	id_negozio VARCHAR(255),
	bic_accredito VARCHAR(255),
	iban_appoggio VARCHAR(255),
	bic_appoggio VARCHAR(255),
	postale BOOLEAN NOT NULL,
	attivato BOOLEAN NOT NULL,
	abilitato BOOLEAN NOT NULL,
	-- fk/pk columns
	id BIGINT DEFAULT nextval('seq_iban_accredito') NOT NULL,
	id_dominio BIGINT NOT NULL,
	-- unique constraints
	CONSTRAINT unique_iban_accredito_1 UNIQUE (cod_iban,id_dominio),
	-- fk/pk keys constraints
	CONSTRAINT fk_iban_accredito_1 FOREIGN KEY (id_dominio) REFERENCES domini(id) ON DELETE CASCADE,
	CONSTRAINT pk_iban_accredito PRIMARY KEY (id)
);




CREATE SEQUENCE seq_tributi start 1 increment 1 maxvalue 9223372036854775807 minvalue 1 cache 1 NO CYCLE;

CREATE TABLE tributi
(
	cod_tributo VARCHAR(35) NOT NULL,
	abilitato BOOLEAN NOT NULL,
	descrizione VARCHAR(255),
	tipo_contabilita VARCHAR(1) NOT NULL,
	codice_contabilita VARCHAR(255) NOT NULL,
	-- fk/pk columns
	id BIGINT DEFAULT nextval('seq_tributi') NOT NULL,
	id_dominio BIGINT NOT NULL,
	id_iban_accredito BIGINT,
	-- unique constraints
	CONSTRAINT unique_tributi_1 UNIQUE (id_dominio,cod_tributo),
	-- fk/pk keys constraints
	CONSTRAINT fk_tributi_1 FOREIGN KEY (id_dominio) REFERENCES domini(id) ON DELETE CASCADE,
	CONSTRAINT fk_tributi_2 FOREIGN KEY (id_iban_accredito) REFERENCES iban_accredito(id) ON DELETE CASCADE,
	CONSTRAINT pk_tributi PRIMARY KEY (id)
);




CREATE SEQUENCE seq_applicazioni_tributi start 1 increment 1 maxvalue 9223372036854775807 minvalue 1 cache 1 NO CYCLE;

CREATE TABLE applicazioni_tributi
(
	-- fk/pk columns
	id BIGINT DEFAULT nextval('seq_applicazioni_tributi') NOT NULL,
	id_applicazione BIGINT,
	id_tributo BIGINT NOT NULL,
	-- fk/pk keys constraints
	CONSTRAINT fk_applicazioni_tributi_1 FOREIGN KEY (id_applicazione) REFERENCES applicazioni(id) ON DELETE CASCADE,
	CONSTRAINT fk_applicazioni_tributi_2 FOREIGN KEY (id_tributo) REFERENCES tributi(id) ON DELETE CASCADE,
	CONSTRAINT pk_applicazioni_tributi PRIMARY KEY (id)
);




CREATE SEQUENCE seq_applicazioni_domini start 1 increment 1 maxvalue 9223372036854775807 minvalue 1 cache 1 NO CYCLE;

CREATE TABLE applicazioni_domini
(
	-- fk/pk columns
	id BIGINT DEFAULT nextval('seq_applicazioni_domini') NOT NULL,
	id_applicazione BIGINT,
	id_dominio BIGINT NOT NULL,
	-- fk/pk keys constraints
	CONSTRAINT fk_applicazioni_domini_1 FOREIGN KEY (id_applicazione) REFERENCES applicazioni(id) ON DELETE CASCADE,
	CONSTRAINT fk_applicazioni_domini_2 FOREIGN KEY (id_dominio) REFERENCES domini(id) ON DELETE CASCADE,
	CONSTRAINT pk_applicazioni_domini PRIMARY KEY (id)
);




CREATE SEQUENCE seq_versamenti start 1 increment 1 maxvalue 9223372036854775807 minvalue 1 cache 1 NO CYCLE;

CREATE TABLE versamenti
(
	cod_versamento_ente VARCHAR(35) NOT NULL,
	importo_totale DOUBLE PRECISION NOT NULL,
	stato_versamento VARCHAR(35) NOT NULL,
	descrizione_stato VARCHAR(255),
	-- Indica se, decorsa la dataScadenza, deve essere aggiornato da remoto o essere considerato scaduto
	aggiornabile BOOLEAN NOT NULL,
	data_creazione TIMESTAMP NOT NULL,
	data_scadenza TIMESTAMP,
	data_ora_ultimo_aggiornamento TIMESTAMP NOT NULL,
	causale_versamento VARCHAR(511),
	debitore_identificativo VARCHAR(35) NOT NULL,
	debitore_anagrafica VARCHAR(70) NOT NULL,
	debitore_indirizzo VARCHAR(70),
	debitore_civico VARCHAR(16),
	debitore_cap VARCHAR(16),
	debitore_localita VARCHAR(35),
	debitore_provincia VARCHAR(35),
	debitore_nazione VARCHAR(2),
	cod_lotto VARCHAR(35),
	cod_versamento_lotto VARCHAR(35),
	cod_anno_tributario VARCHAR(35),
	-- fk/pk columns
	id BIGINT DEFAULT nextval('seq_versamenti') NOT NULL,
	id_uo BIGINT NOT NULL,
	id_applicazione BIGINT NOT NULL,
	-- unique constraints
	CONSTRAINT unique_versamenti_1 UNIQUE (cod_versamento_ente,id_applicazione),
	-- fk/pk keys constraints
	CONSTRAINT fk_versamenti_1 FOREIGN KEY (id_uo) REFERENCES uo(id) ON DELETE CASCADE,
	CONSTRAINT fk_versamenti_2 FOREIGN KEY (id_applicazione) REFERENCES applicazioni(id) ON DELETE CASCADE,
	CONSTRAINT pk_versamenti PRIMARY KEY (id)
);




CREATE SEQUENCE seq_singoli_versamenti start 1 increment 1 maxvalue 9223372036854775807 minvalue 1 cache 1 NO CYCLE;

CREATE TABLE singoli_versamenti
(
	cod_singolo_versamento_ente VARCHAR(70) NOT NULL,
	stato_singolo_versamento VARCHAR(35) NOT NULL,
	importo_singolo_versamento DOUBLE PRECISION NOT NULL,
	anno_riferimento INT,
	-- MARCA BOLLO Valori possibili:\n01: Imposta di bollo
	tipo_bollo VARCHAR(2),
	-- MARCA BOLLO: Digest in Base64 del documento da bollare
	hash_documento VARCHAR(70),
	-- MARCA BOLLO: Sigla automobilistica della provincia di residenza
	provincia_residenza VARCHAR(2),
	tipo_contabilita VARCHAR(1),
	codice_contabilita VARCHAR(255),
	-- fk/pk columns
	id BIGINT DEFAULT nextval('seq_singoli_versamenti') NOT NULL,
	id_versamento BIGINT NOT NULL,
	id_tributo BIGINT,
	id_iban_accredito BIGINT,
	-- unique constraints
	CONSTRAINT unique_singoli_versamenti_1 UNIQUE (id_versamento,cod_singolo_versamento_ente),
	-- fk/pk keys constraints
	CONSTRAINT fk_singoli_versamenti_1 FOREIGN KEY (id_versamento) REFERENCES versamenti(id) ON DELETE CASCADE,
	CONSTRAINT fk_singoli_versamenti_2 FOREIGN KEY (id_tributo) REFERENCES tributi(id) ON DELETE CASCADE,
	CONSTRAINT fk_singoli_versamenti_3 FOREIGN KEY (id_iban_accredito) REFERENCES iban_accredito(id) ON DELETE CASCADE,
	CONSTRAINT pk_singoli_versamenti PRIMARY KEY (id)
);




CREATE SEQUENCE seq_rpt start 1 increment 1 maxvalue 9223372036854775807 minvalue 1 cache 1 NO CYCLE;

CREATE TABLE rpt
(
	cod_carrello VARCHAR(35),
	iuv VARCHAR(35) NOT NULL,
	ccp VARCHAR(35) NOT NULL,
	cod_dominio VARCHAR(35) NOT NULL,
	-- Identificativo dell'RPT utilizzato come riferimento nell'RT
	cod_msg_richiesta VARCHAR(35) NOT NULL,
	-- Data di creazione dell'RPT
	data_msg_richiesta TIMESTAMP NOT NULL,
	-- Stato RPT secondo la codifica AgID
	stato VARCHAR(35) NOT NULL,
	descrizione_stato TEXT,
	cod_sessione VARCHAR(255),
	-- Indirizzo del portale psp a cui redirigere il cittadino per eseguire il pagamento
	psp_redirect_url VARCHAR(512),
	xml_rpt BYTEA NOT NULL,
	data_aggiornamento_stato TIMESTAMP NOT NULL,
	-- Indirizzo di ritorno al portale dell'ente al termine del pagamento
	callback_url TEXT,
	modello_pagamento VARCHAR(16) NOT NULL,
	cod_msg_ricevuta VARCHAR(35),
	data_msg_ricevuta TIMESTAMP,
	firma_ricevuta VARCHAR(1) NOT NULL,
	-- Esito del pagamento:\n0: Eseguito\n1: Non eseguito\n2: Parzialmente eseguito\n3: Decorrenza\n4: Decorrenza Parziale
	cod_esito_pagamento INT,
	importo_totale_pagato DOUBLE PRECISION,
	xml_rt BYTEA,
	cod_stazione VARCHAR(35) NOT NULL,
	-- fk/pk columns
	id BIGINT DEFAULT nextval('seq_rpt') NOT NULL,
	id_versamento BIGINT NOT NULL,
	id_canale BIGINT NOT NULL,
	id_portale BIGINT,
	-- unique constraints
	CONSTRAINT unique_rpt_1 UNIQUE (cod_msg_richiesta),
	CONSTRAINT unique_rpt_2 UNIQUE (iuv,ccp,cod_dominio),
	-- fk/pk keys constraints
	CONSTRAINT fk_rpt_1 FOREIGN KEY (id_versamento) REFERENCES versamenti(id) ON DELETE CASCADE,
	CONSTRAINT fk_rpt_2 FOREIGN KEY (id_canale) REFERENCES canali(id) ON DELETE CASCADE,
	CONSTRAINT fk_rpt_3 FOREIGN KEY (id_portale) REFERENCES portali(id) ON DELETE CASCADE,
	CONSTRAINT pk_rpt PRIMARY KEY (id)
);




CREATE SEQUENCE seq_rr start 1 increment 1 maxvalue 9223372036854775807 minvalue 1 cache 1 NO CYCLE;

CREATE TABLE rr
(
	cod_dominio VARCHAR(35) NOT NULL,
	iuv VARCHAR(35) NOT NULL,
	ccp VARCHAR(35) NOT NULL,
	cod_msg_revoca VARCHAR(35) NOT NULL,
	data_msg_revoca TIMESTAMP NOT NULL,
	data_msg_esito TIMESTAMP,
	stato VARCHAR(35) NOT NULL,
	descrizione_stato VARCHAR(512),
	importo_totale_richiesto DOUBLE PRECISION NOT NULL,
	cod_msg_esito VARCHAR(35),
	importo_totale_revocato DOUBLE PRECISION,
	xml_rr BYTEA NOT NULL,
	xml_er BYTEA,
	-- fk/pk columns
	id BIGINT DEFAULT nextval('seq_rr') NOT NULL,
	id_rpt BIGINT NOT NULL,
	-- unique constraints
	CONSTRAINT unique_rr_1 UNIQUE (cod_msg_revoca),
	-- fk/pk keys constraints
	CONSTRAINT fk_rr_1 FOREIGN KEY (id_rpt) REFERENCES rpt(id) ON DELETE CASCADE,
	CONSTRAINT pk_rr PRIMARY KEY (id)
);




CREATE SEQUENCE seq_notifiche start 1 increment 1 maxvalue 9223372036854775807 minvalue 1 cache 1 NO CYCLE;

CREATE TABLE notifiche
(
	tipo_esito VARCHAR(16) NOT NULL,
	data_creazione TIMESTAMP NOT NULL,
	stato VARCHAR(16) NOT NULL,
	descrizione_stato VARCHAR(255),
	data_aggiornamento_stato TIMESTAMP NOT NULL,
	data_prossima_spedizione TIMESTAMP NOT NULL,
	tentativi_spedizione BIGINT,
	-- fk/pk columns
	id BIGINT DEFAULT nextval('seq_notifiche') NOT NULL,
	id_applicazione BIGINT NOT NULL,
	id_rpt BIGINT,
	id_rr BIGINT,
	-- fk/pk keys constraints
	CONSTRAINT fk_notifiche_1 FOREIGN KEY (id_applicazione) REFERENCES applicazioni(id) ON DELETE CASCADE,
	CONSTRAINT fk_notifiche_2 FOREIGN KEY (id_rpt) REFERENCES rpt(id) ON DELETE CASCADE,
	CONSTRAINT fk_notifiche_3 FOREIGN KEY (id_rr) REFERENCES rr(id) ON DELETE CASCADE,
	CONSTRAINT pk_notifiche PRIMARY KEY (id)
);




CREATE SEQUENCE seq_iuv start 1 increment 1 maxvalue 9223372036854775807 minvalue 1 cache 1 NO CYCLE;

CREATE TABLE iuv
(
	prg BIGINT NOT NULL,
	iuv VARCHAR(35) NOT NULL,
	application_code INT NOT NULL,
	data_generazione DATE NOT NULL,
	tipo_iuv VARCHAR(1) NOT NULL,
	cod_versamento_ente VARCHAR(35),
	-- fk/pk columns
	id BIGINT DEFAULT nextval('seq_iuv') NOT NULL,
	id_applicazione BIGINT NOT NULL,
	id_dominio BIGINT NOT NULL,
	-- unique constraints
	CONSTRAINT unique_iuv_1 UNIQUE (id_dominio,iuv),
	-- fk/pk keys constraints
	CONSTRAINT fk_iuv_1 FOREIGN KEY (id_applicazione) REFERENCES applicazioni(id) ON DELETE CASCADE,
	CONSTRAINT fk_iuv_2 FOREIGN KEY (id_dominio) REFERENCES domini(id) ON DELETE CASCADE,
	CONSTRAINT pk_iuv PRIMARY KEY (id)
);




CREATE SEQUENCE seq_fr start 1 increment 1 maxvalue 9223372036854775807 minvalue 1 cache 1 NO CYCLE;

CREATE TABLE fr
(
	cod_flusso VARCHAR(35) NOT NULL,
	stato VARCHAR(35) NOT NULL,
	descrizione_stato TEXT,
	iur VARCHAR(35) NOT NULL,
	anno_riferimento INT NOT NULL,
	data_ora_flusso TIMESTAMP,
	data_regolamento TIMESTAMP,
	numero_pagamenti BIGINT,
	importo_totale_pagamenti DOUBLE PRECISION,
	cod_bic_riversamento VARCHAR(35),
	xml BYTEA NOT NULL,
	-- fk/pk columns
	id BIGINT DEFAULT nextval('seq_fr') NOT NULL,
	id_psp BIGINT NOT NULL,
	id_dominio BIGINT NOT NULL,
	-- unique constraints
	CONSTRAINT unique_fr_1 UNIQUE (cod_flusso,anno_riferimento),
	-- fk/pk keys constraints
	CONSTRAINT fk_fr_1 FOREIGN KEY (id_psp) REFERENCES psp(id) ON DELETE CASCADE,
	CONSTRAINT fk_fr_2 FOREIGN KEY (id_dominio) REFERENCES domini(id) ON DELETE CASCADE,
	CONSTRAINT pk_fr PRIMARY KEY (id)
);




CREATE SEQUENCE seq_fr_applicazioni start 1 increment 1 maxvalue 9223372036854775807 minvalue 1 cache 1 NO CYCLE;

CREATE TABLE fr_applicazioni
(
	numero_pagamenti BIGINT NOT NULL,
	importo_totale_pagamenti DOUBLE PRECISION NOT NULL,
	-- fk/pk columns
	id BIGINT DEFAULT nextval('seq_fr_applicazioni') NOT NULL,
	id_applicazione BIGINT NOT NULL,
	id_fr BIGINT NOT NULL,
	-- fk/pk keys constraints
	CONSTRAINT fk_fr_applicazioni_1 FOREIGN KEY (id_applicazione) REFERENCES applicazioni(id) ON DELETE CASCADE,
	CONSTRAINT fk_fr_applicazioni_2 FOREIGN KEY (id_fr) REFERENCES fr(id) ON DELETE CASCADE,
	CONSTRAINT pk_fr_applicazioni PRIMARY KEY (id)
);




CREATE SEQUENCE seq_pagamenti start 1 increment 1 maxvalue 9223372036854775807 minvalue 1 cache 1 NO CYCLE;

CREATE TABLE pagamenti
(
	cod_singolo_versamento_ente VARCHAR(35) NOT NULL,
	importo_pagato DOUBLE PRECISION NOT NULL,
	iur VARCHAR(35) NOT NULL,
	data_pagamento TIMESTAMP NOT NULL,
	commissioni_psp DOUBLE PRECISION,
	-- Valori possibili:\nES: Esito originario\nBD: Marca da Bollo
	tipo_allegato VARCHAR(2),
	allegato BYTEA,
	rendicontazione_esito INT,
	rendicontazione_data TIMESTAMP,
	codflusso_rendicontazione VARCHAR(35),
	anno_riferimento INT,
	indice_singolo_pagamento INT,
	causale_revoca VARCHAR(140),
	dati_revoca VARCHAR(140),
	importo_revocato DOUBLE PRECISION,
	esito_revoca VARCHAR(140),
	dati_esito_revoca VARCHAR(140),
	rendicontazione_esito_revoca INT,
	rendicontazione_data_revoca TIMESTAMP,
	cod_flusso_rendicontaz_revoca VARCHAR(35),
	anno_riferimento_revoca INT,
	ind_singolo_pagamento_revoca INT,
	-- fk/pk columns
	id BIGINT DEFAULT nextval('seq_pagamenti') NOT NULL,
	id_rpt BIGINT NOT NULL,
	id_singolo_versamento BIGINT NOT NULL,
	id_fr_applicazione BIGINT,
	id_rr BIGINT,
	id_fr_applicazione_revoca BIGINT,
	-- fk/pk keys constraints
	CONSTRAINT fk_pagamenti_1 FOREIGN KEY (id_rpt) REFERENCES rpt(id) ON DELETE CASCADE,
	CONSTRAINT fk_pagamenti_2 FOREIGN KEY (id_singolo_versamento) REFERENCES singoli_versamenti(id) ON DELETE CASCADE,
	CONSTRAINT fk_pagamenti_3 FOREIGN KEY (id_fr_applicazione) REFERENCES fr_applicazioni(id) ON DELETE CASCADE,
	CONSTRAINT fk_pagamenti_4 FOREIGN KEY (id_rr) REFERENCES rr(id) ON DELETE CASCADE,
	CONSTRAINT fk_pagamenti_5 FOREIGN KEY (id_fr_applicazione_revoca) REFERENCES fr_applicazioni(id) ON DELETE CASCADE,
	CONSTRAINT pk_pagamenti PRIMARY KEY (id)
);




CREATE SEQUENCE seq_eventi start 1 increment 1 maxvalue 9223372036854775807 minvalue 1 cache 1 NO CYCLE;

CREATE TABLE eventi
(
	cod_dominio VARCHAR(35),
	iuv VARCHAR(35),
	ccp VARCHAR(35),
	cod_psp VARCHAR(35),
	tipo_versamento VARCHAR(10),
	componente VARCHAR(4),
	categoria_evento VARCHAR(1),
	tipo_evento VARCHAR(35),
	sottotipo_evento VARCHAR(35),
	erogatore VARCHAR(35),
	fruitore VARCHAR(35),
	cod_stazione VARCHAR(35),
	cod_canale VARCHAR(35),
	parametri_1 VARCHAR(512),
	parametri_2 VARCHAR(512),
	esito VARCHAR(35),
	data_1 TIMESTAMP,
	data_2 TIMESTAMP,
	-- fk/pk columns
	id BIGINT DEFAULT nextval('seq_eventi') NOT NULL,
	-- fk/pk keys constraints
	CONSTRAINT pk_eventi PRIMARY KEY (id)
);




CREATE SEQUENCE seq_rendicontazioni_senza_rpt start 1 increment 1 maxvalue 9223372036854775807 minvalue 1 cache 1 NO CYCLE;

CREATE TABLE rendicontazioni_senza_rpt
(
	importo_pagato DOUBLE PRECISION NOT NULL,
	iur VARCHAR(35) NOT NULL,
	rendicontazione_data DATE NOT NULL,
	-- fk/pk columns
	id BIGINT DEFAULT nextval('seq_rendicontazioni_senza_rpt') NOT NULL,
	id_fr_applicazione BIGINT NOT NULL,
	id_iuv BIGINT NOT NULL,
	id_singolo_versamento BIGINT,
	-- fk/pk keys constraints
	CONSTRAINT fk_rendicontazioni_senza_rpt_1 FOREIGN KEY (id_fr_applicazione) REFERENCES fr_applicazioni(id) ON DELETE CASCADE,
	CONSTRAINT fk_rendicontazioni_senza_rpt_2 FOREIGN KEY (id_iuv) REFERENCES iuv(id) ON DELETE CASCADE,
	CONSTRAINT fk_rendicontazioni_senza_rpt_3 FOREIGN KEY (id_singolo_versamento) REFERENCES singoli_versamenti(id) ON DELETE CASCADE,
	CONSTRAINT pk_rendicontazioni_senza_rpt PRIMARY KEY (id)
);




CREATE TABLE ID_MESSAGGIO_RELATIVO
(
	COUNTER BIGINT NOT NULL,
	PROTOCOLLO VARCHAR(255) NOT NULL,
	INFO_ASSOCIATA VARCHAR(255) NOT NULL,
	ora_registrazione TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	-- fk/pk columns
	-- fk/pk keys constraints
	CONSTRAINT pk_ID_MESSAGGIO_RELATIVO PRIMARY KEY (PROTOCOLLO,INFO_ASSOCIATA)
);

