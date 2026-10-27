# Customer Feedback Portal

## Description

A Java console application that allows staff to manage customer feedback.

Each feedback entry contains a customer name, a rating from 1 to 5, and a comment.

## Features

- Add customer feedback
- Display all feedback
- Calculate the average rating
- Search feedback by rating
- Delete a feedback entry
- Save feedback to a text file
- Load saved feedback when the program starts
- Handle invalid numeric input and file errors

## Technologies

- Java
- Arrays
- Loops and conditional statements
- Switch statements
- BufferedReader
- BufferedWriter
- Exception handling

## How to Run

Compile the program.

```bash
javac FeedbackPortal.java
```

Run the program.

```bash
java FeedbackPortal
```

## Data Storage

Feedback is saved in `feedback.txt`. Each record contains the customer name, rating, and comment, separated by the pipe character.

## Testing

The program was tested with invalid menu input, invalid ratings, feedback management, average calculation, deletion validation, and saving and loading feedback.