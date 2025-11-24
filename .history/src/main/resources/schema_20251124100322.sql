-- ANIMALS: rejestracja + waga na recepcji
create table if not exists animal (
                                      id bigserial primary key,
                                      registration_number text not null unique,
                                      weight numeric not null  -- kg; waga całego zwierzęcia
);

-- TRAYS: każda na jeden typ części + limit wagi
create table if not exists tray (
                                    id bigserial primary key,
                                    type text not null,           -- np. 'leg','rib' itd.
                                    max_weight numeric not null   -- kg, pojemność tacy
);

-- PARTS: każda część zna wagę, typ i z jakiego zwierzęcia pochodzi; leży (opcjonalnie) na tacy
create table if not exists part (
                                    id bigserial primary key,
                                    weight numeric not null,      -- kg
                                    type text not null,
                                    animal_id bigserial not null references animal(id),
                                    tray_id bigserial references tray(id)
    -- UWAGA: reguła "na tacy tylko jeden typ" i "nie przekraczaj max_weight"
    -- będzie egzekwowana w warstwie aplikacyjnej (albo triggerami, jeśli bardzo chcesz).
);

-- PRODUCTS: paczki (sameType/halfAnimal)
create table if not exists product (
                                       id bigserial primary key,
                                       kind text not null            -- 'SAME_TYPE' | 'HALF_ANIMAL'
);

-- Powiązania produkt ↔ części
create table if not exists product_part (
                                            product_id bigserial not null references product(id) on delete cascade,
                                            part_id    bigserial not null references part(id)    on delete cascade,
                                            primary key (product_id, part_id)
);

-- Indeksy przyspieszające trace
create index if not exists idx_part_animal on part(animal_id);
create index if not exists idx_pp_product  on product_part(product_id);
create index if not exists idx_pp_part     on product_part(part_id);

-- ensure columns exist (idempotent for dev)
ALTER TABLE IF EXISTS animal
    ADD COLUMN IF NOT EXISTS arrival_date date,
    ADD COLUMN IF NOT EXISTS origin varchar(128);

-- handy indexy do zapytań z Part 3
CREATE INDEX IF NOT EXISTS idx_animal_arrival_date ON animal(arrival_date);
CREATE INDEX IF NOT EXISTS idx_animal_origin ON animal(LOWER(origin));
