(ns speclj.report.junit_spec
  (:require [speclj.reporting :refer [report-message
                                      report-description
                                      report-pass
                                      report-pending
                                      report-fail
                                      report-runs
                                      report-error]]
            [speclj.components :refer [new-characteristic]]
            [speclj.results :refer [error-result
                                    fail-result
                                    pass-result
                                    pending-result]]
            [speclj.core :refer :all]
            [speclj.report.junit :as junit]
            [clojure.java.io :refer [as-file delete-file]]))

(defn junit-xml-as-string []
  (if (.exists (as-file "./target/junit.xml"))
    (slurp "./target/junit.xml")
    ""))

(describe "JUnit Reporter"
 (with reporter (junit/new-junit-reporter))
 (with characteristic (new-characteristic "spec name" nil))

 (it "can create a reporter"
  (should-not-be-nil @reporter))

 (it "implements speclj.reporting/Reporter protocol"
  (should (satisfies? speclj.reporting/Reporter @reporter)))

 (xit "generating xml for java system properties") ; TODO

 (describe "unused protocol methods"
  (around [it]
          (if (.exists (as-file "./target/junit.xml"))
            (delete-file "./target/junit.xml"))
          (it))

  (it "report-message"
   (let [_ (report-message @reporter nil)
         output (junit-xml-as-string)]
     (should= "" output)))

  (it "report-description"
   (let [_ (report-description @reporter nil)
         output (junit-xml-as-string)]
     (should= "" output)))

  (it "report-pass"
   (let [_ (report-pass @reporter nil)
         output (junit-xml-as-string)]
     (should= "" output)))

  (it "report-pending"
   (let [_ (report-pending @reporter nil)
         output (junit-xml-as-string)]
     (should= "" output)))

  (it "report-fail"
   (let [_ (report-fail @reporter nil)
         output (junit-xml-as-string)]
     (should= "" output)))
 )

 (describe "generating xml from speclj results"
  (around [it]
          (with-redefs [junit/properties->xml (fn [] nil)
                        junit/now (fn [] "1972-04-19T07:23:06.025Z")]
            (it)))

  (it "report-runs"
   (let [_ (report-runs @reporter [(pass-result (new-characteristic "passing spec name" nil) 1.00040)
                                   (pending-result (new-characteristic "pending spec name" nil) 1.00050 "Not yet implemented")
                                   (fail-result (new-characteristic "failing spec name" nil) 1.00060 "Typo")])
         output (junit-xml-as-string)]
     (should= "<?xml version=\"1.0\" encoding=\"UTF-8\"?><testsuites><testsuite name=\"speclj\" errors=\"0\" skipped=\"1\" tests=\"3\" failures=\"1\" time=\"3.00150\" timestamp=\"1972-04-19T07:23:06.025Z\"><testcase classname=\"passing spec name\" name=\"passing spec name\" time=\"1.00040\"></testcase><testcase classname=\"pending spec name\" name=\"pending spec name\" time=\"1.00050\"><skipped></skipped></testcase><testcase classname=\"failing spec name\" name=\"failing spec name\" time=\"1.00060\"><failure message=\"test failure\">Typo</failure></testcase></testsuite></testsuites>"
              output)))

  (it "report-error"
   (let [_ (report-error @reporter (error-result (Exception. "Kaboom")))
         output (junit-xml-as-string)]
     (should= "<?xml version=\"1.0\" encoding=\"UTF-8\"?><testsuites><testsuite name=\"speclj\" errors=\"1\" skipped=\"0\" tests=\"0\" failures=\"0\" time=\"0\" timestamp=\"1972-04-19T07:23:06.025Z\"><error>Kaboom</error></testsuite></testsuites>"
              output)))

  )
 )


