CREATE TABLE Data(
    ID INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    Type VARCHAR(255),
    Title VARCHAR(255),
    Time TIMESTAMP,
    Amount DOUBLE,
    Comment VARCHAR(1000)
);
