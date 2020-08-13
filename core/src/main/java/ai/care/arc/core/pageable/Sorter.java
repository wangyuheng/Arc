package ai.care.arc.core.pageable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author junhao.chen
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Sorter {

    private String field;

    private Order order;

}
