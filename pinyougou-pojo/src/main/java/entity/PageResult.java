package entity;

import java.io.Serializable;
import java.util.List;

/**
 * @author wangyangkun
 * @date 2019/7/9 0009 16:06
 */
public class PageResult<T> implements Serializable {
    private long total;
    private List<T> rows;


    @Override
    public String toString() {
        return "PageResult{" +
                "total=" + total +
                ", rows=" + rows +
                '}';
    }

    public PageResult(long total, List<T> rows) {
        this.total = total;
        this.rows = rows;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public List<T> getRows() {
        return rows;
    }

    public void setRows(List<T> rows) {
        this.rows = rows;
    }
}
