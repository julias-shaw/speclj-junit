(defproject speclj-junit "0.0.11-SNAPSHOT"
  :description "JUnit xml reporter for the speclj testing framework"
  :url "https://github.com/julias-shaw/speclj-junit"
  :scm {:name "git"
        :url "https://github.com/julias-shaw/speclj-junit"}
  :license {:name "MIT License"
            :url "http://opensource.org/licenses/MIT"}
  :signing {:gpg-key "D5935FCC"}
  :deploy-repositories [["clojars" {:creds :gpg}]]

  :dependencies [[org.clojure/clojure "1.5.1"]
                 [org.clojure/data.xml "0.0.8"]
                 [clj-time "0.9.0"]]
  :profiles {
    :dev {
      :plugins [[speclj "2.7.2"]]
      :dependencies [[speclj "2.7.2"]
                     [speclj-junit "0.0.10"]]
      :test-paths ["spec/"]}})
