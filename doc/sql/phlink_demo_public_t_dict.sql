create table t_dict
(
  id          varchar(255) not null
    constraint t_dict_pkey
      primary key,
  create_by   varchar(255)   default NULL::character varying,
  create_time timestamp,
  del_flag    integer,
  update_by   varchar(255)   default NULL::character varying,
  update_time timestamp,
  description varchar(255)   default NULL::character varying,
  title       varchar(255)   default NULL::character varying,
  type        varchar(255)   default NULL::character varying,
  sort_order  numeric(10, 2) default NULL::numeric
);

alter table t_dict
  owner to postgres;

INSERT INTO public.t_dict (id, create_by, create_time, del_flag, update_by, update_time, description, title, type, sort_order) VALUES ('75135930788220928', 'admin', '2018-11-14 22:15:43.000000', 0, 'admin', '2018-11-27 01:39:06.000000', '', '性别', 'sex', 0.00);
INSERT INTO public.t_dict (id, create_by, create_time, del_flag, update_by, update_time, description, title, type, sort_order) VALUES ('75388696739713024', 'admin', '2018-11-15 15:00:07.000000', 0, 'admin', '2018-11-27 01:39:22.000000', '', '按钮权限类型', 'permission_type', 3.00);