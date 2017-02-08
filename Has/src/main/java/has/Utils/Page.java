package has.Utils;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by kaloi on 2/1/2017.
 */
public class Page<T> extends PageRequest {

    protected Collection<T> objects;
    private Long totalCount;
    private Long pageCount;
    private Boolean hasPageLinkPrev;
    private Boolean hasPageLinkNext;
    private Collection<Long> pageLinks;

    public Page(Long page, Integer limit, Long totalCount, Collection<T> objects) {

        this.page = page;
        this.limit = limit;
        this.totalCount = totalCount;
        this.objects = objects;

        this.pageCount = totalCount / limit;
        if (totalCount % limit > 0) {
            this.pageCount = this.pageCount + 1;
        }

        this.hasPageLinkPrev = page > 1;
        this.hasPageLinkNext = page < this.pageCount;

        this.pageLinks = new ArrayList<>();
        if (this.pageCount != 1) {
            this.pageLinks.add(1l);
            if (page > 3l) {
                this.pageLinks.add(-1l);
            }
            if (page > 2l) {
                if (page.equals(this.pageCount) && this.pageCount > 3l) {
                    this.pageLinks.add(page - 2l);
                }
                this.pageLinks.add(page - 1l);
            }
            if (page != 1l && !page.equals(this.pageCount)) {
                this.pageLinks.add(page);
            }
            if (page < this.pageCount - 1l) {
                this.pageLinks.add(page + 1l);

                if (page == 1l && this.pageCount > 3l) {
                    this.pageLinks.add(page + 2l);
                }
            }
            if (page < this.pageCount - 2l) {
                this.pageLinks.add(-1l);
            }
            this.pageLinks.add(this.pageCount);
        }
    }

    public Page(PageRequest pageRequest, Long totalCount, Collection<T> objects) {
        this(pageRequest.getPage(), pageRequest.getLimit(), totalCount, objects);
    }

    public Long getTotalCount() {
        return this.totalCount;
    }

    public Long getPageCount() {
        return this.pageCount;
    }

    public Long getPage() {
        return this.page;
    }

    public Integer getLimit() {
        return this.limit;
    }

    public Boolean getHasPageLinkPrev() {
        return this.hasPageLinkPrev;
    }

    public Boolean getHasPageLinkNext() {
        return hasPageLinkNext;
    }

    public Collection<Long> getPageLinks() {
        return pageLinks;
    }

    public Collection<T> getObjects() {
        return objects;
    }
}