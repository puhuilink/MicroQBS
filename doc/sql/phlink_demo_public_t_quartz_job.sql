create table t_quartz_job
(
  id              varchar(255) not null
    constraint t_quartz_job_pkey
      primary key,
  create_by       varchar(255) default NULL::character varying,
  create_time     timestamp,
  del_flag        integer,
  update_by       varchar(255) default NULL::character varying,
  update_time     timestamp,
  cron_expression varchar(255) default NULL::character varying,
  description     varchar(255) default NULL::character varying,
  job_class_name  varchar(255) default NULL::character varying,
  parameter       varchar(255) default NULL::character varying,
  status          integer
);

alter table t_quartz_job
  owner to postgres;

