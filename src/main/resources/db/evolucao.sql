ALTER TABLE pergunta ADD COLUMN IF NOT EXISTS status varchar(12);
ALTER TABLE pergunta ADD COLUMN IF NOT EXISTS categoria varchar(80);
ALTER TABLE pergunta ADD COLUMN IF NOT EXISTS explicacao varchar(500);
ALTER TABLE pergunta ADD COLUMN IF NOT EXISTS motivo_rejeicao varchar(500);
ALTER TABLE pergunta ADD COLUMN IF NOT EXISTS chave_seed varchar(40);
ALTER TABLE pergunta ADD COLUMN IF NOT EXISTS autor_id bigint;
ALTER TABLE pergunta ADD COLUMN IF NOT EXISTS revisor_id bigint;
ALTER TABLE pergunta ADD COLUMN IF NOT EXISTS revisada_em timestamptz;
ALTER TABLE pergunta ALTER COLUMN enunciado TYPE varchar(500);

ALTER TABLE pontuacao ADD COLUMN IF NOT EXISTS modalidade varchar(12);
ALTER TABLE pontuacao ADD COLUMN IF NOT EXISTS nivel varchar(10);
ALTER TABLE pontuacao ADD COLUMN IF NOT EXISTS respondidas integer;
ALTER TABLE pontuacao ADD COLUMN IF NOT EXISTS erros integer;
ALTER TABLE pontuacao ADD COLUMN IF NOT EXISTS resultado varchar(12);

CREATE UNIQUE INDEX IF NOT EXISTS uk_pergunta_chave_seed ON pergunta (chave_seed);

UPDATE pergunta SET status = 'APROVADA' WHERE status IS NULL;
UPDATE pontuacao SET modalidade = 'CLASSICO' WHERE modalidade IS NULL;
UPDATE pontuacao SET respondidas = acertos WHERE respondidas IS NULL;
UPDATE pontuacao SET erros = GREATEST(0, COALESCE(respondidas, acertos) - acertos) WHERE erros IS NULL;
UPDATE pontuacao SET resultado = 'PARADA' WHERE resultado IS NULL;
