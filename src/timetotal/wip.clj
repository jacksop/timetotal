(defn date? [line]
  (let [groups (re-matches #"^Date:\s+([0-9]{2}/[0-9]{2}/[0-9]{4}) to.*" line)]
    (last groups)))

(defn times? [line]
  (let [groups (re-matches #"^Time:\s+([0-9]{2}:[0-9]{2}:[0-9]{2}) to ([0-9]{2}:[0-9]{2}:[0-9]{2})" line)]
    (next groups)))

(defn add-entry [collection entry]
  (if (empty? entry)
    collection
    (conj collection entry)))

(defn add-times [entry times]
  (let [start (first times)
        end (last times)]
    (assoc entry :start start :end end)))

(defn load-file [path]
  (slurp path))

(defn parse-file [file]
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

(def f (load-file "/Users/jacksop/Desktop/Realtime-timesheet.txt"))
(parse-file f)