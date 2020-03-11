create table t_file
(
  id          varchar(255) not null
    constraint t_file_pkey
      primary key,
  create_by   varchar(255) default NULL::character varying,
  create_time timestamp,
  del_flag    integer,
  update_by   varchar(255) default NULL::character varying,
  update_time timestamp,
  name        varchar(255) default NULL::character varying,
  size        bigint,
  type        varchar(255) default NULL::character varying,
  url         varchar(255) default NULL::character varying,
  f_key       varchar(255) default NULL::character varying,
  location    integer
);

alter table t_file
  owner to postgres;

