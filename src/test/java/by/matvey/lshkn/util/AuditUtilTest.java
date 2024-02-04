package by.matvey.lshkn.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;

class AuditUtilTest {

    @Test
    void writesAudit() {
        AuditUtil.setAuditInfo(new ArrayList<>());
        int prevSize = AuditUtil.getAuditInfo().size();

        AuditUtil.write("test");

        assertThat(prevSize).isLessThan(AuditUtil.getAuditInfo().size());
    }

    @Test
    void writesAuditIfAuditInfoListIsNull() {
        AuditUtil.setAuditInfo(null);

        AuditUtil.write("test");

        assertThat(AuditUtil.getAuditInfo()).hasSize(1);
    }
}