CREATE DATABASE akka;

CREATE TABLE akka.Quote (
    Symbol varchar(255),
    Price float(255),
    Amount float(255),
    Action varchar(255)
);

INSERT INTO akka.Persons (Symbol, Price, Amount, Action)
VALUES ("AAPL", 145.32, 132.51 'Buy');
