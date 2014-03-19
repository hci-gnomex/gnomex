use gnomex;

create temporary table tempNoAuditTable (Table_Name varchar(1000), Err varchar(100));
create temporary table tempOrphanedAuditTable (Table_Name varchar(1000), Err varchar(100));
insert into tempNoAuditTable(Table_Name, Err)
  select t1.Table_name, 'has no audit table' Err
    from INFORMATION_SCHEMA.TABLES t1
    left join INFORMATION_SCHEMA.TABLES t2 on t2.TABLE_SCHEMA = t1.TABLE_SCHEMA and t2.TABLE_NAME = concat(t1.TABLE_NAME, '_Audit')
    where t1.TABLE_SCHEMA = 'gnomex' and t1.TABLE_NAME not like '%_audit' and t1.TABLE_TYPE = 'BASE TABLE' and t2.TABLE_NAME is null;
insert into tempOrphanedAuditTable
  select t1.TABLE_NAME, 'is orphaned audit table' Err
    from INFORMATION_SCHEMA.TABLES t1
    left join INFORMATION_SCHEMA.TABLES t2 on t2.TABLE_SCHEMA = t1.TABLE_SCHEMA and concat(t2.TABLE_NAME,'_Audit') = t1.TABLE_NAME
    where t1.TABLE_NAME like '%_audit' and t2.TABLE_NAME is null AND t1.TABLE_SCHEMA = 'GNOMEX';
select c1.TABLE_NAME, c1.COLUMN_NAME, 'not in audit table or diff type' Err
  from INFORMATION_SCHEMA.COLUMNS c1
  left join INFORMATION_SCHEMA.COLUMNS c2 on c1.TABLE_SCHEMA=c2.TABLE_SCHEMA and c2.TABLE_NAME=concat(c1.TABLE_NAME,'_Audit')
      and c1.COLUMN_NAME=c2.COLUMN_NAME and c1.DATA_TYPE = c2.DATA_TYPE
      and case when c1.CHARACTER_MAXIMUM_LENGTH is null then -1 else c1.CHARACTER_MAXIMUM_LENGTH end =
        case when c2.CHARACTER_MAXIMUM_LENGTH is null then -1 else c2.CHARACTER_MAXIMUM_LENGTH end
  where c1.TABLE_SCHEMA = 'gnomex' AND c1.TABLE_NAME not like '%_audit'
    and c1.TABLE_NAME not in (select TABLE_NAME from INFORMATION_SCHEMA.TABLES where TABLE_TYPE!='BASE TABLE')
    and c1.TABLE_NAME not in (select TABLE_NAME from tempNoAuditTable)
    and c2.COLUMN_NAME is null
  order by c1.TABLE_NAME, c1.ORDINAL_POSITION;
select c1.TABLE_NAME, c1.COLUMN_NAME, 'not in normal table' Err
  from INFORMATION_SCHEMA.COLUMNS c1
  left join INFORMATION_SCHEMA.COLUMNS c2 on c1.TABLE_SCHEMA=c2.TABLE_SCHEMA and concat(c2.TABLE_NAME,'_Audit')=c1.TABLE_NAME
      and c1.COLUMN_NAME=c2.COLUMN_NAME
  where c1.TABLE_SCHEMA = 'gnomex' AND c1.TABLE_NAME like '%_audit'
    and c1.TABLE_NAME not in (select TABLE_NAME from tempOrphanedAuditTable)
    and c1.COLUMN_NAME not like 'Audit%'
    and c2.COLUMN_NAME is null
  order by c1.TABLE_NAME, c1.ORDINAL_POSITION;
select * from tempNoAuditTable;
select * from tempOrphanedAuditTable;
drop table tempNoAuditTable;
drop table tempOrphanedAuditTable;



