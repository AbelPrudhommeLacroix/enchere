all: Menu

Menu:
	mkdir -p bin
	javac -d bin -classpath lib/ojdbc6.jar -sourcepath src src/AuctionApp.java
	java -classpath bin:lib/ojdbc6.jar AuctionApp

clean:
	rm -rf bin/*