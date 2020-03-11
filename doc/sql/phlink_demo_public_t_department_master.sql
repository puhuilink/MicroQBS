create table t_department_master
(
  id            varchar(255) not null
    constraint t_department_header_pkey
      primary key,
  create_by     varchar(255) default NULL::character varying,
  create_time   timestamp,
  del_flag      integer,
  update_by     varchar(255) default NULL::character varying,
  update_time   timestamp,
  department_id varchar(255) default NULL::character varying,
  type          integer,
  user_id       varchar(255) default NULL::character varying
);

alter table t_department_master
  owner to postgres;

INSERT INTO public.t_department_master (id, create_by, create_time, del_flag, update_by, update_time, department_id, type, user_id) VALUES ('118575966346809344', null, '2019-03-14 19:10:54.000000', 0, null, '2019-03-14 19:10:54.000000', '40322777781112832', 0, '682265633886209');