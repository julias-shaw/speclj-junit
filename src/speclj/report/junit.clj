(ns speclj.report.junit
  (:require [speclj.reporting]
            [speclj.platform :refer [format-seconds]]
            [speclj.report.progress :refer [full-name]]
            [speclj.reporting :refer [tally-time]]
            [speclj.results]
            [clojure.string :refer [trim-newline]]
            [clojure.pprint :refer [pprint]]
            [clojure.data.xml :as xml]
            [clj-time.core :as t])
  (:import [speclj.results PassResult FailResult PendingResult]))

(def pass-count (atom 0))
(def fail-count (atom 0))
(def skipped-count (atom 0))

(defn- clear-atom [a]
  (swap! a (fn [_] 0)))

(defn- reset-counters []
  (clear-atom pass-count)
  (clear-atom fail-count)
  (clear-atom skipped-count))

(defn- test-count [] (+ @pass-count @fail-count @skipped-count))

(defn properties->xml []
  (xml/element :properties {}
               (for [p-name (sort (.stringPropertyNames (System/getProperties)))]
                 (xml/element :property {:name p-name :value (System/getProperty p-name)}))))

(defn now []
  (t/now))

(defn- pass->xml [result]
  (swap! pass-count inc)
  (let [characteristic (.-characteristic result)
        spec-name (full-name characteristic)
        seconds (format-seconds (.-seconds result))]
    (xml/element :testcase {:classname spec-name :name spec-name :time seconds})))

(defn- pending->xml [result]
  (swap! skipped-count inc)
  (let [characteristic (.-characteristic result)
        spec-name (full-name characteristic)
        seconds (format-seconds (.-seconds result))
        exception (.-exception result)]
    (xml/element :testcase {:classname spec-name :name spec-name :time seconds}
                 (xml/element :skipped))))

(defn- fail->xml [result]
  (swap! fail-count inc)
  (let [characteristic (.-characteristic result)
        spec-name (full-name characteristic)
        seconds (format-seconds (.-seconds result))
        failure (.-failure result)]
    (xml/element :testcase {:classname spec-name :name spec-name :time seconds}
                 (xml/element :failure {:message "test failure"} failure))))

(defn- result->xml [result]
  (cond
   (= speclj.results.PassResult (type result))
   (pass->xml result)

   (= speclj.results.PendingResult (type result))
   (pending->xml result)

   (= speclj.results.FailResult (type result))
   (fail->xml result)

   :else
   (println (str "Unknown result type: " (type result)))))

(defn- runs->xml [results]
  (reset-counters)
  (let [xml-results (doall (map result->xml results))]
    (xml/element :testsuites {}
                 (xml/element :testsuite {:name "speclj"
                                          :errors 0
                                          :skipped @skipped-count
                                          :tests (test-count)
                                          :failures @fail-count
                                          :time (format-seconds (tally-time results))
                                          :timestamp (now)}
                              (properties->xml)
                              xml-results))))

(defn- error->xml [exception]
  (let [exception (.-exception exception)
                      message (.getMessage exception)]
    (xml/element :testsuites {}
                 (xml/element :testsuite {:name "speclj"
                                          :errors 1
                                          :skipped 0
                                          :tests 0
                                          :failures 0
                                          :time 0
                                          :timestamp (now)}
                              (properties->xml)
                              (xml/element :error {} message)))))

(deftype JUnitReporter [passes fails results]
  speclj.reporting/Reporter
  (report-message [this message])
  (report-description [this description])
  (report-pass [this result])
  (report-pending [this result])
  (report-fail [this result])
  (report-runs [this results]
               (with-open [out-file (java.io.OutputStreamWriter. (java.io.FileOutputStream. "./target/junit.xml") "UTF-8")]
                 (xml/emit (runs->xml results) out-file)))
  (report-error [this exception]
               (with-open [out-file (java.io.OutputStreamWriter. (java.io.FileOutputStream. "./target/junit.xml") "UTF-8")]
                 (xml/emit (error->xml exception) out-file))))

(defn new-junit-reporter []
  (JUnitReporter. (atom 0) (atom 0) (atom nil)))
