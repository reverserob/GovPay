ALTER TABLE psp ALTER COLUMN ragione_sociale TYPE VARCHAR(70);
ALTER TABLE versamenti ADD COLUMN cod_lotto VARCHAR(35);
ALTER TABLE versamenti ADD COLUMN cod_versamento_lotto VARCHAR(35);
ALTER TABLE versamenti ADD COLUMN cod_anno_tributario VARCHAR(35);
ALTER TABLE pagamenti ADD COLUMN data_acquisizione TIMESTAMP NOT NULL DEFAULT current_timestamp;
ALTER TABLE pagamenti ADD COLUMN data_acquisizione_revoca TIMESTAMP;
