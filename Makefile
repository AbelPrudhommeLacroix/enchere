all: Menu

Menu:
	mkdir -p bin
	javac -d bin -classpath lib/ojdbc6.jar -sourcepath src src/HCInterface.java
	java -classpath bin:lib/ojdbc6.jar HCInterface

clean:
	rm -rf bin/*