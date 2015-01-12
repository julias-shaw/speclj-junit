# speclj-growl

speclj-junit is a plugin for [speclj](http://speclj.com/) that formats output using ant's junit xml format. This format is commonly used by continuous integration tools.

## Installation

If you use [leiningen](https://github.com/technomancy/leiningen), add the following to your project.clj under the :dev profile:

    :dependencies [[speclj-junit "0.0.1"]]

As of version 2.1.0, speclj 2.7.x is required.

## Usage

Add `-f junit` to lein spec to show output in growl. For example, this will start autotest with both terminal and growl output:

    lein spec -f junit

## License

Copyright (C) 2015 Julias Shaw

Distributed under the The MIT License.
