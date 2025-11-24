insert into animal(id, registration_number, weight) values
                                                        (1,'AN-001', 612.5),
                                                        (2,'AN-002', 587.3)
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
    (1, 'SAME_TYPE')
on conflict do nothing;

insert into product_part(product_id, part_id) values
                                                  (1, 1),
                                                  (1, 2)
on conflict do nothing;

