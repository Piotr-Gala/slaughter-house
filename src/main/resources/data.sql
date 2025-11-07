insert into animal(id, registration_number, weight) values
                                                        ('11111111-1111-1111-1111-111111111111','AN-001', 612.5),
                                                        ('22222222-2222-2222-2222-222222222222','AN-002', 587.3)
on conflict do nothing;

insert into tray(id, type, max_weight) values
                                           ('aaaa1111-0000-0000-0000-000000000000','leg', 25),
                                           ('bbbb2222-0000-0000-0000-000000000000','rib', 20)
on conflict do nothing;

insert into part(id, weight, type, animal_id, tray_id) values
                                                           ('aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa', 1.2, 'leg', '11111111-1111-1111-1111-111111111111', 'aaaa1111-0000-0000-0000-000000000000'),
                                                           ('bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb', 0.8, 'leg', '22222222-2222-2222-2222-222222222222', 'aaaa1111-0000-0000-0000-000000000000')
on conflict do nothing;

insert into product(id, kind) values
    ('99999999-9999-9999-9999-999999999999', 'SAME_TYPE')
on conflict do nothing;

insert into product_part(product_id, part_id) values
                                                  ('99999999-9999-9999-9999-999999999999', 'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa'),
                                                  ('99999999-9999-9999-9999-999999999999', 'bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb')
on conflict do nothing;

