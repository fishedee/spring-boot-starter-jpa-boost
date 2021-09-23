INSERT INTO contact (id, name, is_customer, is_suppiler, is_delete, contact_category_id, contact_category_path, contact_category_name, remark) VALUES (1001, 'A客户', 1, 0, 0, 1004, '1001_1004', '佛山', '');
INSERT INTO contact (id, name, is_customer, is_suppiler, is_delete, contact_category_id, contact_category_path, contact_category_name, remark) VALUES (1002, 'B供应商', 0, 1, 1, 1005, '1001_1005', '广州', '');
INSERT INTO contact (id, name, is_customer, is_suppiler, is_delete, contact_category_id, contact_category_path, contact_category_name, remark) VALUES (1003, 'C客户', 1, 0, 0, 1007, '1002_1007', '梧州', '');
INSERT INTO contact (id, name, is_customer, is_suppiler, is_delete, contact_category_id, contact_category_path, contact_category_name, remark) VALUES (1004, 'D合作伙伴', 1, 1, 0, 1006, '1002_1006', '桂林', '');

INSERT INTO contact_phones (contact_id, phones_order, name, phone) VALUES (1002, 0, 'B_mm', '2336-12');
INSERT INTO contact_phones (contact_id, phones_order, name, phone) VALUES (1003, 0, 'C_dog', '678');
INSERT INTO contact_phones (contact_id, phones_order, name, phone) VALUES (1004, 0, 'D_fish', '123');
INSERT INTO contact_phones (contact_id, phones_order, name, phone) VALUES (1004, 1, 'D_cat', '456');
