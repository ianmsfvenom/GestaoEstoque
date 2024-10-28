DELIMITER //

CREATE TRIGGER trg_diminui_estoque
AFTER INSERT ON saidas
FOR EACH ROW
BEGIN
    UPDATE estoques
    SET quantidade = quantidade - NEW.quantidade
    WHERE produto_id = NEW.produto_id;
END;

//

DELIMITER ;


DELIMITER //

CREATE TRIGGER trg_aumenta_estoque
AFTER DELETE ON saidas
FOR EACH ROW
BEGIN
    UPDATE estoques
    SET quantidade = quantidade + OLD.quantidade
    WHERE produto_id = OLD.produto_id;
END;

//

DELIMITER ;


DELIMITER //

CREATE TRIGGER trg_aumenta_estoque_entrada
AFTER INSERT ON entradas
FOR EACH ROW
BEGIN
	UPDATE estoques 
    SET quantidade = quantidade + NEW.quantidade 
    WHERE produto_id = NEW.produto_id;
END;

//

DELIMITER ;

DELIMITER //

CREATE TRIGGER trg_diminui_estoque_entrada
AFTER DELETE ON entradas
FOR EACH ROW
BEGIN
	UPDATE estoques
    SET quantidade = quantidade - OLD.quantidade 
    WHERE produto_id = OLD.produto_id;
END;

//

DELIMITER ;