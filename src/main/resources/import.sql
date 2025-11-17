-- =====================
-- PRODUTOS
-- =====================
insert into produto(id, descricao, valor) values ( nextval('produto_seq'), 'Carne', 20 );
insert into produto(id, descricao, valor) values ( nextval('produto_seq'), 'Maionese', 5 );
insert into produto(id, descricao, valor) values ( nextval('produto_seq'), 'Arroz', 25 );
insert into produto(id, descricao, valor) values ( nextval('produto_seq'), 'Macarrão', 10 );
insert into produto(id, descricao, valor) values ( nextval('produto_seq'), 'Café', 18 );

-- =====================
-- PESSOAS FISICAS
-- =====================
insert into pessoa_fisica(id,cpf,nome,email,telefone) values (nextval('pessoa_seq'),'00011122233','bruno','testecpf@email','(12) 12345-6789');

-- =====================
-- PESSOAS JURIDICAS
-- =====================
insert into pessoa_juridica(id,cnpj,razao_social,email,telefone) values (nextval('pessoa_seq'),'99.999.999/0001-99','ifto','testecnpj@email','(12) 12345-6789');

-- =====================
-- VENDAS
-- =====================
insert into venda(id, data, pessoa_id) values ( nextval('venda_seq'), current_date,1 );
insert into venda(id, data,pessoa_id) values ( nextval('venda_seq'), current_date, 2);

-- =====================
-- ITENS DE VENDA
-- =====================
insert into itemvenda(id, quantidade, produto_id, venda_id) values ( nextval('itemvenda_seq'), 2, 1, 1 );
insert into itemvenda(id, quantidade, produto_id, venda_id) values ( nextval('itemvenda_seq'), 3, 2, 1 );
insert into itemvenda(id, quantidade, produto_id, venda_id) values ( nextval('itemvenda_seq'), 1, 3, 2 );
insert into itemvenda(id, quantidade, produto_id, venda_id) values ( nextval('itemvenda_seq'), 4, 4, 2 );
insert into itemvenda(id, quantidade, produto_id, venda_id) values ( nextval('itemvenda_seq'), 1, 5, 2 );

