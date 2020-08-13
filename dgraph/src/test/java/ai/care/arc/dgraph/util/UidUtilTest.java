package ai.care.arc.dgraph.util;

import org.junit.Test;

import java.util.Random;
import java.util.UUID;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class UidUtilTest {

    @Test
    public void should_return_true_when_input_hex() {
        int rand = new Random().nextInt(Integer.MAX_VALUE);
        String uid = "0x"+ Integer.toHexString(rand);
        assertTrue(UidUtil.isUid(uid));
    }

    @Test
    public void should_return_true_when_input_illegal_hex() {
        String uid = "0xg";
        assertFalse(UidUtil.isUid(uid));
    }

    @Test
    public void should_return_false_when_input_uuid() {
        assertFalse(UidUtil.isUid(UUID.randomUUID().toString()));
    }

    @Test
    public void should_return_false_when_input_null() {
        assertFalse(UidUtil.isUid(null));
    }
}