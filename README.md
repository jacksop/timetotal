# timetotal

A Clojure command line utility to parse a timesheet file and print a table of hours by month.

## Timesheet file

We ar eusing a very specific format for the timesheet file. Here is a section showing a couple
of entries.

    EVENT 2 OF 19
    Summary:  Project-XYZ
    Status: none
    Date: 02/09/2014 to 02/09/2014
    Time: 18:40:00 to 19:30:00
    Location:
    Notes:  Producing style guide

    EVENT 3 OF 19
    Summary:  Project-XYZ
    Status: none
    Date: 02/09/2014 to 02/09/2014
    Time: 22:30:00 to 23:00:00
    Location:
    Notes:  UI discussion

## Usage

Supply the file location as a parameter.

For example, if using leiningen:

    lein run timetotal /Users/Alice/Desktop/timesheet.txt

## License

Copyright © 2014 Paul Jackson, m-bryonic Solutions Ltd

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
