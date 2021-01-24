package net.smackem.jobots.runtime;

import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.HostAccess;
import org.graalvm.polyglot.Value;
import org.junit.Test;

import javax.script.ScriptException;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class RobotTest {
    @Test
    public void testIt() {
        assertThat(Integer.valueOf(1)).isNotNull();
    }

    @Test
    public void runScript() {
        final String js = "js";
        final Context context = Context.newBuilder(js)
                .allowHostAccess(HostAccess.ALL)
                .allowHostClassLookup(className -> className.startsWith("net.smackem.jobots"))
                .build();
        final Value bindings = context.getBindings(js);
        bindings.putMember("input", new RobotController.Input(
                new Vector(100, 200),
                new Vector(1000, 800),
                List.of(new Vector(20, 10), new Vector(400, 30))));
        try (context) {
            final Value result = context.eval(js, """
                    var locations = input.neighbours();
                    var Output = Java.type('net.smackem.jobots.runtime.RobotController.Output');
                    var Vector = Java.type('net.smackem.jobots.runtime.Vector');
                    var x = [];
                    for (let location of locations) {
                        print(location);
                        x.push({
                            x: location.x(),
                            y: location.y()
                        });
                    }
                    [x, new Output(new Vector(11, 22))];
                    """);
            System.out.printf("%s\n", result);
        }
    }
}
