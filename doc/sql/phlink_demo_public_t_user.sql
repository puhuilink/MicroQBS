create table t_user
(
  id            varchar(255) not null
    constraint t_user_pkey
      primary key,
  create_by     varchar(255)  default NULL::character varying,
  create_time   timestamp,
  update_by     varchar(255)  default NULL::character varying,
  update_time   timestamp,
  address       varchar(255)  default NULL::character varying,
  avatar        varchar(1000) default NULL::character varying,
  description   varchar(255)  default NULL::character varying,
  email         varchar(255)  default NULL::character varying,
  mobile        varchar(255)  default NULL::character varying,
  nick_name     varchar(255)  default NULL::character varying,
  password      varchar(255)  default NULL::character varying,
  sex           varchar(255)  default NULL::character varying,
  status        integer,
  type          integer,
  username      varchar(255)  default NULL::character varying,
  del_flag      integer,
  department_id varchar(255)  default NULL::character varying,
  street        varchar(255)  default NULL::character varying,
  pass_strength varchar(2)    default NULL::character varying,
  realname      varchar(50)
);

alter table t_user
  owner to postgres;

INSERT INTO public.t_user (id, create_by, create_time, update_by, update_time, address, avatar, description, email, mobile, nick_name, password, sex, status, type, username, del_flag, department_id, street, pass_strength, realname) VALUES ('16739222421508096', '', '2018-06-06 18:48:02.000000', '', '2018-10-08 00:04:32.000000', '', 'https://s1.ax1x.com/2018/05/19/CcdVQP.png', '', '1012139570@qq.com', '18782059033', '', '$2a$10$PS04ecXfknNd3V8d.ymLTObQciapMU4xU8.GADBZZsuTZr7ymnagy', '男', 0, 0, 'test2', 0, '40652338142121984', '', '弱', null);
INSERT INTO public.t_user (id, create_by, create_time, update_by, update_time, address, avatar, description, email, mobile, nick_name, password, sex, status, type, username, del_flag, department_id, street, pass_strength, realname) VALUES ('4363087427670016', '', '2018-05-03 15:09:42.000000', '', '2018-10-08 00:04:46.000000', '[\"510000\",\"510100\",\"510114\"]', 'https://s1.ax1x.com/2018/05/19/CcdVQP.png', '', '1012139570@qq.com', '18782059033', '', '$2a$10$PS04ecXfknNd3V8d.ymLTObQciapMU4xU8.GADBZZsuTZr7ymnagy', '男', 0, 0, 'test', 0, '40652338142121984', '', '弱', null);
INSERT INTO public.t_user (id, create_by, create_time, update_by, update_time, address, avatar, description, email, mobile, nick_name, password, sex, status, type, username, del_flag, department_id, street, pass_strength, realname) VALUES ('682265633886209', '', '2018-04-30 23:28:42.000000', 'admin', '2019-04-28 22:31:02.000000', '', 'https://s1.ax1x.com/2018/05/19/CcdVQP.png', '', '1012@qq.com', '18782059033', '', '$2a$10$PS04ecXfknNd3V8d.ymLTObQciapMU4xU8.GADBZZsuTZr7ymnagy', '女', -1, 0, 'Exrick', 0, '40322777781112832', '', '弱', null);
INSERT INTO public.t_user (id, create_by, create_time, update_by, update_time, address, avatar, description, email, mobile, nick_name, password, sex, status, type, username, del_flag, department_id, street, pass_strength, realname) VALUES ('682265633886208', '', '2018-05-01 16:13:51.000000', 'admin', '2019-01-19 14:11:43.000000', '[\"510000\",\"510100\",\"510104\"]', 'https://s1.ax1x.com/2018/05/19/CcdVQP.png', 'test', '2549575805@qq.com', '18782059038', 'Exrick', '$2a$10$PS04ecXfknNd3V8d.ymLTObQciapMU4xU8.GADBZZsuTZr7ymnagy', '男', 0, 1, 'admin', 0, '40322777781112832', '长安街', '弱', null);