package ai.care.arc.dgraph.repository;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SimpleDgraphRepositoryTest {

    @Test
    public void upsert_extract_uid_var(){
        String sql = "{var(func: eq(STATISTICS.key, 123)) { v AS uid }}";
        String tmp = sql.split("(?i) as uid")[0];
        String uidVar = tmp.substring(tmp.lastIndexOf(' ')).trim();
        assertEquals("v", uidVar);
    }
}