CREATE TABLE movimentacoes (
	id INTEGER PRIMARY KEY auto_increment,
    produto_id	INTEGER NOT NULL,
    quantidade INTEGER NOT NULL,
    local VARCHAR(100) NOT NULL,
    tipo_local VARCHAR(100) NOT NULL,
	FOREIGN KEY (produto_id) REFERENCES produtos(codigo) 
);