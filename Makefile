all:
	./compile.sh

clean:
	rm -r classes
	mkdir classes

test:
	./run.sh

ci:
	git commit -am "soft tabs and makefile changes"
	git push
