create table t_log
(
  id            varchar(255) not null
    constraint t_log_pkey
      primary key,
  create_by     varchar(255) default NULL::character varying,
  create_time   timestamp,
  del_flag      integer,
  update_by     varchar(255) default NULL::character varying,
  update_time   timestamp,
  cost_time     integer,
  ip            varchar(255) default NULL::character varying,
  ip_info       varchar(255) default NULL::character varying,
  name          varchar(255) default NULL::character varying,
  request_param varchar(255) default NULL::character varying,
  request_type  varchar(255) default NULL::character varying,
  request_url   varchar(255) default NULL::character varying,
  username      varchar(255) default NULL::character varying,
  log_type      integer
);

alter table t_log
  owner to postgres;

INSERT INTO public.t_log (id, create_by, create_time, del_flag, update_by, update_time, cost_time, ip, ip_info, name, request_param, request_type, request_url, username, log_type) VALUES ('245780528496644096', null, null, 0, null, null, 41, '127.0.0.1', '', '登录系统', '{"password":"******","code":"Xd3E","saveLogin":"true","captchaId":"86566a06d222417cb250fe94a8dcdbc3","username":"test"}', 'POST', '/login', 'test', 1);
INSERT INTO public.t_log (id, create_by, create_time, del_flag, update_by, update_time, cost_time, ip, ip_info, name, request_param, request_type, request_url, username, log_type) VALUES ('245781232137277440', null, null, 0, null, null, 39, '127.0.0.1', '0|0|0|内网IP|内网IP', '登录系统', '{"password":"******","code":"hxre","saveLogin":"true","captchaId":"f39944d1896d424d96c3d9185b124307","username":"test"}', 'POST', '/login', 'test', 1);
INSERT INTO public.t_log (id, create_by, create_time, del_flag, update_by, update_time, cost_time, ip, ip_info, name, request_param, request_type, request_url, username, log_type) VALUES ('245812731393871872', null, '2020-02-28 21:44:23.174000', 0, null, null, 41, '127.0.0.1', '0|0|0|内网IP|内网IP', '登录系统', '{"password":"******","code":"n2oe","saveLogin":"true","captchaId":"fb20b7de00ad4b01afd6ee677492ccbb","username":"test"}', 'POST', '/login', 'test', 1);
INSERT INTO public.t_log (id, create_by, create_time, del_flag, update_by, update_time, cost_time, ip, ip_info, name, request_param, request_type, request_url, username, log_type) VALUES ('248328367080738816', null, '2020-03-06 20:20:37.458000', 0, null, null, 39, '127.0.0.1', '0|0|0|内网IP|内网IP', '登录系统', '{"password":"******","code":"zwty","saveLogin":"true","captchaId":"1323f512af7143feba53c832ad62abe8","username":"test"}', 'POST', '/login', 'test', 1);
INSERT INTO public.t_log (id, create_by, create_time, del_flag, update_by, update_time, cost_time, ip, ip_info, name, request_param, request_type, request_url, username, log_type) VALUES ('248328590372900864', null, '2020-03-06 20:21:30.678000', 0, null, null, 17, '127.0.0.1', '0|0|0|内网IP|内网IP', '登录系统', '{"password":"******","code":"2wn3","saveLogin":"true","captchaId":"81dd6960818749efb051776e6cb2a59d","username":"test"}', 'POST', '/login', 'test', 1);
INSERT INTO public.t_log (id, create_by, create_time, del_flag, update_by, update_time, cost_time, ip, ip_info, name, request_param, request_type, request_url, username, log_type) VALUES ('248329579171680256', null, '2020-03-06 20:25:26.447000', 0, null, null, 46, '127.0.0.1', '0|0|0|内网IP|内网IP', '登录系统', '{"password":"******","code":"ufsd","saveLogin":"true","captchaId":"095fe1c716624d8db281eb5a33d5422d","username":"test"}', 'POST', '/login', 'test', 1);
INSERT INTO public.t_log (id, create_by, create_time, del_flag, update_by, update_time, cost_time, ip, ip_info, name, request_param, request_type, request_url, username, log_type) VALUES ('248329810638540800', null, '2020-03-06 20:26:21.613000', 0, null, null, 19, '127.0.0.1', '0|0|0|内网IP|内网IP', '登录系统', '{"password":"******","code":"zekx","saveLogin":"true","captchaId":"0b5c35e2eef44c91b1390a8846eeb2e2","username":"test"}', 'POST', '/login', 'test', 1);
INSERT INTO public.t_log (id, create_by, create_time, del_flag, update_by, update_time, cost_time, ip, ip_info, name, request_param, request_type, request_url, username, log_type) VALUES ('249652692555665408', null, '2020-03-10 12:03:01.274000', 0, null, null, 59, '127.0.0.1', '0|0|0|内网IP|内网IP', '登录系统', '{"password":"******","code":"jmls","saveLogin":"true","captchaId":"c88baddaf4ec4d37bacaab4f97a16ab3","username":"test"}', 'POST', '/login', 'test', 1);
INSERT INTO public.t_log (id, create_by, create_time, del_flag, update_by, update_time, cost_time, ip, ip_info, name, request_param, request_type, request_url, username, log_type) VALUES ('250045843149688832', null, '2020-03-11 14:05:15.687000', 0, null, null, 70, '127.0.0.1', '0|0|0|内网IP|内网IP', '登录系统', '{"password":"******","code":"zmio","saveLogin":"true","captchaId":"a0d5f661cce044e9a3e26d0eb03f17e7","username":"test"}', 'POST', '/login', 'test', 1);