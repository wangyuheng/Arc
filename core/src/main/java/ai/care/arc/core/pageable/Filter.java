package ai.care.arc.core.pageable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author junhao.chen
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Filter {

    private FilterOperator operator;

    private String field;

    private List<String> value;
}