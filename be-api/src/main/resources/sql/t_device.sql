create table t_device
(
    id             bigint not null
		constraint t_device_pk
			primary key,
    device_name    varchar(100),
    device_code    varchar(100),
    device_type    char(1),
    create_by      bigint,
    create_time    timestamp,
    modify_by      bigint,
    modify_time    timestamp,
    deleted        boolean default false,
    binding_status char(1)
);

comment on table t_device is '设备信息表';

comment on column t_device.id is '主键';

comment on column t_device.device_name is '设备名称';

comment on column t_device.device_code is '设备标识码';

comment on column t_device.device_type is '设备类型';

comment on column t_device.create_by is '创建人';

comment on column t_device.create_time is '创建时间';

comment on column t_device.modify_by is '操作人';

comment on column t_device.modify_time is '操作时间';

comment on column t_device.deleted is '是否删除';

comment on column t_device.binding_status is '绑定状态';

