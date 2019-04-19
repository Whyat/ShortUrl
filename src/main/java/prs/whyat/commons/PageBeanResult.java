package prs.whyat.commons;

import java.util.List;

/**
 * @ClassName: Test
 * @Description: TODO
 * @Author: Caoxin
 * @Date:Created in 16:32 2019/3/25
 */
public class PageBeanResult<T> {
    Integer limit;
    Integer total;
    Integer status;
    String msg;
    List<T> dataList;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public List<T> getDataList() {
        return dataList;
    }

    public void setDataList(List<T> dataList) {
        this.dataList = dataList;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }
}
