1、编写维度类和udf函数
create function ct_convert as 'com.qianfeng.analystic.hive.CurrencyTypeDimensionUdf' using jar "hdfs://hadoop01:9000/logs/udfjars/GP1706LogAnalystic-1.0.jar";
create function pt_convert as 'com.qianfeng.analystic.hive.PaymentTypeDimensionUdf' using jar "hdfs://hadoop01:9000/logs/udfjars/GP1706LogAnalystic-1.0.jar";

2、创建hive表映射每一天的数据，创建成分区表
create table if not exists dw_od(
s_time bigint,
pl string,
oid string,
cut string,
cua double,
pt string,
en string
)
partitioned by(month int,day int)
row format delimited fields terminated by ' '
stored as orc;


导入数据：
from ods_logs
insert overwrite table dw_od partition(month=7,day=27)
select s_time,pl,o_id,cut,cua,pt,en
where month = 7
and day = 27
and o_id is not null
and o_id != 'null'
;


创建临时表：用于存储每一个指标
create table if not exists dwa_od(
dtid int,
plid int,
cutid int,
ptid int,
ct double,
created string
)
partitioned by(month int,day int)
row format delimited fields terminated by ' '
stored as orc;

总的订单数：
from(
select
 from_unixtime(cast(do.s_time/1000 as bigint),"yyyy-MM-dd") dt,
 do.pl pl,
 do.cut cut,
 do.pt pt,
 count(distinct do.oid) ct
from dw_od do
where do.month = 7
and day = 27
and do.en = "e_crt"
group by from_unixtime(cast(do.s_time/1000 as bigint),"yyyy-MM-dd"),pl,cut,pt
) as tmp
insert overwrite table dwa_od partition(month=7,day=27)
select date_convert(dt),platform_convert(pl),ct_convert(cut),pt_convert(pt),sum(ct),dt
group by dt,pl,cut,pt
;

扩展的维度：
insert into table dwa_od partition(month=7,day=27)
select dtid,platform_convert('all'),cutid,ptid,sum(ct),created from dwa_od
group by dtid,cutid,ptid,created
;


sqoop 语句：
sqoop export --connect jdbc:mysql://hadoop01:3306/result \
--username root --password root --table stats_order \
--export-dir hdfs://hadoop01:9000/hive/lda.db/dwa_od/month=7/day=27/* \
--input-fields-terminated-by "\\001" --update-mode allowinsert \
--columns 'platform_dimension_id,date_dimension_id,currency_type_dimension_id,payment_type_dimension_id,orders,created' \
--update-key platform_dimension_id,date_dimension_id,currency_type_dimension_id,payment_type_dimension_id \
;









