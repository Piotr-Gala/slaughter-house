INSERT INTO animal(weight, arrival_date, origin) values
                                                        (450, '2025-11-27', 'Arla'),
                                                        (653, '2025-11-27', 'Danish Crown')
on conflict do nothing;

insert into tray(id, type, max_weight) values
                                           (1,'leg', 25),
                                           (2,'rib', 20)
on conflict do nothing;

insert into part(id, weight, type, animal_id, tray_id) values
                                                           (1, 1.2, 'leg', 1, 1),
                                                           (2, 0.8, 'leg', 2, 1)
on conflict do nothing;

insert into product(id, kind) values
    (1, 0)  -- SAME_TYPE
on conflict do nothing;

insert into product_part(product_id, part_id) values
                                                  (1, 1),
                                                  (1, 2)
on conflict do nothing;

