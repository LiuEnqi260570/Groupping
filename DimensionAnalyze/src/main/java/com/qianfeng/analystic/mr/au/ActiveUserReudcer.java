package com.qianfeng.analystic.mr.au;

import com.qianfeng.analystic.model.dim.StatsUserDimension;
import com.qianfeng.analystic.model.dim.value.map.TimeOutputValue;
import com.qianfeng.analystic.model.dim.value.reduce.MapWritableValue;
import com.qianfeng.common.DateEnum;
import com.qianfeng.common.KpiType;
import com.qianfeng.util.TimeUtil;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.MapWritable;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @Auther: lyd
 * @Date: 2018/7/27 15:13
 * @Description:活跃用户的reducer
 */
public class ActiveUserReudcer extends Reducer<StatsUserDimension,TimeOutputValue,
        StatsUserDimension,MapWritableValue>{
    private Set<String> unique = new HashSet<String>();
    private MapWritableValue v = new MapWritableValue();

    //按小时统计的集合
    private Map<Integer,Set<String>> hourlyMap = new HashMap<Integer, Set<String>>();
    private MapWritable hourlyMapWritable = new MapWritable();


    /**
     * 初始化
     * @param context
     * @throws IOException
     * @throws InterruptedException
     */
    @Override
    protected void setup(Context context) throws IOException, InterruptedException {
        //循环初始化
        for (int i = 0;i<24;i++){
            hourlyMap.put(i,new HashSet<String>());
            hourlyMapWritable.put(new IntWritable(i),new IntWritable(0));
        }
    }

    @Override
    protected void reduce(StatsUserDimension key, Iterable<TimeOutputValue> values, Context context) throws IOException, InterruptedException {

        try{
            //判断事件是哪一个的
            String kpi = key.getStatsCommonDimension().getKpiDimension().getKpiName();
            if(kpi.equals(KpiType.ACTIVE_USER)){
                //循环map阶段传过来的value
                for (TimeOutputValue tv:values) {
                    //将uuid取出来添加到set中
                    this.unique.add(tv.getId());
                    int hour = TimeUtil.getDateInfo(tv.getTime(), DateEnum.HOUR);
                    hourlyMap.get(hour).add(tv.getId());
                }
                //构建输出的value
                this.v.setKpi(KpiType.HOURLY_ACTIVE_USER);
                //循环赋值
                for (Map.Entry<Integer,Set<String>> en:hourlyMap.entrySet()){
                    this.hourlyMapWritable.put(new IntWritable(en.getKey()),
                            new IntWritable(en.getValue().size()));
                }
                this.v.setValue(hourlyMapWritable);
                //输出
                context.write(key,this.v);

                //针对以前普通的活跃用户的输出
                //构建输出的value
                MapWritable mapWritable = new MapWritable();
                mapWritable.put(new IntWritable(-1),new IntWritable(this.unique.size()));

                this.v.setValue(mapWritable);
                //还需要设置kpi
                this.v.setKpi(KpiType.valueOfType(key.getStatsCommonDimension().getKpiDimension().getKpiName()));
                //输出即可
                context.write(key,this.v);

            } else {
                //循环map阶段传过来的value
                for (TimeOutputValue tv:values) {
                    //将uuid取出来添加到set中
                    this.unique.add(tv.getId());
                }

                //构建输出的value
                MapWritable mapWritable = new MapWritable();
                mapWritable.put(new IntWritable(-1),new IntWritable(this.unique.size()));

                this.v.setValue(mapWritable);
                //还需要设置kpi
                this.v.setKpi(KpiType.valueOfType(key.getStatsCommonDimension().getKpiDimension().getKpiName()));
                //输出即可
                context.write(key,this.v);
            }
        } finally {
            this.unique.clear();
            this.hourlyMap.clear();
            this.hourlyMapWritable.clear();
            //循环初始化
            for (int i = 0;i<24;i++){
                hourlyMap.put(i,new HashSet<String>());
                hourlyMapWritable.put(new IntWritable(i),new IntWritable(0));
            }
        }





       /* try{
            //判断事件是哪一个的
            String kpi = key.getStatsCommonDimension().getKpiDimension().getKpiName();
            if(kpi.equals(KpiType.HOURLY_ACTIVE_USER.kpiName)){
                //循环map阶段传过来的value
                for (TimeOutputValue tv:values) {
                    int hour = TimeUtil.getDateInfo(tv.getTime(), DateEnum.HOUR);
                    hourlyMap.get(hour).add(tv.getId());
                }
                //构建输出的value
                this.v.setKpi(KpiType.HOURLY_ACTIVE_USER);
                //循环赋值
                for (Map.Entry<Integer,Set<String>> en:hourlyMap.entrySet()){
                    this.hourlyMapWritable.put(new IntWritable(en.getKey()),
                            new IntWritable(en.getValue().size()));
                }
                this.v.setValue(hourlyMapWritable);
                //输出
                context.write(key,this.v);

            } else {
                //循环map阶段传过来的value
                for (TimeOutputValue tv:values) {
                    //将uuid取出来添加到set中
                    this.unique.add(tv.getId());
                }

                //构建输出的value
                MapWritable mapWritable = new MapWritable();
                mapWritable.put(new IntWritable(-1),new IntWritable(this.unique.size()));

                this.v.setValue(mapWritable);
                //还需要设置kpi
                this.v.setKpi(KpiType.valueOfType(key.getStatsCommonDimension().getKpiDimension().getKpiName()));
                //输出即可
                context.write(key,this.v);
            }
        } finally {
            this.unique.clear();
            this.hourlyMap.clear();
            this.hourlyMapWritable.clear();
            //循环初始化
            for (int i = 0;i<24;i++){
                hourlyMap.put(i,new HashSet<String>());
                hourlyMapWritable.put(new IntWritable(i),new IntWritable(0));
            }
        }*/

    }
}
