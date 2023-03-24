INSERT INTO category (id, is_active, name, name_id)
VALUES (1, 1, 'Test_category1', 'cat1'),
       (2, 1, 'Test_category2', 'cat2'),
       (3, 1, 'Test_category3', 'cat3'),
       (4, 0, 'Test_category4', 'cat4'),
       (5, 1, 'Test_category5', 'cat5');

INSERT INTO banner (id, name, text, price, is_active)
VALUES (1, 'Banner1','first banner', 1.3, 1),
       (2, 'Banner2','second banner', 2.6, 1),
       (3, 'Banner3','third banner', 3.9, 1),
       (4, 'Banner4','expensive but deleted banner', 99.9, 0);

INSERT INTO category_banner (banner_id, category_id)
VALUES (1, 1),
       (1, 2),
       (1, 3),
       (2, 1),
       (2, 2),
       (3, 1);

INSERT INTO record (id, user_agent, banner_id, date, ip_adress, no_content_reason, price)
VALUES (1, 'agent', 2, now() - interval 1 day , '000', '', 2.6);

INSERT INTO record_category_ids (record_id, category_ids)
VALUES (1, 1),
       (1, 2);