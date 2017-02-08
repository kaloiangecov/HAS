package has.Utils;

/**
 * Created by kaloi on 2/1/2017.
 */
public class PageRequest {
    protected Long page = 1l;// 1 is the first page
    protected Integer limit = 10;

    public PageRequest() {

    }

    public PageRequest(Long page, Integer limit) {
        this.limit = limit;
        this.page = page;
    }

    public Long getPage() {
        return page;
    }

    public Integer getLimit() {
        return limit;
    }

    public Long getOffset() {
        return (page - 1l) * limit;
    }
}