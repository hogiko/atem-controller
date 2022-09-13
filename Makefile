#

DEV: install dev

RUN: install uber run

install:
	mvn clean install

dev:
	mvn -DDEBUG=true quarkus:dev

run:
	java -jar -DDEBUG=true target/atem-controller-1.0-SNAPSHOT-runner.jar

build: install uber

uber:
	mvn -Dquarkus.package.type=uber-jar package

push:
	cp target/atem-controller-1.0-SNAPSHOT-runner.jar /home/joerg/NextCloud/feg-koblenz.de/Diakonat\ Gottesdienst/Video/TallyLights/
