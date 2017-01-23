package has;
import has.User.User;
import java.util.List;

/**
 * Created by pc1 on 20.1.2017 г..
 */
public class DataTableResult {
    private Integer draw;
    private Integer start;
    private Integer length;
    private Integer recordsTotal;
    private Integer recordsFiltered;
    private List data;

    public DataTableResult(Integer draw, Integer start, Integer length, Integer recordsTotal, Integer recordsFiltered, List data) {
        this.draw = draw;
        this.start = start;
        this.length = length;
        this.recordsTotal = recordsTotal;
        this.recordsFiltered = recordsFiltered;
        this.data = data;
    }

    public Integer getDraw() {
        return draw;
    }

    public void setDraw(Integer draw) {
        this.draw = draw;
    }

    public Integer getStart() {
        return start;
    }

    public void setStart(Integer start) {
        this.start = start;
    }

    public Integer getLength() {
        return length;
    }

    public void setLength(Integer length) {
        this.length = length;
    }

    public Integer getRecordsTotal() {
        return recordsTotal;
    }

    public void setRecordsTotal(Integer recordsTotal) {
        this.recordsTotal = recordsTotal;
    }

    public Integer getRecordsFiltered() {
        return recordsFiltered;
    }

    public void setRecordsFiltered(Integer recordsFiltered) {
        this.recordsFiltered = recordsFiltered;
    }

    public List getData() {
        return data;
    }

    public void setData(List data) {
        this.data = data;
    }
}
