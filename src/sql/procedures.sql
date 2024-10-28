CREATE DEFINER=`root`@`localhost` PROCEDURE `produtos_alta_rotatividade`()
BEGIN
    -- Criar tabela temporária com o total de vendas por produto
    CREATE TEMPORARY TABLE vendas_totais AS (
        SELECT 
            s.produto_id, 
            p.nome, 
            SUM(s.quantidade) AS total_vendido
        FROM saidas s
        JOIN produtos p ON s.produto_id = p.codigo
        WHERE s.saida = 'Compra de cliente'
        GROUP BY s.produto_id, p.nome
    );

    -- Calcular a média geral de vendas
    SET @media_vendas = (SELECT AVG(total_vendido) FROM vendas_totais);

    -- Selecionar produtos com vendas acima da média (alta rotatividade)
    SELECT 
        produto_id, 
        nome, 
        total_vendido
    FROM vendas_totais
    WHERE total_vendido > @media_vendas
    ORDER BY total_vendido DESC;

    -- Limpar a tabela temporária
    DROP TEMPORARY TABLE IF EXISTS vendas_totais;
END


CREATE DEFINER=`root`@`localhost` PROCEDURE `produtos_baixa_rotatividade`()
BEGIN
    -- Criar tabela temporária com o total de vendas por produto
    CREATE TEMPORARY TABLE vendas_totais AS (
        SELECT 
            s.produto_id, 
            p.nome, 
            SUM(s.quantidade) AS total_vendido
        FROM saidas s
        JOIN produtos p ON s.produto_id = p.codigo
        WHERE s.saida = 'Compra de cliente'
        GROUP BY s.produto_id, p.nome
    );

    -- Calcular a média geral de vendas
    SET @media_vendas = (SELECT AVG(total_vendido) FROM vendas_totais);

    -- Selecionar produtos com vendas abaixo ou igual à média (baixa rotatividade)
    SELECT 
        produto_id, 
        nome, 
        total_vendido
    FROM vendas_totais
    WHERE total_vendido <= @media_vendas
    ORDER BY total_vendido ASC;

    -- Limpar a tabela temporária
    DROP TEMPORARY TABLE IF EXISTS vendas_totais;
END


CREATE DEFINER=`root`@`localhost` PROCEDURE `calcular_curva_abc`()
BEGIN
    -- Etapa 1: Calcular o valor total das vendas por produto
    CREATE TEMPORARY TABLE vendas_agrupadas AS (
        SELECT 
            s.produto_id, 
            p.nome, 
            SUM(s.quantidade) AS total_quantidade, 
            p.preco_venda, 
            SUM(s.quantidade * p.preco_venda) AS valor_total
        FROM saidas s
        JOIN produtos p ON s.produto_id = p.codigo
        WHERE s.saida = 'Compra de cliente'
        GROUP BY s.produto_id, p.nome, p.preco_venda
    );

    -- Etapa 2: Calcular o percentual de cada produto no total vendido
    CREATE TEMPORARY TABLE vendas_com_percentual AS (
        SELECT 
            produto_id, 
            nome, 
            total_quantidade, 
            preco_venda, 
            valor_total,
            valor_total / (SELECT SUM(valor_total) FROM vendas_agrupadas) AS percentual_acumulado
        FROM vendas_agrupadas
    );

    -- Etapa 3: Calcular o percentual cumulativo e classificar em A, B ou C
    CREATE TEMPORARY TABLE vendas_abc AS (
        SELECT 
            produto_id, 
            nome, 
            total_quantidade, 
            preco_venda, 
            valor_total, 
            percentual_acumulado,
            SUM(percentual_acumulado) OVER (ORDER BY valor_total DESC) AS percentual_cumulativo
        FROM vendas_com_percentual
    );

    -- Etapa 4: Retornar os produtos com a classificação ABC
    SELECT 
        produto_id, 
        nome, 
        total_quantidade, 
        preco_venda, 
        valor_total, 
        percentual_cumulativo,
        CASE 
            WHEN percentual_cumulativo <= 0.8 THEN 'A'
            WHEN percentual_cumulativo <= 0.95 THEN 'B'
            ELSE 'C'
        END AS classe_abc
    FROM vendas_abc
    ORDER BY percentual_cumulativo;

    -- Limpar as tabelas temporárias
    DROP TEMPORARY TABLE IF EXISTS vendas_agrupadas;
    DROP TEMPORARY TABLE IF EXISTS vendas_com_percentual;
    DROP TEMPORARY TABLE IF EXISTS vendas_abc;
END


CREATE DEFINER=`root`@`localhost` PROCEDURE `margem_lucro_total`()
BEGIN
    DECLARE lucro_total DECIMAL(10, 2);
    DECLARE receita_total DECIMAL(10, 2);
    DECLARE margem_lucro DECIMAL(10, 2);

    -- Calcular o lucro total e a receita total
    SELECT 
        SUM((p.preco_venda - p.preco_compra) * s.quantidade) AS total_lucro,
        SUM(s.quantidade * p.preco_venda) AS total_receita
    INTO 
        lucro_total, 
        receita_total
    FROM saidas s
    JOIN produtos p ON s.produto_id = p.codigo
    WHERE s.saida = 'Compra de cliente';

    -- Calcular a margem de lucro total
    IF receita_total > 0 THEN
        SET margem_lucro = (lucro_total / receita_total) * 100;
    ELSE
        SET margem_lucro = 0;
    END IF;

    -- Retornar o resultado
    SELECT 
        lucro_total AS lucro_total,
        receita_total AS receita_total,
        margem_lucro AS margem_lucro_total;
END