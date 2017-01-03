(defproject speclj-junit "0.0.11"
  :description "JUnit XML reporter for the speclj testing framework"
  :url "https://github.com/julias-shaw/speclj-junit"
  :scm {:name "git"
        :url "https://github.com/julias-shaw/speclj-junit"}
  :license {:name "MIT License"
            :url "http://opensource.org/licenses/MIT"}
  :signing {:gpg-key "D5935FCC"}
  :deploy-repositories [["clojars" {:creds :gpg}]]

  :dependencies [[org.clojure/clojure  "1.8.0"]
                 [org.clojure/data.xml "0.0.8"]
                 [clj-time             "0.13.0"]]
  :profiles {
    :dev {
      :plugins [[speclj       "3.3.2"]
                [lein-ancient "0.6.10"]]
      :dependencies [[speclj       "3.3.2"]
                     [speclj-junit "0.0.10"]]
      :test-paths ["spec/"]}})
