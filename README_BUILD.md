How to compile a new version of SQLiteJDBC
==========================================

Prerequisites
-------------

1.	JDK 1.5
2.	Perl
3.	Maven
4.	make
5.	gcc
6.	curl
7.	unzip
8.	git
9.	docker

Build
-----

1.	Edit the `VERSION` file and set the SQLite version to use corresponding to one of the github tags of https://github.com/Willena/libsqlite3-wx-see.git ( current is 3.x.x )
2.	Edit the version number in `pom.xml` to match `VERSION`.
3.	Then, run:

	```
	$ make
	```

How to build pure-java library
==============================

***The pure-java library is no longer supported as of version 3.7.15.https://bitbucket.org/xerial/sqlite-jdbc/issue/10/dropping-pure-java-support***

-	Use Mac OS X or Linux with gcc-3.x

	```
	make purejava
	```

-	The build will fail due to the broken regex libray, so copy the non-corrupted archive I downloaded:

	```
	$ cp archive/regex3.8a.tar.gz target/build/nestedvm-2009-08-09/upstream/downlolad/
	```

-	then do

	```
	'make purejava'
	```

(for deployer only) How to build pure-java and native libraries
===============================================================

```
    make -fMakefile.package
```
