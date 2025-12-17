create table projects
(
    id          uuid primary key,
    name        varchar(100) not null unique,
    description varchar(200),
    created_at  timestamptz  not null
);

create table environments
(
    id         uuid primary key,
    project_id uuid         not null references projects (id) on delete cascade,
    name       varchar(100) not null,
    created_at timestamptz  not null,
    unique (project_id, name)
);