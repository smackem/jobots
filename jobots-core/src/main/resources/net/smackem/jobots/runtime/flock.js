while (true) {
    let input = bus.poll();
    bus.offer(new Output(new Vector(10, 10)));
}