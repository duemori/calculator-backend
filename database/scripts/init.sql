CREATE DATABASE calculator;
CREATE DATABASE person;

CREATE TABLE calculator.operations (
	id int NOT NULL AUTO_INCREMENT,
	name VARCHAR(20) NOT NULL,
	description VARCHAR(20) NOT NULL,
	cost DECIMAL(5,2) NOT NULL,
	PRIMARY KEY (id)
);

INSERT INTO calculator.operations (id, name, description, cost) VALUES (1, 'ADDITION', 'Addition', 0.5);
INSERT INTO calculator.operations (id, name, description, cost) VALUES (2, 'SUBTRACTION', 'Subtraction', 0.6);
INSERT INTO calculator.operations (id, name, description, cost) VALUES (3, 'MULTIPLICATION', 'Multiplication', 1.2);
INSERT INTO calculator.operations (id, name, description, cost) VALUES (4, 'DIVISION', 'Division', 1.7);
INSERT INTO calculator.operations (id, name, description, cost) VALUES (5, 'SQUARE_ROOT', 'Square root', 2.3);
INSERT INTO calculator.operations (id, name, description, cost) VALUES (6, 'RANDOM_STRING', 'Random string', 3.5);

CREATE TABLE calculator.transactions (
	id int NOT NULL AUTO_INCREMENT,
	operations_id int NULL,
	users_id int NOT NULL,
	amount DECIMAL(9,2) NOT NULL,
	credit_debit CHAR(1) NOT NULL,
	params VARCHAR(25) NULL,
	response VARCHAR(20) NULL,
	date DATETIME NOT NULL,
	active BOOLEAN NOT NULL DEFAULT 1,
	PRIMARY KEY (id),
	FOREIGN KEY (operations_id) REFERENCES operations(id),
	INDEX transactions_users_id_idx (users_id)
);

CREATE TABLE person.users (
	id int NOT NULL AUTO_INCREMENT,
	email VARCHAR(255) NOT NULL,
	password VARCHAR(255) NOT NULL,
	date DATETIME NOT NULL,
	active BOOLEAN NOT NULL DEFAULT 1,
	PRIMARY KEY (id),
	INDEX users_email_idx (email)
);

CREATE USER 'calculator'@'%' IDENTIFIED BY 'calc123';
GRANT ALL PRIVILEGES ON calculator.* TO 'calculator'@'%';

CREATE USER 'person'@'%' IDENTIFIED BY 'person123';
GRANT ALL PRIVILEGES ON person.* TO 'person'@'%';