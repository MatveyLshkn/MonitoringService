package by.matvey.lshkn.util;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;

class LoggerUtilTest {

    @Test
    void addsLog() {
        LoggerUtil.setLogInfo(new ArrayList<>());
        int prevSize = LoggerUtil.getLogInfo().size();

        LoggerUtil.log("test");

        assertThat(prevSize).isLessThan(LoggerUtil.getLogInfo().size());
    }

    @Test
    void addsLogIfLogInfoListIsNull() {
        LoggerUtil.setLogInfo(null);

        LoggerUtil.log("test");

        assertThat(LoggerUtil.getLogInfo()).hasSize(1);
    }
}