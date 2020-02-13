-- auto-generated definition
create table t_device_repair
(
    id          bigint not null
        constraint t_device_repair_pk
            primary key,
    device_id   bigint,
    comment     varchar(200),
    create_time timestamp,
    modify_by   bigint,
    modify_time timestamp,
    create_by   bigint,
    status      char,
    deleted     boolean default false
);

comment on table t_device_repair is '设备维修表';

comment on column t_device_repair.id is '设备维修id';

comment on column t_device_repair.device_id is '设备id';

comment on column t_device_repair.comment is '维修原因';

comment on column t_device_repair.create_time is '创建时间';

comment on column t_device_repair.modify_by is '操作人';

comment on column t_device_repair.modify_time is '操作时间';

comment on column t_device_repair.create_by is '创建人';

comment on column t_device_repair.status is '维修状态';

comment on column t_device_repair.deleted is '是否删除';

alter table t_device_repair
    owner to postgres;

