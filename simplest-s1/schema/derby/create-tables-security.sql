--资源
create table resource (
    id              integer not null generated always as identity primary key,
    type            varchar(16) not null,
    name            varchar(64) not null,
    value           varchar(128),
    unique (type, name)
);
comment on table resource is '资源';
comment on column resource.id is 'AUTO INCREMENT';
comment on column resource.type is '类型。如URL、MENU等';
comment on column resource.name is '名称。如URL类型的新增用户、MENU类型的我的订单';
comment on column resource.value is '值。如URL类型的/user/AddUser.do、MENU类型的/order/MyOrders.js。可为null，例如菜单的非叶子节点';

--菜单（前台）。resource的一种，继承自resource。
create table menu (
    id              integer not null primary key references resource (id),
    parent_id       integer references menu (id),
    "order"         integer not null,
    is_leaf         boolean not null
);
comment on table menu is '菜单';
comment on column menu.id is 'id';
comment on column menu.parent_id is '父菜单id。根菜单无，其它须有';
comment on column menu.order is '显示顺序';
comment on column menu.is_leaf is '是否为叶子';

--权限
create table permission (
    id              integer not null generated always as identity primary key,
    name            varchar(64) not null unique,
    resource_id     integer not null references resource (id),
    operation       varchar(16) not null,
    unique (resource_id, operation)
);
comment on table permission is '权限';
comment on column permission.id is 'AUTO INCREMENT';
comment on column permission.name is '名称';
comment on column permission.resource_id is '资源id';
comment on column permission.operation is '操作。对于资源类型为URL，仅有VISIT(访问)；对于资源类型为MENU，仅有DISPLAY(前端显示)';

--角色
--我们的角色继承决定采用有限制的继承（Limited Inheritance，单继承），而非一般继承（General Inheritance，多继承）。
--因为前者的设计更单纯，因此下面表的设计中，一个角色只有一个父亲或没有。
create table role (
    id              integer not null generated always as identity primary key,
    name            varchar(64) not null unique,
    parent_id       integer references role (id)
);
comment on table role is '角色';
comment on column role.id is 'AUTO INCREMENT';
comment on column role.name is '名称';
comment on column role.parent_id is '父角色id';

--角色的权限
create table role_permission (
    role_id         integer not null references role (id),
    permission_id   integer not null references permission (id),
    unique (role_id, permission_id)
);
comment on table role_permission is '角色的权限';
comment on column role_permission.role_id is '角色id';
comment on column role_permission.permission_id is '权限id';

--用户。user在derby中为关键字，可加双引号避免报错。
create table "user" (
    id              integer not null generated always as identity primary key,
    username        varchar(32) not null unique,
    password_enc    varchar(32) not null,
    email           varchar(32) unique
);
comment on table "user" is '用户';
comment on column "user".id is 'AUTO INCREMENT';
comment on column "user".username is '用户名';
comment on column "user".password is '密码。目前MD5加密存储密码的密文。';
comment on column "user".email is '电子邮件';

--用户的角色
create table user_role (
    user_id         integer not null references "user" (id),
    role_id         integer not null references role (id),
    unique (user_id, role_id)
);
comment on table user_role is '用户的角色';
comment on column user_role.user_id is '用户id';
comment on column user_role.role_id is '角色id';