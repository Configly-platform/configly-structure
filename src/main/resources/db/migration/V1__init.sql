create table projects
(
    id          uuid primary key,
    name        varchar(100) not null unique,
    description varchar(200),
    created_at  timestamp    not null,
    updated_at  timestamp    not null,
    status      varchar(10) not null,
    revision    bigint not null default 0
);

create table environments
(
    id         uuid primary key,
    project_id uuid         not null references projects (id) on delete cascade,
    name       varchar(100) not null,
    created_at  timestamp    not null,
    updated_at  timestamp    not null,
    status      varchar(10) not null,
    type        varchar(10) not null,
    revision    bigint not null default 0,
    unique (project_id, name)
);