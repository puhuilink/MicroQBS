create table t_department
(
  id          varchar(255) not null
    constraint t_department_pkey
      primary key,
  create_by   varchar(255)   default NULL::character varying,
  create_time timestamp,
  del_flag    integer,
  update_by   varchar(255)   default NULL::character varying,
  update_time timestamp,
  parent_id   varchar(255)   default NULL::character varying,
  sort_order  numeric(10, 2) default NULL::numeric,
  status      integer,
  title       varchar(255)   default NULL::character varying,
  is_parent   boolean        default true
);

alter table t_department
  owner to postgres;

INSERT INTO public.t_department (id, create_by, create_time, del_flag, update_by, update_time, parent_id, sort_order, status, title, is_parent) VALUES ('40322777781112832', '', '2018-08-10 20:40:40.000000', 0, '', '2018-08-11 00:03:06.000000', '0', 1.00, 0, '总部', true);
INSERT INTO public.t_department (id, create_by, create_time, del_flag, update_by, update_time, parent_id, sort_order, status, title, is_parent) VALUES ('40322811096469504', '', '2018-08-10 20:40:48.000000', 0, '', '2019-03-14 18:50:44.000000', '40322777781112832', 1.00, 0, '技术部', true);
INSERT INTO public.t_department (id, create_by, create_time, del_flag, update_by, update_time, parent_id, sort_order, status, title, is_parent) VALUES ('40343262766043136', '', '2018-08-10 22:02:04.000000', 0, '', '2018-08-11 00:02:53.000000', '0', 2.00, 0, '成都分部', true);
INSERT INTO public.t_department (id, create_by, create_time, del_flag, update_by, update_time, parent_id, sort_order, status, title, is_parent) VALUES ('40652270295060480', '', '2018-08-11 18:29:57.000000', 0, '', '2018-08-12 18:45:01.000000', '0', 3.00, 0, '人事部', true);
INSERT INTO public.t_department (id, create_by, create_time, del_flag, update_by, update_time, parent_id, sort_order, status, title, is_parent) VALUES ('40389030113710080', '', '2018-08-11 01:03:56.000000', 0, '', '2018-08-11 17:50:04.000000', '40343262766043136', 1.00, 0, 'JAVA', false);
INSERT INTO public.t_department (id, create_by, create_time, del_flag, update_by, update_time, parent_id, sort_order, status, title, is_parent) VALUES ('40652338142121984', null, '2018-08-11 18:30:13.000000', 0, null, '2018-08-11 18:30:13.000000', '40652270295060480', 1.00, 0, '游客', false);
INSERT INTO public.t_department (id, create_by, create_time, del_flag, update_by, update_time, parent_id, sort_order, status, title, is_parent) VALUES ('40681289119961088', '', '2018-08-11 20:25:16.000000', 0, '', '2018-08-11 22:47:48.000000', '40652270295060480', 2.00, 0, 'VIP', false);
INSERT INTO public.t_department (id, create_by, create_time, del_flag, update_by, update_time, parent_id, sort_order, status, title, is_parent) VALUES ('40322852833988608', '', '2018-08-10 20:40:58.000000', 0, '', '2018-08-11 01:29:42.000000', '40322811096469504', 1.00, 0, '研发中心', null);
INSERT INTO public.t_department (id, create_by, create_time, del_flag, update_by, update_time, parent_id, sort_order, status, title, is_parent) VALUES ('40327204755738624', '', '2018-08-10 20:58:15.000000', 0, '', '2018-08-10 22:02:15.000000', '40322811096469504', 2.00, 0, '大数据', null);
INSERT INTO public.t_department (id, create_by, create_time, del_flag, update_by, update_time, parent_id, sort_order, status, title, is_parent) VALUES ('40327253309001728', '', '2018-08-10 20:58:27.000000', 0, '', '2018-08-11 17:26:38.000000', '40322811096469504', 3.00, -1, '人工智障', null);
INSERT INTO public.t_department (id, create_by, create_time, del_flag, update_by, update_time, parent_id, sort_order, status, title, is_parent) VALUES ('40344005342400512', '', '2018-08-10 22:05:01.000000', 0, '', '2018-08-11 17:48:44.000000', '40343262766043136', 2.00, 0, 'Vue', null);