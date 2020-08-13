package ai.care.arc.core.pageable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


/**
 * @author junhao.chen
 * @date 2020/6/15
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QueryBody {

    private Sorter sorter;
    private Pagination pagination;
    private List<Filter> filters;
    private String searchText;
    private List<String> searchFields;
}
