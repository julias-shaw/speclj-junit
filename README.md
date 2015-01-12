# speclj-growl

speclj-junit is a plugin for [speclj](http://speclj.com/) that formats output using ant's junit xml format. This format is commonly used by continuous integration tools.

## Installation

Add the following to your project.clj under the :dev profile:

    :dependencies [[speclj-junit "0.0.9"]]

Speclj 2.7.x or later is required.

## Usage

Add `-f junit` to lein spec to output spec results in Ant's [JUnitReport task](https://ant.apache.org/manual/Tasks/junitreport.html) format.

    lein spec -f junit

This will write a junit.xml file to target that is suitable for integration with Jenkins and such.

## TODO

* Make output file configurable

## Thanks

Many thanks to Paul Gross for his [speclj-growl](https://github.com/pgr0ss/speclj-growl) project for a great example of writing speclj plugins.

## License

Copyright (C) 2015 Julias Shaw

Distributed under the The MIT License.
