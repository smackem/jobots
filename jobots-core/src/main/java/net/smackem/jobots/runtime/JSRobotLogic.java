package net.smackem.jobots.runtime;

import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.HostAccess;
import org.graalvm.polyglot.Value;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JSRobotLogic implements RobotLogic, AutoCloseable {
    private static final String lang = "js";
    private static final Logger log = LoggerFactory.getLogger(ThreadedJSRobotLogic.class);
    private final Value jsFunc;
    private final Context context;
    private Output output;

    public JSRobotLogic(String source, String sourceName) {
        this.context = Context.newBuilder(lang)
                .allowHostAccess(HostAccess.ALL)
                .allowHostClassLookup(className -> className.startsWith("net.smackem.jobots"))
                .build();
        final Value outputType = context.eval(lang, "Java.type('%s')".formatted(RobotLogic.Output.class.getName()));
        final Value vectorType = context.eval(lang, "Java.type('%s')".formatted(Vector.class.getName()));
        final Value bindings = context.getBindings(lang);
        bindings.putMember("Output", outputType);
        bindings.putMember("Vector", vectorType);
        bindings.putMember("log", log);
        this.jsFunc = context.parse(lang, source);
    }

    @Override
    public void offerInput(Input input) {
        final Value bindings = context.getBindings(lang);
        bindings.putMember("input", input);
        final Value output = this.jsFunc.execute();
        if (output != null) {
            this.output = output.asHostObject();
        } else {
            this.output = null;
        }
        log.info("JS returned {}", output);
    }

    @Override
    public Output pollOutput() {
        return this.output;
    }

    @Override
    public void close() {
        this.context.close();
    }
}
