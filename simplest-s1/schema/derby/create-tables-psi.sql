--商品
create table goods (
    id              integer not null generated always as identity primary key,
    name            varchar(32) not null unique
);
COMMENT ON TABLE goods IS '商品';
COMMENT ON COLUMN goods.id IS 'AUTO INCREMENT';
COMMENT ON COLUMN goods.name IS '名称';

--采购单
create table po (
    id              integer not null generated always as identity primary key,
    "no"            varchar(16) not null unique,
    vendor          varchar(64) not null,
    amount          numeric(10, 2) not null,
    created_time    timestamp not null,
    created_by      integer not null references "user" (id),
    status          smallint not null
);
COMMENT ON TABLE po IS '采购单';
COMMENT ON COLUMN po.id IS 'AUTO INCREMENT';
COMMENT ON COLUMN po.no IS '单号';
COMMENT ON COLUMN po.vendor IS '供应商';
COMMENT ON COLUMN po.amount IS '金额';
COMMENT ON COLUMN po.created_time IS '创建时间';
COMMENT ON COLUMN po.created_by IS '创建人ID';
COMMENT ON COLUMN po.status IS '状态。1:有效；0:删除';

--采购单条目
create table po_item (
    id              integer not null generated always as identity primary key,
    po_id           integer not null references po (id),
    goods_id        integer not null references goods (id),
    quantity        integer not null,
    unit_cost       numeric(10, 2) not null
);
COMMENT ON TABLE po_item IS '采购单条目';
COMMENT ON COLUMN po_item.id IS 'AUTO INCREMENT';
COMMENT ON COLUMN po_item.po_id IS '采购单ID';
COMMENT ON COLUMN po_item.goods_id IS '商品ID';
COMMENT ON COLUMN po_item.quantity IS '数量';
COMMENT ON COLUMN po_item.unit_cost IS '单个成本价';

--销售单
create table so (
    id              integer not null generated always as identity primary key,
    "no"            varchar(16) not null unique,
    customer        varchar(64) not null,
    salesperson     varchar(32) not null,
    amount          numeric(10, 2) not null,
    created_time    timestamp not null,
    created_by      integer not null references "user" (id),
    status          smallint not null
);
COMMENT ON TABLE so IS '销售单';
COMMENT ON COLUMN so.id IS 'AUTO INCREMENT';
COMMENT ON COLUMN so.no IS '单号';
COMMENT ON COLUMN so.customer IS '客户';
COMMENT ON COLUMN so.salesperson IS '销售人员';
COMMENT ON COLUMN so.amount IS '金额';
COMMENT ON COLUMN so.created_time IS '创建时间';
COMMENT ON COLUMN so.created_by IS '创建人ID';
COMMENT ON COLUMN so.status IS '状态。1:有效；0:删除';

--销售单条目
create table so_item (
    id              integer not null generated always as identity primary key,
    so_id           integer not null references so (id),
    goods_id        integer not null references goods (id),
    quantity        integer not null,
    unit_price      numeric(10, 2) not null
);
COMMENT ON TABLE so_item IS '销售单条目';
COMMENT ON COLUMN so_item.id IS 'AUTO INCREMENT';
COMMENT ON COLUMN so_item.so_id IS '销售单ID';
COMMENT ON COLUMN so_item.goods_id IS '商品ID';
COMMENT ON COLUMN so_item.quantity IS '数量';
COMMENT ON COLUMN so_item.unit_price IS '单个售价';

--入库单
create table inventory_receipt (
    id             integer not null generated always as identity primary key,
    "no"           varchar(16) not null unique,
    reference_type integer,
    reference_no   varchar(16),
    created_time   timestamp not null,
    created_by     integer not null references "user" (id),
    received_time  timestamp,
    received_by    integer references "user" (id),
    status         smallint not null
);
COMMENT ON TABLE inventory_receipt IS '入库单';
COMMENT ON COLUMN inventory_receipt.id IS 'AUTO INCREMENT';
COMMENT ON COLUMN inventory_receipt.no IS '单号';
COMMENT ON COLUMN inventory_receipt.reference_type IS '参考类型。1:采购';
COMMENT ON COLUMN inventory_receipt.reference_no IS '参考号。对采购，记录采购单号';
COMMENT ON COLUMN inventory_receipt.created_time IS '创建时间';
COMMENT ON COLUMN inventory_receipt.created_by IS '创建人ID';
COMMENT ON COLUMN inventory_receipt.received_time IS '入库时间';
COMMENT ON COLUMN inventory_receipt.received_by IS '入库人ID';
COMMENT ON COLUMN inventory_receipt.status IS '状态。1:初始状态；2:已入库；0:删除';

--入库单条目
create table inventory_receipt_item (
    id                   integer not null generated always as identity primary key,
    inventory_receipt_id integer not null references inventory_receipt (id),
    goods_id             integer not null references goods (id),
    quantity             integer not null
);
COMMENT ON TABLE inventory_receipt_item IS '入库单条目';
COMMENT ON COLUMN inventory_receipt_item.id IS 'AUTO INCREMENT';
COMMENT ON COLUMN inventory_receipt_item.inventory_receipt_id IS '入库单ID';
COMMENT ON COLUMN inventory_receipt_item.goods_id IS '商品ID';
COMMENT ON COLUMN inventory_receipt_item.quantity IS '数量';

--出库单
create table inventory_issue (
    id             integer not null generated always as identity primary key,
    "no"           varchar(16) not null unique,
    reference_type integer,
    reference_no   varchar(16),
    created_time   timestamp not null,
    created_by     integer not null references "user" (id),
    issued_time    timestamp,
    issued_by      integer references "user" (id),
    status         smallint not null
);
COMMENT ON TABLE inventory_issue IS '出库单';
COMMENT ON COLUMN inventory_issue.id IS 'AUTO INCREMENT';
COMMENT ON COLUMN inventory_issue.no IS '单号';
COMMENT ON COLUMN inventory_issue.reference_type IS '参考类型。1:销售';
COMMENT ON COLUMN inventory_issue.reference_no IS '参考号。对销售，记录销售单号';
COMMENT ON COLUMN inventory_issue.created_time IS '创建时间';
COMMENT ON COLUMN inventory_issue.created_by IS '创建人ID';
COMMENT ON COLUMN inventory_issue.issued_time IS '出库时间';
COMMENT ON COLUMN inventory_issue.issued_by IS '出库人ID';
COMMENT ON COLUMN inventory_issue.status IS '状态。1:初始状态；2:已出库；0:删除';

--出库单条目
create table inventory_issue_item (
    id                 integer not null generated always as identity primary key,
    inventory_issue_id integer not null references inventory_issue (id),
    goods_id           integer not null references goods (id),
    quantity           integer not null
);
COMMENT ON TABLE inventory_issue_item IS '出库单条目';
COMMENT ON COLUMN inventory_issue_item.id IS 'AUTO INCREMENT';
COMMENT ON COLUMN inventory_issue_item.inventory_issue_id IS '出库单ID';
COMMENT ON COLUMN inventory_issue_item.goods_id IS '商品ID';
COMMENT ON COLUMN inventory_issue_item.quantity IS '数量';

--库存
create table inventory_stock (
    id              integer not null generated always as identity primary key,
    goods_id        integer not null references goods (id),
    quantity        integer not null
);
COMMENT ON TABLE inventory_stock IS '库存';
COMMENT ON COLUMN inventory_stock.id IS 'AUTO INCREMENT';
COMMENT ON COLUMN inventory_stock.goods_id IS '商品ID';
COMMENT ON COLUMN inventory_stock.quantity IS '数量';
