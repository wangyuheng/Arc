package ai.care.arc.core.pageable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author junhao.chen
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Pagination implements Serializable {

    private Integer pageNum;

    private Integer pageSize;
}
