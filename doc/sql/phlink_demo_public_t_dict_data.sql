create table t_dict_data
(
  id          varchar(255) not null
    constraint t_dict_data_pkey
      primary key,
  create_by   varchar(255)   default NULL::character varying,
  create_time timestamp,
  del_flag    integer,
  update_by   varchar(255)   default NULL::character varying,
  update_time timestamp,
  description varchar(255)   default NULL::character varying,
  dict_id     varchar(255)   default NULL::character varying,
  sort_order  numeric(10, 2) default NULL::numeric,
  status      integer,
  title       varchar(255)   default NULL::character varying,
  value       varchar(255)   default NULL::character varying
);

alter table t_dict_data
  owner to postgres;

INSERT INTO public.t_dict_data (id, create_by, create_time, del_flag, update_by, update_time, description, dict_id, sort_order, status, title, value) VALUES ('75158227712479232', 'admin', '2018-11-14 23:44:19.000000', 0, 'admin', '2019-04-28 22:15:11.000000', '', '75135930788220928', 0.00, 0, '男', '男');
INSERT INTO public.t_dict_data (id, create_by, create_time, del_flag, update_by, update_time, description, dict_id, sort_order, status, title, value) VALUES ('75159254272577536', 'admin', '2018-11-14 23:48:24.000000', 0, 'admin', '2019-04-28 22:15:17.000000', '', '75135930788220928', 1.00, 0, '女', '女');
INSERT INTO public.t_dict_data (id, create_by, create_time, del_flag, update_by, update_time, description, dict_id, sort_order, status, title, value) VALUES ('75159898425397248', 'admin', '2018-11-14 23:50:57.000000', 0, 'admin', '2019-04-28 22:15:22.000000', '', '75135930788220928', 2.00, -1, '保密', '保密');
INSERT INTO public.t_dict_data (id, create_by, create_time, del_flag, update_by, update_time, description, dict_id, sort_order, status, title, value) VALUES ('75390787835138048', 'admin', '2018-11-15 15:08:26.000000', 0, 'admin', '2018-11-15 15:08:26.000000', '', '75388696739713024', 0.00, 0, '查看操作(view)', 'view');
INSERT INTO public.t_dict_data (id, create_by, create_time, del_flag, update_by, update_time, description, dict_id, sort_order, status, title, value) VALUES ('75390886501945344', 'admin', '2018-11-15 15:08:49.000000', 0, 'admin', '2018-11-15 15:08:57.000000', '', '75388696739713024', 1.00, 0, '添加操作(add)', 'add');
INSERT INTO public.t_dict_data (id, create_by, create_time, del_flag, update_by, update_time, description, dict_id, sort_order, status, title, value) VALUES ('75390993939042304', 'admin', '2018-11-15 15:09:15.000000', 0, 'admin', '2018-11-15 15:09:15.000000', '', '75388696739713024', 2.00, 0, '编辑操作(edit)', 'edit');
INSERT INTO public.t_dict_data (id, create_by, create_time, del_flag, update_by, update_time, description, dict_id, sort_order, status, title, value) VALUES ('75391067532300288', 'admin', '2018-11-15 15:09:32.000000', 0, 'admin', '2018-11-15 15:09:32.000000', '', '75388696739713024', 3.00, 0, '删除操作(delete)', 'delete');
INSERT INTO public.t_dict_data (id, create_by, create_time, del_flag, update_by, update_time, description, dict_id, sort_order, status, title, value) VALUES ('75391126902673408', 'admin', '2018-11-15 15:09:46.000000', 0, 'admin', '2018-11-15 15:09:46.000000', '', '75388696739713024', 4.00, 0, '清空操作(clear)', 'clear');
INSERT INTO public.t_dict_data (id, create_by, create_time, del_flag, update_by, update_time, description, dict_id, sort_order, status, title, value) VALUES ('75391192883269632', 'admin', '2018-11-15 15:10:02.000000', 0, 'admin', '2018-11-15 15:10:02.000000', '', '75388696739713024', 5.00, 0, '启用操作(enable)', 'enable');
INSERT INTO public.t_dict_data (id, create_by, create_time, del_flag, update_by, update_time, description, dict_id, sort_order, status, title, value) VALUES ('75391251024711680', 'admin', '2018-11-15 15:10:16.000000', 0, 'admin', '2018-11-15 15:10:16.000000', '', '75388696739713024', 6.00, 0, '禁用操作(disable)', 'disable');
INSERT INTO public.t_dict_data (id, create_by, create_time, del_flag, update_by, update_time, description, dict_id, sort_order, status, title, value) VALUES ('75391297124306944', 'admin', '2018-11-15 15:10:27.000000', 0, 'admin', '2018-11-15 15:10:27.000000', '', '75388696739713024', 7.00, 0, '搜索操作(search)', 'search');
INSERT INTO public.t_dict_data (id, create_by, create_time, del_flag, update_by, update_time, description, dict_id, sort_order, status, title, value) VALUES ('75391343379091456', 'admin', '2018-11-15 15:10:38.000000', 0, 'admin', '2018-11-15 15:10:38.000000', '', '75388696739713024', 8.00, 0, '上传文件(upload)', 'upload');
INSERT INTO public.t_dict_data (id, create_by, create_time, del_flag, update_by, update_time, description, dict_id, sort_order, status, title, value) VALUES ('75391407526776832', 'admin', '2018-11-15 15:10:53.000000', 0, 'admin', '2018-11-15 15:10:53.000000', '', '75388696739713024', 9.00, 0, '导出操作(output)', 'output');
INSERT INTO public.t_dict_data (id, create_by, create_time, del_flag, update_by, update_time, description, dict_id, sort_order, status, title, value) VALUES ('75391475042488320', 'admin', '2018-11-15 15:11:09.000000', 0, 'admin', '2018-11-15 15:11:09.000000', '', '75388696739713024', 10.00, 0, '导入操作(input)', 'input');
INSERT INTO public.t_dict_data (id, create_by, create_time, del_flag, update_by, update_time, description, dict_id, sort_order, status, title, value) VALUES ('75391522182270976', 'admin', '2018-11-15 15:11:21.000000', 0, 'admin', '2018-11-15 15:11:21.000000', '', '75388696739713024', 11.00, 0, '分配权限(editPerm)', 'editPerm');
INSERT INTO public.t_dict_data (id, create_by, create_time, del_flag, update_by, update_time, description, dict_id, sort_order, status, title, value) VALUES ('75391576364290048', 'admin', '2018-11-15 15:11:34.000000', 0, 'admin', '2018-11-15 15:11:34.000000', '', '75388696739713024', 12.00, 0, '设为默认(setDefault)', 'setDefault');
INSERT INTO public.t_dict_data (id, create_by, create_time, del_flag, update_by, update_time, description, dict_id, sort_order, status, title, value) VALUES ('75391798033256448', 'admin', '2018-11-15 15:12:26.000000', 0, 'admin', '2018-11-15 15:12:26.000000', '', '75388696739713024', 13.00, 0, '其他操作(other)', 'other');