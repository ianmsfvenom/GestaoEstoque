CREATE TABLE `produtos` (
  `codigo` int(11) NOT NULL AUTO_INCREMENT,
  `nome` varchar(50) NOT NULL,
  `categoria` varchar(50) NOT NULL,
  `fornecedor_id` int(11) NOT NULL,
  `unidade_medida` varchar(10) NOT NULL,
  `preco_compra` decimal(10,2) NOT NULL,
  `preco_venda` decimal(10,2) NOT NULL,
  `descricao` varchar(255) NOT NULL,
  `codigo_barra` varchar(50) NOT NULL,
  PRIMARY KEY (`codigo`),
  KEY `fornecedor_id` (`fornecedor_id`),
  CONSTRAINT `produtos_ibfk_1` FOREIGN KEY (`fornecedor_id`) REFERENCES `fornecedores` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=94945827 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci