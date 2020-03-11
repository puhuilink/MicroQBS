create table t_role
(
  id           varchar(255) not null
    constraint t_role_pkey
      primary key,
  create_by    varchar(255) default NULL::character varying,
  create_time  timestamp,
  update_by    varchar(255) default NULL::character varying,
  update_time  timestamp,
  name         varchar(255) default NULL::character varying,
  del_flag     integer,
  description  varchar(255) default NULL::character varying,
  data_type    integer,
  default_role boolean
);

alter table t_role
  owner to postgres;

INSERT INTO public.t_role (id, create_by, create_time, update_by, update_time, name, del_flag, description, data_type, default_role) VALUES ('16457350655250432', '', '2018-06-06 00:08:00.000000', 'admin', '2018-11-02 20:42:24.000000', 'ROLE_TEST', 0, '测试权限按钮显示', 1, null);
INSERT INTO public.t_role (id, create_by, create_time, update_by, update_time, name, del_flag, description, data_type, default_role) VALUES ('496138616573952', '', '2018-04-22 23:03:49.000000', 'admin', '2018-11-15 23:02:59.000000', 'ROLE_ADMIN', 0, '超级管理员 拥有所有权限', 0, null);
INSERT INTO public.t_role (id, create_by, create_time, update_by, update_time, name, del_flag, description, data_type, default_role) VALUES ('496138616573953', '', '2018-05-02 21:40:03.000000', 'admin', '2018-11-01 22:59:48.000000', 'ROLE_USER', 0, '普通注册用户 路过看看', 0, true);