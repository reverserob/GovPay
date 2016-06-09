ALTER TABLE psp MODIFY COLUMN ragione_sociale VARCHAR(70);
ALTER TABLE versamenti ADD COLUMN cod_lotto VARCHAR(35);
ALTER TABLE versamenti ADD COLUMN cod_versamento_lotto VARCHAR(35);
ALTER TABLE versamenti ADD COLUMN cod_anno_tributario VARCHAR(35);
ALTER TABLE pagamenti ADD COLUMN data_acquisizione TIMESTAMP(3) NOT NULL DEFAULT 0;
ALTER TABLE pagamenti ADD COLUMN data_acquisizione_revoca TIMESTAMP(3) DEFAULT 0;
