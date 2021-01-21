package net.smackem.jobots.runtime;

import org.junit.Test;

import javax.script.ScriptException;

import static org.assertj.core.api.Assertions.assertThat;

public class RobotTest {
    @Test
    public void testIt() throws ScriptException, NoSuchMethodException {
        assertThat(Integer.valueOf(1)).isNotNull();
        Robot.runScript();
    }
}
