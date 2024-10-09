# Pairs.md

You must create an app that forms teams of `X` similar people from a total of `Y` people.  The app should form no more than `ceil(Y/X)` teams.

The user of the app must be able to select the input file from a list of available file names.  The user will select from a dropdown menu.

When a button is clicked, your app must display the valid data in a nicely formatted table.

The user of the app must be able to input values for `X` and `Y`.

If 'X' is greater than what we want, the

## Data
The people data are stored in a CSV file.  Each line of the file has a person's email, first name, last name, and skill level like

    kdrexel@xula.edu,katHARine,DreXel,eXPert

You should ignore all invalid lines of data.  An input line is invalid if the email address is improperly formatted (only check format), either of the two names are missing, or the skill level is an invalid value.  A line of data with more than 3 commas isn't necessarily invalid, instead just ignore all text after the skill level.

## Skill Levels
The skill levels (in increasing order) are [NOVICE, COMPETENT, EXPERT].  The UNKNOWN skill level means the team doesn't know that person's skill level.  Your app must use an enumerated class for the skill levels.

### Similar?

The user provides a lambda that determines if two people are similar.  Given two people, this predicate returns True if the two people are similar or False if the two people are not similar.

## Help!

* Write code along with me during class and ask lots of questions.
* Watch Riddhi Dutta's [video](https://www.youtube.com/watch?v=0ada8fAMpVs) more than once.  Watch him the first time then write code along with him rather than merely watching him code.
* If you want a fun read with lots of lambda and stream examples, read [this](https://www.cs.cmu.edu/~charlie/courses/15-214/2016-fall/slides/26%20-%20Lambdas%20and%20Streams.pdf).  It's written by one of the world's most respected developers (Dr. Joshua Bloch).
* Join my office hours on Wednesdays and Thursdays 7-9 PM in [Zoom](https://xula.zoom.us/j/93501348600).