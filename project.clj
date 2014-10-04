(defproject timetotal "0.1.0-SNAPSHOT"
  :description "Aggregate timesheet data"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [clj-time "0.8.0"]]
  :main timetotal.core
  :aot [timetotal.core])
