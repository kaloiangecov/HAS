package has.Utils;

import com.mysema.query.jpa.JPQLQuery;

import java.util.List;

/**
 * Created by kaloi on 2/1/2017.
 */
public class QueryToPageExecutor {

    public  <T> Page<T> getPage(JPQLQuery query, PageRequest pageRequest) {

        List<T> resultList = (List<T>) query
                .offset(pageRequest.getOffset())
                .limit(pageRequest.getLimit())
                .fetch();

        Long totalCount = query.fetch().count();
        return new Page<T>(pageRequest, totalCount, resultList);
    }
}
