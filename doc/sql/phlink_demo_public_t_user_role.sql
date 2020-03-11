create table t_user_role
(
  id          varchar(255) not null
    constraint t_user_role_pkey
      primary key,
  create_by   varchar(255) default NULL::character varying,
  create_time timestamp,
  del_flag    integer,
  update_by   varchar(255) default NULL::character varying,
  update_time timestamp,
  role_id     varchar(255) default NULL::character varying,
  user_id     varchar(255) default NULL::character varying
);

alter table t_user_role
  owner to postgres;

INSERT INTO public.t_user_role (id, create_by, create_time, del_flag, update_by, update_time, role_id, user_id) VALUES ('134933785559961600', null, '2019-04-28 22:31:02.000000', 0, null, '2019-04-28 22:31:02.000000', '496138616573953', '682265633886209');
INSERT INTO public.t_user_role (id, create_by, create_time, del_flag, update_by, update_time, role_id, user_id) VALUES ('134933785576738816', null, '2019-04-28 22:31:02.000000', 0, null, '2019-04-28 22:31:02.000000', '496138616573952', '682265633886209');
INSERT INTO public.t_user_role (id, create_by, create_time, del_flag, update_by, update_time, role_id, user_id) VALUES ('61392579396112384', null, '2018-10-08 00:04:32.000000', 0, null, '2018-10-08 00:04:32.000000', '16457350655250432', '16739222421508096');
INSERT INTO public.t_user_role (id, create_by, create_time, del_flag, update_by, update_time, role_id, user_id) VALUES ('61392637076180992', null, '2018-10-08 00:04:46.000000', 0, null, '2018-10-08 00:04:46.000000', '496138616573953', '4363087427670016');
INSERT INTO public.t_user_role (id, create_by, create_time, del_flag, update_by, update_time, role_id, user_id) VALUES ('98931727094779904', null, '2019-01-19 14:11:43.000000', 0, null, '2019-01-19 14:11:43.000000', '496138616573952', '682265633886208');