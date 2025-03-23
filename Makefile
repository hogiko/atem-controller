#

DEBUG_OPTS="-agentlib:jdwp=transport=dt_socket,server=y,suspend=y,address=5005"

DEV: install dev

RUN: install uber run

install:
	mvn clean install

dev:
	mvn -DDEBUG=true quarkus:dev

run:
	java -jar -DDEBUG=true target/tally-controller-1.0.0-SNAPSHOT-runner.jar

debug:
	java -jar $(DEBUG_OPTS) target/tally-controller-1.0.0-SNAPSHOT-runner.jar


uber:
	mvn -Dquarkus.package.type=uber-jar package

push:
	cp target/tally-controller-1.0.0-SNAPSHOT-runner.jar /home/joerg/NextCloud/feg-koblenz.de/Diakonat\ Gottesdienst/Video/TallyLights/
