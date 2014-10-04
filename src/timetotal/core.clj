(ns timetotal.core
  (:require [clj-time.core :refer [date-time interval in-minutes month year]]
            [clojure.pprint :refer [print-table]])
  (:gen-class))


(defn date?
  "Returns the date if its a date line, otherwise nil"
  [line]
  (let [groups (re-matches #"^Date:\s+([0-9]{2}/[0-9]{2}/[0-9]{4}) to.*" line)]
    (last groups)))

(defn times?
  "Returns the time if its a time line, otherwise nil"
  [line]
  (let [groups (re-matches #"^Time:\s+([0-9]{2}:[0-9]{2}:[0-9]{2}) to ([0-9]{2}:[0-9]{2}:[0-9]{2})" line)]
    (next groups)))

(defn add-entry
  "Add the entry to the collection"
  [collection entry]
  (if (empty? entry)
    collection
    (conj collection entry)))

(defn extract-ints
  "Match using the pattern on the string, returning the matched groups as integers"
  [pattern str]
  (map #(Integer. %) (rest (re-matches pattern str))))

(defn to-date
  "Create a date-time from the date and time strings"
  [date-str time-str]
  (let [dmy (extract-ints #"([0-9]{2})/([0-9]{2})/([0-9]{4})" date-str)
        hms (extract-ints #"([0-9]{2}):([0-9]{2}):([0-9]{2})" time-str)]
    (apply date-time (into hms dmy))))

(defn add-times
  "Add the times to the current entry"
  [entry times]
  (let [start (to-date (entry :date)(first times))
        end (to-date (entry :date)(last times))
        mth (month start)
        yr (year start)
        duration (in-minutes (interval start end))]
    (assoc entry :start start :end end :duration duration :month mth :year yr)))

(defn fetch-file
  "Return the file at path as a string"
  [path]
  (slurp path))

(defn parse-file
  "Parse the file and return a collection of entry hashes"
  [file]
  (let [lines (clojure.string/split-lines file)]
    (loop [lines lines
           coll []
           curr {}]
      (if lines
        (let [line (first lines)
              date (date? line)
              times (times? line)]
          (if date
            (recur (next lines)
                   (add-entry coll curr)
                   {:date date})
            (if times
              (recur (next lines)
                     coll
                     (add-times curr times))
              (recur (next lines)
                     coll
                     curr))))
        (add-entry coll curr)))))

(defn sum-duration
  "Sum the fields keyed by duration in the collection"
  [coll]
  (->> (for [x coll] (select-keys x [:duration]))
       (apply merge-with +)
       :duration))

(defn group-total
  "Return totals for a collection of entries"
  [x]
  (let [[[year month] group] x
        total (sum-duration group)]
    {:year year :month month :hours (format "%.2f" (float (/ total 60)))}))

(defn month-total
  "Return totals grouped by year and month"
  [data]
  (map group-total (group-by (juxt :year :month) data)))

(defn -main
  [& args]
  (let [file (first args)]
    (if file
      (print (->> (fetch-file file)
                  parse-file
                  month-total
                  print-table))
      (println "Usage: cmdline FILE-PATH"))))
