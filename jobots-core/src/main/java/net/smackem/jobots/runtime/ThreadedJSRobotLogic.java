package net.smackem.jobots.runtime;

import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.HostAccess;
import org.graalvm.polyglot.Value;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

public class ThreadedJSRobotLogic implements RobotLogic, AutoCloseable {

    private static final String lang = "js";
    private static final Logger log = LoggerFactory.getLogger(ThreadedJSRobotLogic.class);
    private final String source;
    private final Thread thread;
    private final LogicBus bus;
    private volatile boolean acceptingInput;

    public ThreadedJSRobotLogic(String source, String sourceName) {
        this.source = source;
        this.bus = new LogicBus();
        this.thread = new Thread(this::run, sourceName + "-thread");
        this.thread.start();
    }

    private void run() {
        final Context context = Context.newBuilder(lang)
                .allowHostAccess(HostAccess.ALL)
                .allowHostClassLookup(className -> className.startsWith("net.smackem.jobots"))
                .build();
        final Value outputType = context.eval(lang, "Java.type('%s')".formatted(RobotLogic.Output.class.getName()));
        final Value vectorType = context.eval(lang, "Java.type('%s')".formatted(Vector.class.getName()));
        final Value bindings = context.getBindings(lang);
        bindings.putMember("bus", this.bus);
        bindings.putMember("Output", outputType);
        bindings.putMember("Vector", vectorType);
        bindings.putMember("log", log);
        try (context) {
            final Value func = context.parse(lang, this.source);
            this.acceptingInput = true;
            final Value result = func.execute();
            log.info("JS returned {}", result);
        } catch (Exception e) {
            log.info("JS execution broke with exception", e);
        }
    }

    @Override
    public void offerInput(Input input) {
        if (this.acceptingInput == false) {
            return;
        }
        if (this.bus.inputQueue.offer(input) == false) {
            log.warn("offering input to {} failed, queue is full", this.thread.getName());
        }
    }

    @Override
    public Output pollOutput() {
        return this.bus.outputQueue.poll();
    }

    @Override
    public void close() {
        this.thread.interrupt();
        try {
            this.thread.join(1000);
        } catch (InterruptedException e) {
            log.warn("interrupted join on thread {}", this.thread.getName());
        }
    }

    public static class LogicBus {
        private final BlockingQueue<Input> inputQueue = new ArrayBlockingQueue<>(5);
        private final BlockingQueue<Output> outputQueue = new ArrayBlockingQueue<>(1);

        public Input poll() throws InterruptedException {
            log.debug("poll input queue (len={})", this.inputQueue.size());
            return this.inputQueue.poll(1, TimeUnit.MINUTES);
        }

        public void offer(Output output) throws InterruptedException {
            log.debug("offer to output queue (len={})", this.outputQueue.size());
            this.outputQueue.offer(output, 1, TimeUnit.SECONDS);
        }
    }
}
