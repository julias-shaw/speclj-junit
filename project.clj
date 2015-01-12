(defproject speclj-junit "0.0.6"
  :description "JUnit xml reporter for the speclj testing framework"
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [org.clojure/data.xml "0.0.8"]
                 [clj-time "0.9.0"]]
  :url "https://github.com/julias-shaw/speclj-junit"
  :license {:name "MIT License"
            :url "http://opensource.org/licenses/MIT"}
  :profiles {
    :dev {
      :plugins [[speclj "2.7.2"]]
      :dependencies [[speclj "2.7.2"]
                     [speclj-junit "0.0.6"]]
      :test-paths ["spec/"]}})
