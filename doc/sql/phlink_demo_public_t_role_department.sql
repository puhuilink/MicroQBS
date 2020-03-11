create table t_role_department
(
  id            varchar(255) not null
    constraint t_role_department_pkey
      primary key,
  create_by     varchar(255) default NULL::character varying,
  create_time   timestamp,
  del_flag      integer,
  update_by     varchar(255) default NULL::character varying,
  update_time   timestamp,
  department_id varchar(255) default NULL::character varying,
  role_id       varchar(255) default NULL::character varying
);

alter table t_role_department
  owner to postgres;

INSERT INTO public.t_role_department (id, create_by, create_time, del_flag, update_by, update_time, department_id, role_id) VALUES ('70763874256687105', 'admin', '2018-11-02 20:42:43.000000', 0, 'admin', '2018-11-02 20:42:43.000000', '40322777781112832', '16457350655250432');
INSERT INTO public.t_role_department (id, create_by, create_time, del_flag, update_by, update_time, department_id, role_id) VALUES ('70763874265075712', 'admin', '2018-11-02 20:42:43.000000', 0, 'admin', '2018-11-02 20:42:43.000000', '40322811096469504', '16457350655250432');
INSERT INTO public.t_role_department (id, create_by, create_time, del_flag, update_by, update_time, department_id, role_id) VALUES ('70763874277658624', 'admin', '2018-11-02 20:42:43.000000', 0, 'admin', '2018-11-02 20:42:43.000000', '40322852833988608', '16457350655250432');