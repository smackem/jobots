package net.smackem.jobots.runtime;

import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.HostAccess;
import org.graalvm.polyglot.Value;

import javax.script.ScriptException;

public class Robot {

    public Robot() throws ScriptException, NoSuchMethodException {
    }

    public static class Location {
        private final int x;
        private final int y;

        public Location() {
            this.x = 0;
            this.y = 0;
        }

        public Location(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public int x() {
            return this.x;
        }

        public int y() {
            return this.y;
        }
    }

    public static class Critter {

        public Critter() {}

        public Object locations() {
            return new Location[] {
                    new Location(100, 200),
                    new Location(101, 201),
            };
        }
    }

    public static void runScript() throws ScriptException, NoSuchMethodException {
        final String js = "js";
        final Context context = Context.newBuilder(js)
                .allowHostAccess(HostAccess.ALL)
                .allowHostClassLookup(className -> true)
                .build();
        final Value bindings = context.getBindings(js);
        bindings.putMember("critter", new Critter());
        try (context) {
            final Value result = context.eval("js", """
                    var locations = critter.locations();
                    var x = [];
                    for (let location of locations) {
                        print(location);
                        x.push({
                            x: location.x(),
                            y: location.y()
                        });
                    }
                    x;
                    """);
            System.out.printf("%s\n", result);
        }
    }
}
