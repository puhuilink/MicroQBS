create table t_device_relation
(
    id bigint not null
        constraint t_device_relation_pk
            primary key,
    device_id bigint,
    student_id bigint,
    create_by bigint,
    create_time timestamp,
    modify_by bigint,
    modify_time timestamp,
    deleted boolean default false
);

comment on table t_device_relation is '设备信息关系表';

comment on column t_device_relation.id is '主键';

comment on column t_device_relation.device_id is '设备id';

comment on column t_device_relation.student_id is '学生id';

comment on column t_device_relation.create_by is '创建人';

comment on column t_device_relation.create_time is '创建时间';

comment on column t_device_relation.modify_by is '操作人';

comment on column t_device_relation.modify_time is '操作时间';

comment on column t_device_relation.deleted is '是否删除';