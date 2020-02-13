create table t_student
(
    id bigint,
    name varchar(20),
    school_id bigint,
    class_id bigint,
    sex char(1),
    birthday date,
    create_by bigint,
    create_time timestamp,
    modify_by bigint,
    modify_time bigint
);

comment on table t_student is '学生信息';

comment on column t_student.id is 'id';

comment on column t_student.name is '学生姓名';

comment on column t_student.school_id is '学校id';

comment on column t_student.class_id is '班级id';

comment on column t_student.sex is '性别';

comment on column t_student.birthday is '出生年月日';

comment on column t_student.create_by is '创建人';

comment on column t_student.create_time is '创建时间';

comment on column t_student.modify_by is '操作人';

comment on column t_student.modify_time is '更新时间';

