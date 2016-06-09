ALTER TABLE psp MODIFY ragione_sociale VARCHAR(70);
ALTER TABLE versamenti ADD cod_lotto VARCHAR(35);
ALTER TABLE versamenti ADD cod_versamento_lotto VARCHAR(35);
ALTER TABLE versamenti ADD cod_anno_tributario VARCHAR(35);
ALTER TABLE pagamenti ADD data_acquisizione TIMESTAMP NOT NULL;
ALTER TABLE pagamenti ADD data_acquisizione_revoca TIMESTAMP;
