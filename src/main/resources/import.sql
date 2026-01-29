INSERT INTO tb_category(name, description, ativo) VALUES ('Abrasivos', 'Materiais para desbaste ou corte', true);
INSERT INTO tb_category(name, description, ativo) VALUES ('Identificação', 'Materiais para identificar peças', true);
INSERT INTO tb_category(name, description, ativo) VALUES ('Solda', 'Materiais para soldador', true);

INSERT INTO tb_material (name, unit_Of_Measure, current_Stock, minimum_Stock, ativo, category_id) VALUES ('Disco de desbaste 7 polegadas', 'UN', 5, 1, true, 1);
INSERT INTO tb_material (name, unit_Of_Measure, current_Stock, minimum_Stock, ativo, category_id) VALUES ('Luva de raspa', 'UN', 40, 10, true, 1);
INSERT INTO tb_material (name, unit_Of_Measure, current_Stock, minimum_Stock, ativo, category_id) VALUES ('Óculos de proteção', 'UN', 25, 5, true, 2);
INSERT INTO tb_material (name, unit_Of_Measure, current_Stock, minimum_Stock, ativo, category_id) VALUES ('Máscara de solda automática', 'UN', 3, 1, true, 1);
INSERT INTO tb_material (name, unit_Of_Measure, current_Stock, minimum_Stock, ativo, category_id) VALUES ('Eletrodo E6013 3,25mm', 'UN', 120, 30, true, 1);
