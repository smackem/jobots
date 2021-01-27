const prefDistance = 120;
const minDistanceFromWall = 50;

function vectorStr(v) {
    return v.x() + ";" + v.y();
}

let input;
while (input = bus.poll()) {
    let position = input.position();
    let others = [];
    for (let otherPos of input.neighbours()) {
        others.push({
            "distance": Vector.distance(otherPos, position),
            "position": otherPos
        });
    }
    others.sort(function(a, b) {
        return a.distance - b.distance;
    });
    log.debug("others = {}", others);
    let speed = null;
    let nearest = others[0];
    if (nearest.distance > prefDistance) {
        speed = nearest.position.subtract(position);
    } else if (nearest.distance < prefDistance) {
        if (!nearest.distance) {
            speed = new Vector(Math.random() * 10, Math.random() * 10);
        } else {
            speed = nearest.position
                .subtract(position)
                .negate()
                .normalize()
                .multiplyWith(prefDistance - nearest.distance);
        }
    }
    if (position.x() < minDistanceFromWall / 2) {
        speed = new Vector(minDistanceFromWall - position.x(), 0);
    } else if (position.x() > input.boardDimensions().x() - minDistanceFromWall / 2) {
        speed = new Vector(-(minDistanceFromWall - (input.boardDimensions().x() - position.x())), 0);
    }
    if (position.y() < minDistanceFromWall / 2) {
        speed = new Vector(0, minDistanceFromWall - position.y());
    } else if (position.y() > input.boardDimensions().y() - minDistanceFromWall / 2) {
        speed = new Vector(0, -(minDistanceFromWall - (input.boardDimensions().y() - position.y())));
    }
    if (speed != null) {
        log.debug("speed = {}", vectorStr(speed));
        bus.offer(new Output(speed));
    }
}