package com.qianfeng.analystic.model.dim;

import com.qianfeng.analystic.model.dim.base.*;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 * @Auther: lyd
 * @Date: 2018/7/27 14:27
 * @Description:公共维度类的封装 平台和时间维度
 */
public class StatsCommonDimension extends StatsBaseDimension{

    private PlatformDimension platformDimension = new PlatformDimension();
    private DateDimension dateDimension = new DateDimension();
    private KpiDimension kpiDimension = new KpiDimension();

    public StatsCommonDimension(){

    }

    public StatsCommonDimension(PlatformDimension platformDimension, DateDimension dateDimension, KpiDimension kpiDimension) {
        this.platformDimension = platformDimension;
        this.dateDimension = dateDimension;
        this.kpiDimension = kpiDimension;
    }

    /**
     * 克隆当前对象的一个实例
     * @param dimension
     * @return
     */
    public static StatsCommonDimension clone(StatsCommonDimension dimension){
       DateDimension dateDimension = new DateDimension(dimension.dateDimension.getId(),
               dimension.dateDimension.getYear(),dimension.dateDimension.getSeason(),
               dimension.dateDimension.getMonth(),dimension.dateDimension.getWeek(),
               dimension.dateDimension.getDay(),dimension.dateDimension.getCalendar(),
               dimension.dateDimension.getType());
       PlatformDimension platformDimension = new PlatformDimension(dimension.platformDimension.getId(),
               dimension.platformDimension.getPlatformName());
       KpiDimension kpiDimension = new KpiDimension(dimension.kpiDimension.getId(),
               dimension.kpiDimension.getKpiName());
        return new StatsCommonDimension(platformDimension,dateDimension,kpiDimension);
    }

    @Override
    public void write(DataOutput out) throws IOException {
        this.dateDimension.write(out); //对象的写出
        this.platformDimension.write(out);
        this.kpiDimension.write(out);
    }

    @Override
    public void readFields(DataInput in) throws IOException {
        this.dateDimension.readFields(in);
        this.platformDimension.readFields(in);
        this.kpiDimension.readFields(in);
    }

    @Override
    public int compareTo(BaseDimension o) {

        if(o == this){
            return 0;
        }
        StatsCommonDimension other = (StatsCommonDimension) o;
        int tmp = this.dateDimension.compareTo(other.dateDimension);
        if(tmp != 0){
            return tmp;
        }
        tmp = this.platformDimension.compareTo(other.platformDimension);
        if(tmp != 0){
            return tmp;
        }
        tmp = this.kpiDimension.compareTo(other.kpiDimension);
        return tmp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        StatsCommonDimension that = (StatsCommonDimension) o;

        if (platformDimension != null ? !platformDimension.equals(that.platformDimension) : that.platformDimension != null)
            return false;
        if (dateDimension != null ? !dateDimension.equals(that.dateDimension) : that.dateDimension != null)
            return false;
        return kpiDimension != null ? kpiDimension.equals(that.kpiDimension) : that.kpiDimension == null;
    }

    @Override
    public int hashCode() {
        int result = platformDimension != null ? platformDimension.hashCode() : 0;
        result = 31 * result + (dateDimension != null ? dateDimension.hashCode() : 0);
        result = 31 * result + (kpiDimension != null ? kpiDimension.hashCode() : 0);
        return result;
    }

    public PlatformDimension getPlatformDimension() {
        return platformDimension;
    }

    public void setPlatformDimension(PlatformDimension platformDimension) {
        this.platformDimension = platformDimension;
    }

    public DateDimension getDateDimension() {
        return dateDimension;
    }

    public void setDateDimension(DateDimension dateDimension) {
        this.dateDimension = dateDimension;
    }

    public KpiDimension getKpiDimension() {
        return kpiDimension;
    }

    public void setKpiDimension(KpiDimension kpiDimension) {
        this.kpiDimension = kpiDimension;
    }
}
