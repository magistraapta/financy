create table users (
    id bigserial primary key,
    username varchar(255) not null,
    password varchar(255) not null,
    created_at timestamp not null default now(),
    updated_at timestamp not null default now()
);

insert into users (username, password) values ('user', 'user');

create type transaction_type as enum ('INCOME', 'EXPENSE');

create table transactions (
    id bigserial primary key,
    user_id bigint not null,
    amount decimal(10, 2) not null,
    type transaction_type not null,
    created_at timestamp not null default now(),
    updated_at timestamp not null default now()
);

alter table transactions add constraint fk_transactions_users foreign key (user_id) references users(id);